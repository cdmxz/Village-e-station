package com.ces.village.controller.wx;

import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.constant.OrderStatusConstant;
import com.ces.village.constant.RefundStatusConstant;
import com.ces.village.exception.CustomException;
import com.ces.village.pojo.entity.OrderDetail;
import com.ces.village.pojo.entity.Orders;
import com.ces.village.pojo.entity.Refund;
import com.ces.village.service.OrderDetailService;
import com.ces.village.service.OrdersService;
import com.ces.village.service.RefundService;
import com.ces.village.service.WxMsgService;
import com.ces.village.utils.JsonConvertUtil;
import com.ces.village.utils.WxPayUtil;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.refund.model.RefundNotification;
import com.wechat.pay.java.service.refund.model.Status;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 微信支付 回调
 */
@Log4j2
 @Tag(name = "微信支付")
@RestController
@RequestMapping("/api/wxpay")
public class WxPayController {

    @Autowired
    private WxPayUtil wxPayUtil;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private WxMsgService wxMsgService;

    /**
     * 微信支付回调接口
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/notify")
    public String notify(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("微信支付 支付回调...");
            Transaction transaction = wxPayUtil.parseNotify(request);
            log.info(JsonConvertUtil.toJsonString(transaction));
            Transaction.TradeStateEnum orderState = transaction.getTradeState();
            if (orderState.equals(Transaction.TradeStateEnum.SUCCESS)) {
                // 如果支付成功，修改订单状态 为待发货
                String orderNo = transaction.getOutTradeNo();
                // 查询订单
                Orders orders = ordersService.getByOrderNo(orderNo);
                if (orders == null) {
                    throw new CustomException(ErrorCodeEnum.ORDER_NOT_FOUND);
                }
                orders.setExpireTime(null);
                orders.setOrderStatus(OrderStatusConstant.UNSEND);
                ordersService.updateById(orders);
                // 查询订单详情表
                List<OrderDetail> list = orderDetailService.listByOrderNo(orders.getOrderNo().toString());
                orders.setOrderDetails(list);
                wxMsgService.paymentSuccessPush(orders);
            }
        } catch (Exception e) {
            if (e instanceof CustomException) {
                log.error("微信支付回调报错：" + e.getMessage());
            } else if (e instanceof com.wechat.pay.java.core.exception.ValidationException) {
                // 签名验证失败，返回 401 UNAUTHORIZED 状态码
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        response.setStatus(200);
        return "{" +
                '"' + "code" + '"' + ":" + '"' + "SUCCESS" + '"' + " " +
                '"' + "message" + '"' + ":" + '"' + "成功" + '"' +
                "}";
    }

    /**
     * 微信支付 退款回调接口
     * 订单管理员同意退款有重复代码 勿删
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/notify/refund")
    public String notifyRefund(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("微信支付 退款回调...");
            RefundNotification refundNotify = wxPayUtil.parseRefundNotify(request);
            log.info(JsonConvertUtil.toJsonString(refundNotify));
            com.wechat.pay.java.service.refund.model.Status refundStatus = refundNotify.getRefundStatus();
            String orderNo = refundNotify.getOutTradeNo();
            String refundNo = refundNotify.getOutRefundNo();
            if (refundStatus.equals(Status.SUCCESS)) {
                Refund refund = refundService.getByRefundNo(refundNo);
                // 如果退款成功，修改 商品详情表中的该商品的状态 为退款成功
                orderDetailService.updateStatus(refund.getOrderNo().toString(), refund.getGoodId().toString(), RefundStatusConstant.SUCCESS_REFUND);
                // 修改退款表该记录为退款成功
                refund.setStatus(RefundStatusConstant.SUCCESS_REFUND);
                refundService.updateById(refund);
                // 如果该订单的全部商品都退款完成了，就修改订单状态为已关闭
                if (ordersService.isFullRefund(orderNo)) {
                    ordersService.updateOrderStatus(orderNo, OrderStatusConstant.CLOSE);
                }
            }
        } catch (Exception e) {
            if (e instanceof CustomException) {
                log.error("微信支付退款回调报错：" + e.getMessage());
            } else if (e instanceof com.wechat.pay.java.core.exception.ValidationException) {
                // 签名验证失败，返回 401 UNAUTHORIZED 状态码
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
        response.setStatus(200);
        return "{" +
                '"' + "code" + '"' + ":" + '"' + "SUCCESS" + '"' + " " +
                '"' + "message" + '"' + ":" + '"' + "成功" + '"' +
                "}";
    }
}
