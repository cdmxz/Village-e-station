package com.ces.Village.controller.user;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.annotation.LoginRequired;
import com.ces.Village.common.BaseContext;
import com.ces.Village.common.CurrentUser;
import com.ces.Village.common.R;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.constant.OrderStatusConstant;
import com.ces.Village.pojo.dto.OrderAddDTO;
import com.ces.Village.pojo.dto.OrderSendDTO;
import com.ces.Village.pojo.dto.OrderStatusDTO;
import com.ces.Village.pojo.dto.RefundDTO;
import com.ces.Village.pojo.entity.OrderDetail;
import com.ces.Village.pojo.entity.Orders;
import com.ces.Village.pojo.entity.Refund;
import com.ces.Village.pojo.vo.OrderDetailsVO;
import com.ces.Village.pojo.vo.OrderListVO;
import com.ces.Village.pojo.vo.RefundListVO;
import com.ces.Village.pojo.vo.RefundVo;
import com.ces.Village.service.OrderDetailService;
import com.ces.Village.service.OrdersService;
import com.ces.Village.service.RefundService;
import com.ces.Village.service.WxMsgService;
import com.ces.Village.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 订单表 前端控制器
 * </p>
 *
 * @author author
 * @since 2023-11-18
 */
@Log4j2
@RestController
@Api(tags = "订单接口")
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private RefundService refundService;

    @Autowired
    private WxMsgService wxMsgService;

    /**
     * 添加订单
     *
     * @param orderAddDTO
     * @return
     */
    @LoginRequired
    @PostMapping("/admin/add")
    public R<?> add(@RequestBody OrderAddDTO orderAddDTO) {
        JSONObject obj;
        try {
            obj = ordersService.addOrder(orderAddDTO);
        } catch (Exception e) {
            return R.error(ErrorCodeEnum.ORDER_CREATE_FAILED);
        }
        return R.success(obj);
    }

    /**
     * 查询订单列表（管理员）
     *
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @GetMapping("/admin/list")
    public R<?> list(@RequestParam("page") Integer currentPage,
                     @RequestParam(value = "keyword", required = false) String keyword) {
        Page<Orders> page = ordersService.getOrderList(currentPage, keyword);
        OrderListVO data = OrderListVO.create(page);
        return R.success(data);
    }

    /**
     * 根据状态查询 我的 订单列表
     *
     * @return
     */
    @LoginRequired
    @GetMapping("/my/listbystatus")
    public R<?> myListByStatus(@RequestParam("page") Integer currentPage,
                               @RequestParam("order_status") Integer orderStatus) {
        if (!OrderStatusConstant.isValid(orderStatus))
            return R.error(ErrorCodeEnum.ORDER_STATUS_ERROR);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Page<Orders> page = ordersService.getOrderListByStatusAndUserId(currentPage, orderStatus, currentUser.getId());
        OrderListVO data = OrderListVO.create(page);
        return R.success(data);
    }

    /**
     * 查看 我的 订单列表
     *
     * @return
     */
    @LoginRequired
    @GetMapping("/my/list")
    public R<?> myList(@RequestParam("page") Integer currentPage) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Page<Orders> page = ordersService.getOrderListByUserId(currentPage, currentUser.getId());
        OrderListVO data = OrderListVO.create(page);
        return R.success(data);
    }

    /**
     * 根据状态查询订单列表（管理员）
     *
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @GetMapping("/admin/listbystatus")
    public R<?> listByStatus(@RequestParam("page") Integer currentPage,
                             @RequestParam("order_status") Integer orderStatus,
                             @RequestParam(value = "keyword", required = false) String keyword) {
        if (!OrderStatusConstant.isValid(orderStatus))
            return R.error(ErrorCodeEnum.ORDER_STATUS_ERROR);
        Page<Orders> page = ordersService.getOrderListByStatus(currentPage, keyword, orderStatus);
        OrderListVO data = OrderListVO.create(page);
        log.info("根据状态查看订单列表(管理员):{}", data);
        return R.success(data);
    }


    /**
     * 查看订单详情
     *
     * @param ordersNo
     * @return
     */
    @LoginRequired
    @GetMapping("/details")
    @ApiOperation("查看订单详情")
    public R<OrderDetailsVO> details(@RequestParam("order_no") String ordersNo) {
        log.info("订单详情：{}", ordersNo);
        OrderDetailsVO detail = ordersService.details(ordersNo);
        return R.success(detail);
    }

    /**
     * 删除订单并删除订单详情
     *
     * @return
     */
    @LoginRequired
    @DeleteMapping("/del")
    @Transactional
    public R<Orders> delete(@RequestParam("order_no") String orderNo) {
        log.info("删除订单和订单详情...");
        Orders orders = ordersService.getByOrderNo(orderNo);
        if (orders == null) {
            return R.error(ErrorCodeEnum.ORDER_NOT_FOUND);
        }
        // 查询订单状态，只有已关闭、已完成、已取消状态才能删除
        if (orders.getOrderStatus().equals(OrderStatusConstant.UNRECEIVE) ||
                orders.getOrderStatus().equals(OrderStatusConstant.UNSEND)) {
            return R.error(ErrorCodeEnum.ORDER_STATUS_ERROR);
        }
        boolean ret;
        // 订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderNo, orderNo);
        ret = ordersService.remove(queryWrapper);
        if (!ret) {
            return R.error(ErrorCodeEnum.DELETE_FAILED);
        }
        // 订单详情
        LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
        detailQueryWrapper.eq(OrderDetail::getOrderNo, orderNo);
        ret = orderDetailService.remove(detailQueryWrapper);
        if (ret) {
            return R.success();
        } else {
            return R.error(ErrorCodeEnum.DELETE_FAILED);
        }
    }

    /**
     * 订单发货
     *
     * @param orderSendDTO
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @PostMapping("/admin/send")
    public R<Orders> send(@RequestBody OrderSendDTO orderSendDTO) {
        // 获取订单号和物流单号
        String orderNo = orderSendDTO.getOrderNo();
        String trackNumber = orderSendDTO.getTrackNumber();

        // 查询订单信息
        Orders orders = ordersService.getByOrderNo(orderNo);
        if (orders == null) {
            return R.error(ErrorCodeEnum.ORDER_NOT_FOUND);
        }
        if (orders.getOrderStatus() != OrderStatusConstant.UNSEND) {
            return R.error(ErrorCodeEnum.ORDER_STATUS_ERROR);
        }

        // 设置发货时间
        orders.setSendTime(LocalDateTime.now());
        // 设置订单号
        orders.setTrackNumber(trackNumber);
        // 点击发货后修改订单状态为待收货
        orders.setOrderStatus(OrderStatusConstant.UNRECEIVE);
        ordersService.updateById(orders);
        // 查询订单详情表
        List<OrderDetail> list = orderDetailService.listByOrderNo(orders.getOrderNo().toString());
        orders.setOrderDetails(list);
        wxMsgService.sendOutGoodsPush(orders);
        return R.success();
    }


    /**
     * 更新订单状态
     */
    @LoginRequired
    @PutMapping("/update/status")
    public R<?> updateStatus(@RequestBody OrderStatusDTO orderStatusDTO) {
        String orderNo = orderStatusDTO.getOrderNo();
        Integer status = orderStatusDTO.getStatus();
        log.info("更新订单状态，订单号：{}", orderNo);
        boolean result = ordersService.updateOrderStatus(orderNo, status);
        if (result) {
            return R.success();
        } else {
            return R.error(ErrorCodeEnum.UPDATE_FAILED);
        }
    }

    /**
     * 重新支付订单
     */
    @LoginRequired
    @PostMapping("/repay")
    public R<?> repay(@RequestBody Orders orderDTO) {
        String orderNo = orderDTO.getOrderNo().toString();
        log.info("重新支付订单，订单号：{}", orderNo);
        JSONObject object = ordersService.repay(orderNo);
        return R.success(object);
    }

    /**
     * 查询退款列表（管理员）
     *
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @GetMapping("/admin/refund/list")
    public R<?> refundList(@RequestParam("page") Integer currentPage) {
        Page<Refund> page = refundService.getRefundList(currentPage);
        List<Refund> list = page.getRecords();
        List<RefundVo> refundVoList = ConvertUtil.entityToVoList(list, RefundVo.class);
        RefundListVO data = RefundListVO.builder()
                .pageCount(page.getPages())
                .list(refundVoList)
                .build();
        return R.success(data);
    }

    /**
     * 查看 我的 退款列表
     *
     * @return
     */
    @LoginRequired
    @GetMapping("/my/refund/list")
    public R<?> myRefundList(@RequestParam("page") Integer currentPage) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Page<Refund> page = refundService.getRefundListByUserId(currentPage, currentUser.getId());
        List<Refund> list = page.getRecords();
        List<RefundVo> refundVoList = ConvertUtil.entityToVoList(list, RefundVo.class);
        RefundListVO data = RefundListVO.builder()
                .pageCount(page.getPages())
                .list(refundVoList)
                .build();
        return R.success(data);
    }

    /**
     * 查看 退款详情
     *
     * @return
     */
    @LoginRequired
    @GetMapping("/refund")
    public R<?> refundDetails(@RequestParam("refund_no") String refundNo) {
        Refund refund = refundService.getByRefundNo(refundNo);
        if (refund == null) {
            return R.error(ErrorCodeEnum.NOT_FOUND_REFUND);
        }
        RefundVo refundVo = ConvertUtil.entityToVo(refund, RefundVo.class);
        return R.success(refundVo);
    }

    /**
     * 申请退款接口（用户）
     */
    @LoginRequired
    @PostMapping("request/refund")
    public R<?> requestRefund(@RequestBody RefundDTO refundDTO) {
        ordersService.requestRefund(refundDTO);
        return R.success();
    }

