package com.ces.village.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.village.pojo.entity.OrderDetail;

import java.util.List;

/**
 * <p>
 * 订单明细表 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-18
 */
public interface OrderDetailService extends IService<OrderDetail> {
    /**
     * 更改订单号
     *
     * @param oldOrderNo
     * @param newOrderNo
     * @return
     */
    boolean changeOrderNo(Long oldOrderNo, Long newOrderNo);

    /**
     * 根据订单号和商品id查询订单详情表
     *
     * @param orderNo
     * @param goodId
     * @return
     */
    OrderDetail getByOrderNoAndGoodId(String orderNo, String goodId);

    /**
     * 根据订单号和商品id 修改 订单详情表的状态
     *
     * @param orderNo
     * @param goodId
     * @param status
     * @return
     */
    boolean updateStatus(String orderNo, String goodId, Integer status);

//    /**
//     * 根据退款单号 修改 订单详情表的状态
//     *
//     * @param refundNo
//     * @param status
//     * @return
//     */
//    boolean updateStatusByRefundNo(String refundNo, Integer status);

    List<OrderDetail> listByOrderNo(String orderNo);


}
