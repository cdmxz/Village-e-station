package com.ces;

import com.ces.village.service.OrderDetailService;
import com.ces.village.service.OrdersService;
import com.ces.village.service.impl.WxMsgServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VillageEStationApplicationTests {
    @Autowired
    private WxMsgServiceImpl wxMsgService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrdersService ordersService;

    @Test
    void contextLoads() {
//        // 查询订单
//        Orders orders = ordersService.getByOrderNo("1758453728178343936");
//        if (orders == null) {
//            throw new CustomException(ErrorCodeEnum.ORDER_NOT_FOUND);
//        }
//        // 查询订单详情表
//        List<OrderDetail> list = orderDetailService.listByOrderNo(orders.getOrderNo().toString());
//        orders.setOrderDetails(list);
//        wxMsgService.paymentSuccessPush(orders);
    }

    @Test
    void TestJobTask() {
//        service.updateDeadlineStatus();
    }


//    @Test
//    void testPay() {
////        try {
////            String payId = weChatPayUtil.jsapiPrepay("1738901080685244416",
////                    BigDecimal.valueOf(100),
////                    "测试",
////                    "oSl6h60GFEcP1_e5R2wrO9YF2Jzk");
////            System.out.println(payId);
////        } catch (Exception e) {
////            System.out.println(e.getMessage());
////        }
//    }
//
//    @Test
//    void testClose() {
////        try {
////            weChatPayUtil.CloseOrder("1738901080685244416");
////
////        } catch (Exception e) {
////            System.out.println(e.getMessage());
////        }
//    }
//
//    @Test
//    void testQuery() {
////        try {
////            Transaction transaction = weChatPayUtil.queryOrderByOutTradeNo("1738901080685244416");
////            System.out.println(transaction);
////        } catch (Exception e) {
////            System.out.println(e.getMessage());
////        }
//    }
}
