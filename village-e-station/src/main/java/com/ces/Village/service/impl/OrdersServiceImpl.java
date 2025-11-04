package com.ces.village.service.impl;


import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.village.common.BaseContext;
import com.ces.village.constant.*;
import com.ces.village.exception.CustomException;
import com.ces.village.mapper.OrdersMapper;
import com.ces.village.pojo.dto.OrderAddDTO;
import com.ces.village.pojo.dto.OrderAddGoodsDTO;
import com.ces.village.pojo.dto.RefundDTO;
import com.ces.village.pojo.entity.*;
import com.ces.village.pojo.vo.OrderDetailsVO;
import com.ces.village.service.*;
import com.ces.village.utils.ConvertUtil;
import com.ces.village.utils.SnowflakeUtil;
import com.ces.village.utils.WxPayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-18
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private WxPayUtil wxPayUtil;
    @Autowired
    private UsersService usersService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private WxMsgService wxMsgService;


    /**
     * 查看订单详情
     *
     * @param ordersNo
     * @return
     */
    public OrderDetailsVO details(String ordersNo) {
        // 根据订单号查询订单
        Orders orders = ordersMapper.getDetailsByOrderNo(ordersNo);
        if (orders == null) {
            throw new CustomException(ErrorCodeEnum.ORDER_NOT_FOUND);
        }
        OrderDetailsVO orderDetailsVO = ConvertUtil.entityToVo(orders, OrderDetailsVO.class);
        orderDetailsVO.setOrderDetails(orders.getOrderDetails());
        return orderDetailsVO;
    }

    /**
     * 添加订单
     *
     * @param orderAddDTO
     * @return
     */
    @Transactional // 开启事务管理
    public JSONObject addOrder(OrderAddDTO orderAddDTO) throws FileNotFoundException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        UserAddress userAddress = userAddressService.getById(orderAddDTO.getUserAddressId());
        if (userAddress == null) {
            throw new CustomException(ErrorCodeEnum.ADDRESS_BOOK_IS_NULL);
        }
        Long userId = BaseContext.getCurrentUser().getId();
        // 生成订单号
        Long orderNo = SnowflakeUtil.getId();
        // 总金额
        BigDecimal totalPrice = BigDecimal.ZERO;
        // 订单明细数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (OrderAddGoodsDTO goodsDTO : orderAddDTO.getGoods()) {
            OrderDetail orderDetail = new OrderDetail();
            Goods good = goodsService.getById(goodsDTO.getGoodId());
            if (good == null) {
                throw new CustomException(ErrorCodeEnum.GOODS_NOT_FOUND);
            }
            // 判断商品货存
            int surp = good.getSurplus() - goodsDTO.getNumber();
            if (surp < 0) {
                throw new CustomException(ErrorCodeEnum.OUT_OF_STOCK);
            }
            // 更新剩余库存
            good.setSurplus(surp);
            if (surp == 0) {// 库存为0，修改状态为库存不足
                good.setSurplus(GoodStatusConstant.OUT_OF_STOCK);
            }
            // 增加销量
            good.setSalesQuantity(good.getSalesQuantity() + goodsDTO.getNumber());
            good.setRecentSales(good.getRecentSales() + goodsDTO.getNumber());
            goodsService.updateById(good);
            // 计算总金额
            totalPrice = totalPrice.add(good.getPrice()).multiply(BigDecimal.valueOf(goodsDTO.getNumber()));
            orderDetail.setName(good.getName())
                    .setCode(good.getCode())
                    .setPrice(good.getPrice())
                    .setThumbnailUrl(good.getThumbnailUrl())
                    .setQuantity(goodsDTO.getNumber())
                    .setOrderNo(orderNo)
                    .setGoodId(good.getId());
            orderDetailList.add(orderDetail);
        }
        // 构造订单数据
        Orders orders = new Orders();
        orders.setActualAmount(totalPrice)
                .setTotalAmount(totalPrice)
                .setReceiveInformation(userAddress.combination())
                .setOrderStatus(OrderStatusConstant.UNPAID)// 待付款
                .setOrderTime(LocalDateTime.now())
                .setExpireTime(LocalDateTime.now().plusMinutes(15))// 15分钟后到期
                .setUserId(userId)
                .setOrderNo(orderNo);
        // 调用微信支付api获取 发给小程序的数据
        JSONObject result = miniProgramPay(orders, userId);
        // 向订单表插入1条数据
        ordersMapper.insert(orders);
        // 向明细表插入n条数据
        orderDetailService.saveBatch(orderDetailList);
        return result;
