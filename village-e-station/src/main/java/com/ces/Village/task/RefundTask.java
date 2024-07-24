package com.ces.Village.task;

import com.ces.Village.constant.RefundApplyConstant;
import com.ces.Village.pojo.entity.Refund;
import com.ces.Village.service.OrdersService;
import com.ces.Village.service.RefundService;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;


/**
 * 微信退款任务
 */
@Log4j2
@Component
public class RefundTask {
    @Autowired
    private RefundService refundService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 每分钟执行一次
     * 调用微信支付api，发起退款
     */
    //@Scheduled(cron = "0 */1 * * * ?") // 每分钟的第0秒执行
    public void refundTask() {
        log.info("调用微信支付api，发起退款...");
        // 查出同意退款的退款记录
        List<Refund> refundList = refundService.getPendingRefundList();
        log.info("待退款记录数为 {}条", refundList.size());
        for (Refund refund : refundList) {
            String refundNo = "refund:" + refund.getRefundNo().toString();
            // 同一个订单号的多笔退款，需要相隔1分钟
            if (Boolean.TRUE.equals(redisTemplate.hasKey(refundNo))) {
                continue;
            }
            redisTemplate.boundValueOps(refundNo).expire(Duration.ofSeconds(61));
            try {
                ordersService.refund(refundNo);
                refund.setIsApply(RefundApplyConstant.APPLY_FOR);
            } catch (Exception e) {
                log.info("退款单号：{}，退款出错：{}", refundNo, e.getMessage());
                refund.setErrReason(e.getMessage());
                refund.setIsApply(RefundApplyConstant.NO_APPLY);
            }
            refundService.updateById(refund);
        }
    }
}