//    /**
//     * 退款接口（管理员）
//     */
//    @LoginRequired(requireAdmin = true)
//    @PostMapping("admin/refund")
//    public R<?> refund(@RequestBody RefundDTO refundDTO) {
//        try {
//            ordersService.refund(refundDTO);
//            return R.success();
//        } catch (Exception e) {
//            log.error("退款出错：{}", e.getMessage());
//            return R.error(ErrorCodeEnum.REFUND_ERROR);
//        }
//    }

    /**
     * 是否同意退款
     */
    @LoginRequired(requireAdmin = true)
    @PostMapping("admin/agreerefund")
    public R<?> agreeRefund(@RequestBody RefundDTO refundDTO) {
        ordersService.agreeRefund(refundDTO);
        return R.success();
    }

    /**
     * 取消退款接口
     */
    @LoginRequired
    @PostMapping("cancel/refund")
    public R<?> cancelRefund(@RequestBody RefundDTO refundDTO) {
        ordersService.cancelRefund(refundDTO.getRefundNo());
        return R.success();
    }

    /**
     * 取消支付接口
     */
    @LoginRequired
    @PostMapping("cancel/pay")
    public R<?> cancelPay(@RequestBody Orders orderDTO) {
        String orderNo = orderDTO.getOrderNo().toString();
        try {
            ordersService.cancelPay(orderNo);
            return R.success();
        } catch (Exception e) {
            log.error("取消支付接口：{}", e.getMessage());
            return R.error(ErrorCodeEnum.UPDATE_FAILED);
        }
    }
}
