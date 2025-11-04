package com.ces.village.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.village.pojo.dto.OrderAddDTO;
import com.ces.village.pojo.dto.RefundDTO;
import com.ces.village.pojo.entity.Orders;
import com.ces.village.pojo.vo.OrderDetailsVO;

import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;


/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-18
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 根据订单号查询订单详情
     *
     * @param ordersNo
     * @return
     */
    OrderDetailsVO details(String ordersNo);

    /**
     * 添加订单
     *
     * @param orderAddDTO
     * @return
     */
    JSONObject addOrder(OrderAddDTO orderAddDTO) throws FileNotFoundException, NoSuchAlgorithmException, SignatureException, InvalidKeyException;

    /**
     * 修改订单状态
     */
    boolean updateOrderStatus(String orderNo, Integer status);

    /**
     * 修改订单状态
     */
    boolean updateOrderStatus(Orders orders, Integer status);

    /**
     * 查询订单列表
     *
     * @return
     */
    Page<Orders> getOrderList(Integer currentPage, String keyword);

    /**
     * 根据订单状态查询订单列表
     *
     * @param orderStatus
     * @return
     */
    Page<Orders> getOrderListByStatus(Integer currentPage, String keyword, Integer orderStatus);


    /**
     * 根据订单号查询订单
     *
     * @param orderNo
     * @return
     */
    Orders getByOrderNo(String orderNo);

    /**
     * 根据订单状态和用户id 查询订单列表
     *
     * @param orderStatus
     * @return
     */
    Page<Orders> getOrderListByStatusAndUserId(Integer currentPage, Integer orderStatus, Long userId);

    /**
     * 根据用户id 查询订单列表
     *
     * @return
     */
    Page<Orders> getOrderListByUserId(Integer currentPage, Long userId);

    /**
     * 重新下单支付
     *
     * @param orderNo
     * @return
     */
    JSONObject repay(String orderNo);

    /**
     * 订单中的商品退款（退款任务调用）
     *
     * @param refundNo
     * @return
     */
    void refund(String refundNo);

    /**
     * 是否同意退款（管理员）
     *
     * @param refundDTO
     * @return
     */
    void agreeRefund(RefundDTO refundDTO);

    /**
     * 取消退款
     *
     * @param refundNo
     * @return
     */
    void cancelRefund(String refundNo);

    /**
     * 申请订单退款（用户）
     *
     * @param refundDTO
     * @return
     */
    boolean requestRefund(RefundDTO refundDTO);

    /**
     * 用户取消支付
     *
     * @param orderNo
     */
    void cancelPay(String orderNo);


    /**
     * 该订单中的商品是否全部退款了
     *
     * @param orderNo
     * @return
     */
    boolean isFullRefund(String orderNo);


}
