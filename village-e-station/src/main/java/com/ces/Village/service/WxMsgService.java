package com.ces.village.service;

import com.ces.village.pojo.dto.ReviewPushDTO;
import com.ces.village.pojo.dto.WxMediaCheckPushResponse;
import com.ces.village.pojo.entity.Comments;
import com.ces.village.pojo.entity.Orders;
import com.ces.village.pojo.entity.Refund;

/**
 * <p>
 * 微信消息 服务类
 * </p>
 *
 */
public interface WxMsgService {
    /**
     * 推送微信消息给用户
     *
     * @param openid
     * @param templateId
     * @return
     */
    void msgPush(String openid, String templateId, String data) throws Exception;

    /**
     * 退款通知
     */
    void refundPush(Refund refund);

    /**
     * 订单发货提醒
     * orders要包含orderDetails
     */
    void sendOutGoodsPush(Orders orders);

    /**
     * 评论删除提醒
     */
    void commentDeletionPush(Comments comments);

    /**
     * 审核结果通知
     */
    void reviewPush(ReviewPushDTO data);

    /**
     * 支付成功通知
     * orders要包含orderDetails
     */
    void paymentSuccessPush(Orders orders);

    /**
     * 校验微信服务器的签名
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    boolean checkSignature(String signature, String timestamp, String nonce);

    /**
     * 媒体检测接口 回调函数
     * @param pushDTO
     */
    void mediaCheckCallBack(WxMediaCheckPushResponse pushDTO);

    /**
     * 查询rid对应的错误消息
     * 微信消息推送失败会返回rid
     * 通过rid查询失败原因
     * @param rid
     * @return
     */
     String queryRid(String rid);
}
