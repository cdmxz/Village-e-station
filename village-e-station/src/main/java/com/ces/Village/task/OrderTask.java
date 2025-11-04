package com.ces.village.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ces.village.constant.OrderStatusConstant;
import com.ces.village.pojo.entity.Orders;
import com.ces.village.service.OrdersService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单任务
 */
@Log4j2
@Component
public class OrderTask {
    @Autowired
    private OrdersService ordersService;

    /**
     * 每分钟执行一次
     * 扫描未付款的订单，到达截止时间的订单 设为已关闭
     */
    @Scheduled(cron = "0 */1 * * * ?") // 每分钟的第0秒执行
    public void refundTask() {
        // 查询未付款的订单列表
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getOrderStatus, OrderStatusConstant.UNPAID);
        List<Orders> list = ordersService.list(queryWrapper);
        LocalDateTime now = LocalDateTime.now();
        for (Orders item : list) {
            // 退款时间在现在的时间之前
            // 表示订单已到达截止日期
            LocalDateTime expire = item.getExpireTime();
            if (expire != null && expire.isBefore(now)) {
                // 设置订单状态为已关闭
                item.setOrderStatus(OrderStatusConstant.CLOSE);
                ordersService.updateById(item);
            }
        }
        log.info("扫描未付款的订单列表...");
    }
}
