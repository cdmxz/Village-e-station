package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.constant.RefundStatusConstant;
import com.ces.Village.exception.CustomException;
import com.ces.Village.mapper.OrderDetailMapper;
import com.ces.Village.pojo.entity.OrderDetail;
import com.ces.Village.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-18
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 更改订单号
     *
     * @param oldOrderNo
     * @param newOrderNo
     * @return
     */
    public boolean changeOrderNo(Long oldOrderNo, Long newOrderNo) {
        return orderDetailMapper.changeOrderNo(oldOrderNo, newOrderNo) > 0;
    }

    /**
     * 根据订单号和商品id查询订单详情表
     *
     * @param orderNo
     * @param goodId
     * @return
     */
    @Override
    public OrderDetail getByOrderNoAndGoodId(String orderNo, String goodId) {
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderNo, orderNo).eq(OrderDetail::getGoodId, goodId);
        return orderDetailMapper.selectOne(queryWrapper);
    }

    /**
     * 根据订单号和商品id 修改 订单详情表的状态
     *
     * @param orderNo
     * @param goodId
     * @param status
     * @return
     */
    @Override
    public boolean updateStatus(String orderNo, String goodId, Integer status) {
        OrderDetail orderDetail = getByOrderNoAndGoodId(orderNo, goodId);
        orderDetail.setStatus(status);
        return updateStatus(orderDetail, status);
    }

    @Override
    public List<OrderDetail> listByOrderNo(String orderNo) {
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderNo, orderNo);
        return orderDetailMapper.selectList(queryWrapper);
    }

    /**
     * 修改 订单详情表的状态
     *
     * @param orderDetail
     * @param status
     * @return
     */
    public boolean updateStatus(OrderDetail orderDetail, Integer status) {
        if (!RefundStatusConstant.isValid(status)) {
            throw new CustomException(ErrorCodeEnum.PARAM_IS_INVALID);
        }
        orderDetail.setStatus(status);
        return orderDetailMapper.updateById(orderDetail) > 0;
    }
}