//        return null;
    }

    /**
     * 修改订单状态
     */
    @Override
    public boolean updateOrderStatus(Orders orders, Integer status) {
        orders.setOrderStatus(status);
        return ordersMapper.updateById(orders) > 0;
    }

    /**
     * 修改订单状态
     */
    @Override
    public boolean updateOrderStatus(String orderNo, Integer status) {
        if (!OrderStatusConstant.isValid(status)) {
            throw new CustomException(ErrorCodeEnum.ORDER_STATUS_ERROR);
        }
        // 查询订单
        Orders orders = getByOrderNo(orderNo);
        if (orders == null) {
            throw new CustomException(ErrorCodeEnum.ORDER_NOT_FOUND);
        }
        return updateOrderStatus(orders, status);
    }

    /**
     * 调用微信api发起支付，并获取给小程序端的数据
     *
     * @param order
     * @return
     */
    private JSONObject miniProgramPay(Orders order, Long userId) throws FileNotFoundException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        String orderNo = String.valueOf(order.getOrderNo());
        BigDecimal total = order.getTotalAmount();
        String description = "乡村e站商城";
        Users user = usersService.getById(userId);
        String openId = user.getOpenId();
        return wxPayUtil.miniProgramPay(orderNo, total, description, openId);
    }

    /**
     * 查询订单列表
     *
     * @return
     */
    public Page<Orders> getOrderList(Integer currentPage, String keyword) {
        Page<Orders> page = new Page<>(currentPage, 10);
        return ordersMapper.getOrderListByStatus(page, keyword, OrderStatusConstant.ALL);
    }

    /**
     * 根据订单状态查询订单列表
     *
     * @return
     */
    public Page<Orders> getOrderListByStatus(Integer currentPage, String keyword, Integer orderStatus) {
        Page<Orders> page = new Page<>(currentPage, 10);
        return ordersMapper.getOrderListByStatus(page, keyword, orderStatus);
    }

    /**
     * 根据订单号查询订单
     *
     * @param orderNo
     * @return
     */
    @Override
    public Orders getByOrderNo(String orderNo) {
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderNo, orderNo);
        return ordersMapper.selectOne(queryWrapper);
    }

    /**
     * 根据订单状态和用户id 查询订单列表
     *
     * @param currentPage
     * @param orderStatus
     * @param userId
     * @return
     */
    @Override
    public Page<Orders> getOrderListByStatusAndUserId(Integer currentPage, Integer orderStatus, Long userId) {
        Page<Orders> page = new Page<>(currentPage, 10);
        return ordersMapper.getOrderListByStatusAndUserId(page, orderStatus, userId);
    }

    /**
     * 根据用户id 查询订单列表
     *
     * @param currentPage
     * @return
     */
    @Override
    public Page<Orders> getOrderListByUserId(Integer currentPage, Long userId) {
        Page<Orders> page = new Page<>(currentPage, 10);
        return ordersMapper.getOrderListByStatusAndUserId(page, OrderStatusConstant.ALL, userId);
    }

    /**
     * 重新下单支付
     *
     * @param orderNo
     * @return
     */
    @Transactional
    @Override
    public JSONObject repay(String orderNo) {
        // 查询订单
        Orders orders = getByOrderNo(orderNo);
        if (orders == null) {
            throw new CustomException(ErrorCodeEnum.ORDER_NOT_FOUND);
        }
        // 重新支付的时间要距离下单时间大于30秒
        // 避免重复下单
        if (orders.getOrderTime().plusSeconds(30).isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorCodeEnum.PAYMENT_INTERVAL_TOO_SHORT);
        }
        wxPayUtil.CloseOrder(orderNo);
        Long userId = BaseContext.getCurrentUser().getId();
        // 生成新的订单号
        Long newOrderNo = SnowflakeUtil.getId();
        boolean result = changeOrderNo(orders, newOrderNo);
        if (!result)
            throw new CustomException(ErrorCodeEnum.UPDATE_FAILED);
        try {
            return miniProgramPay(orders, userId);
        } catch (Exception e) {
            log.error("重新支付订单出错：" + e.getMessage());
            throw new CustomException(ErrorCodeEnum.PAYMENT_FAILED);
        }

    }

    /**
     * 订单中的商品退款（退款任务调用）
     *
     * @param refundNo
     * @return
     */
    @Transactional
    @Override
    public void refund(String refundNo) {
        // 退款金额
        BigDecimal refundAmount = BigDecimal.ZERO;
        Refund refund = refundService.getByRefundNo(refundNo);
        if (refund == null) {
            throw new CustomException(ErrorCodeEnum.NOT_FOUND_REFUND);
        }
        // 获得订单号
        String orderNo = refund.getOrderNo().toString();
        // 原订单金额
        Orders orders = getByOrderNo(orderNo);
        BigDecimal total = orders.getTotalAmount();
        // 退款金额
        BigDecimal price = refund.getPrice();
        BigDecimal quantity = BigDecimal.valueOf(refund.getQuantity());
        refundAmount = refundAmount.add(price.multiply(quantity));
        // 退款金额大于支付的总金额
        if (refundAmount.compareTo(total) > 0) {
            throw new CustomException(ErrorCodeEnum.REFUND_GREATER_THAN_TOTAL);
        }
        // 调用微信接口退款
        wxPayUtil.refund(orderNo, refundNo, refundAmount, total);
    }

    /**
     * 是否同意退款（管理员）
     *
     * @param refundDTO
     * @return
     */
    @Transactional
    @Override
    public void agreeRefund(RefundDTO refundDTO) {
        String refundNo = refundDTO.getRefundNo();
        Refund refund = refundService.getByRefundNo(refundNo);
        if (refund == null) {
            throw new CustomException(ErrorCodeEnum.NOT_FOUND_REFUND);
        }
        if (!RefundAgreeConstant.isValid(refundDTO.getIsAgree())) {
            throw new CustomException(ErrorCodeEnum.PARAM_IS_INVALID);
        }
        String orderNo = refund.getOrderNo().toString();
        String goodId = refund.getGoodId().toString();
        refund.setIsAgree(refundDTO.getIsAgree());
        boolean result;
        // 如果设置为不同意退款
        if (refundDTO.getIsAgree().equals(RefundAgreeConstant.DISAGREE)) {
            refund.setStatus(RefundStatusConstant.REJECT_REFUND);
            refund.setRejectReason(refundDTO.getReason());
            // 设置订单详情表，取消该商品的退款状态
            result = orderDetailService.updateStatus(orderNo, goodId, RefundStatusConstant.NO_REFUND);
        } else {
            // 修改退款表该记录为退款成功
            refund.setStatus(RefundStatusConstant.SUCCESS_REFUND);
            // 修改 商品详情表中的该商品的状态 为退款成功
            result = orderDetailService.updateStatus(orderNo, goodId, RefundStatusConstant.SUCCESS_REFUND);
            // 如果该订单的全部商品都退款完成了，就修改订单状态为已关闭
            if (isFullRefund(orderNo)) {
                updateOrderStatus(orderNo, OrderStatusConstant.CLOSE);
            }
        }
        if (!result) {
            throw new CustomException(ErrorCodeEnum.REFUND_ERROR);
        }
        result = refundService.updateById(refund);
        if (!result) {
            throw new CustomException(ErrorCodeEnum.REFUND_ERROR);
        }
        // 微信消息推送
        wxMsgService.refundPush(refund);
    }

    /**
     * 取消退款
     * 订单在 退款中 状态，取消退款
     *
     * @param refundNo
     * @return
     */
    @Transactional
    @Override
    public void cancelRefund(String refundNo) {
        Refund refund = refundService.getByRefundNo(refundNo);
        if (refund == null) {
            throw new CustomException(ErrorCodeEnum.REFUND_NOT_FOUND);
        }
        // 设置订单详情表的 该商品状态为 未退款
        orderDetailService.updateStatus(refund.getOrderNo().toString(),
                refund.getGoodId().toString(),
                RefundStatusConstant.NO_REFUND);
        // 从退款表中删除记录
        refundService.removeById(refund);
    }

    /**
     * 申请订单退款（用户）
     * 后期需要添加一笔订单的多个商品同时退款的话
     * 可以为每一个商品生成一个退款单号（退款记录）
     *
     * @param refundDTO
     * @return
     */
    @Transactional
    @Override
    public boolean requestRefund(RefundDTO refundDTO) {
        // 添加一个退款订单
        String orderNo = refundDTO.getOrderNo();
        String goodId = refundDTO.getGoodId();
        Long userId = BaseContext.getCurrentUser().getId();
        Long refundNo = SnowflakeUtil.getId();
        Orders orders = getByOrderNo(refundDTO.getOrderNo());
        if (orders == null) {
            throw new CustomException(ErrorCodeEnum.ORDER_NOT_FOUND);
        }
        // 根据订货单号和商品id查询退款记录
        Refund refund = refundService.getByOrderNoAndGoodId(orderNo, goodId);
        if (refund != null && !refund.getStatus().equals(RefundStatusConstant.REJECT_REFUND)) {
            // 如果找到了 该订单中的该商品的退款记录，且退款状态也不是拒绝退款
            throw new CustomException(ErrorCodeEnum.HAS_EXISTED_REFUND);
        }
        // 如果订单是待发货状态，直接退款，无需管理员同意
        boolean res = orders.getOrderStatus().equals(OrderStatusConstant.UNSEND);
        Integer isAgree = (res) ? RefundAgreeConstant.AGREE : RefundAgreeConstant.DISAGREE;
        OrderDetail orderDetail = orderDetailService.getByOrderNoAndGoodId(orderNo, goodId);
        if (orderDetail == null) {
            throw new CustomException(ErrorCodeEnum.ORDER_NOT_EXISTED_GOODS);
        }
        refund = ConvertUtil.dtoToEntity(orderDetail, Refund.class);
        refund.setStatus(RefundStatusConstant.PENDING_REFUND)
                .setPutTime(LocalDateTime.now())
                .setUserId(userId)
                .setRefundAmount(orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())))
                .setIsAgree(isAgree)
                .setReason(refundDTO.getReason())
                .setRefundNo(refundNo)
                .setId(null);
        // 设置该商品的状态为待退款，等待在微信退款回调中，设置为退款成功
        orderDetail.setStatus(RefundStatusConstant.PENDING_REFUND);
        orderDetailService.updateById(orderDetail);
        return refundService.save(refund);
    }

    /**
     * 用户取消支付
     *
     * @param orderNo
     */
    @Override
    public void cancelPay(String orderNo) {
        List<OrderDetail> details = orderDetailService.listByOrderNo(orderNo);
        for (OrderDetail detail : details) {
            Goods goods = goodsService.getById(detail.getGoodId());
            // 找不到该商品，商品可能已被删除，跳过
            if (goods == null) {
                continue;
            }
            // 商品库存回归
            goods.setSurplus(goods.getSurplus() + detail.getQuantity());
            // 减去销量
            int sales = goods.getRecentSales() - detail.getQuantity();
            goods.setRecentSales(sales);
            sales = goods.getSalesQuantity() - detail.getQuantity();
            goods.setSalesQuantity(sales);
            goodsService.updateById(goods);
        }
        // 调用微信api关闭订单
        wxPayUtil.CloseOrder(orderNo);
        updateOrderStatus(orderNo, OrderStatusConstant.CLOSE);
    }

    /**
     * 更改订单号
     * 更改订单表和订单详情表
     *
     * @param order
     * @param newOrderNo
     * @return
     */
    public boolean changeOrderNo(Orders order, Long newOrderNo) {
        Long oldNo = order.getOrderNo();
        // 更改订单详情表的订单号
        orderDetailService.changeOrderNo(oldNo, newOrderNo);
        // 更改订单表的订单号
        order.setOrderNo(newOrderNo);
        return ordersMapper.updateById(order) > 0;
    }

    /**
     * 该订单中的商品是否全部为 已退款
     *
     * @param orderNo
     * @return
     */
    public boolean isFullRefund(String orderNo) {
        // 查出该订单号对应的订单详情记录
        List<OrderDetail> list = orderDetailService.listByOrderNo(orderNo);
        for (OrderDetail detail : list) {
            // 只要有一个记录不是退款成功，就返回false
            if (!detail.getStatus().equals(RefundStatusConstant.SUCCESS_REFUND)) {
                return false;
            }
        }
        return true;
    }


}
