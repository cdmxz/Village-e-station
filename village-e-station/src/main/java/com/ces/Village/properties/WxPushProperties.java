package com.ces.village.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信消息推送配置
 */
@Component
@ConfigurationProperties(prefix = "wechat.push")
@Data
public class WxPushProperties {
    private String msgPostToken;// 推送token
    private String msgPostAESKey;// 推送，用于解密消息的AesKey
    private String msgRefundId;// 退款申请通知 模板ID
    private String msgSendOutGoodsId;// 订单发货提醒 模板ID
    private String msgCommentDeletionId;// 评论删除提醒 模板ID
    private String msgReviewId;// 审核结果通知 模板ID
    private String msgPaymentSuccessId;// 支付成功通知 模板ID
}
