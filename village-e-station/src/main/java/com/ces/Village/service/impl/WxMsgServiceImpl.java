package com.ces.village.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ces.village.constant.RefundStatusConstant;
import com.ces.village.pojo.dto.ReviewPushDTO;
import com.ces.village.pojo.dto.WxMediaCheckPushResponse;
import com.ces.village.pojo.dto.WxMsgPushResponse;
import com.ces.village.pojo.entity.Comments;
import com.ces.village.pojo.entity.OrderDetail;
import com.ces.village.pojo.entity.Orders;
import com.ces.village.pojo.entity.Refund;
import com.ces.village.properties.WxPushProperties;
import com.ces.village.service.OssService;
import com.ces.village.service.UsersService;
import com.ces.village.service.WxMsgService;
import com.ces.village.utils.StringUtils;
import com.ces.village.utils.WxApiUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信消息推送服务
 */
@Service
@Log4j2
public class WxMsgServiceImpl implements WxMsgService {
    @Autowired
    private WxPushProperties wxPushProperties;
    @Autowired
    private WxApiUtil wxApiUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private OssService ossService;
    @Autowired
    private UsersService usersService;


    /**
     * 推送微信消息给用户
     *
     * @param openid
     * @param templateId
     * @return
     */
    @Override
    public void msgPush(String openid, String templateId, String data) throws Exception {
        WxMsgPushResponse response = wxApiUtil.msgPush(openid, templateId, data, null);
        if (response.getErrCode() != 0) {
            throw new Exception(String.format("原因=%s, openid=%s, templateId=%s, data=%s", response.getErrMsg(), openid, templateId, data));
        }
    }

    /**
     * 退款通知
     */
    @Override
    public void refundPush(Refund refund) {
        try {
            String openid = usersService.userId2OpenId(refund.getUserId());
            // 构建订阅消息内容的JSON对象
            JSONObject messageData = new JSONObject();
            messageData.put("character_string1", dataItem("订单编号", refund.getOrderNo().toString()));
            messageData.put("thing2", dataItem("订单内容", StringUtils.truncate(refund.getName(), 20)));
            messageData.put("amount3", dataItem("订单金额", refund.getRefundAmount().toPlainString()));
            String status = RefundStatusConstant.toString(refund.getStatus());
            status = StringUtils.truncate(status, 5);
            messageData.put("phrase8", dataItem("退款状态", status));
            messageData.put("thing4", dataItem("退款原因", StringUtils.truncate(refund.getReason(), 20)));
            // 将订阅消息内容转换为JSON字符串
            String jsonData = messageData.toJSONString();
            msgPush(openid, wxPushProperties.getMsgRefundId(), jsonData);
        } catch (Exception e) {
            log.error("消息推送失败:{}", e.getMessage());
        }
    }

    /**
     * 订单发货提醒
     * orders要包含orderDetails
     */
    @Override
    public void sendOutGoodsPush(Orders orders) {
        try {
            String openid = usersService.userId2OpenId(orders.getUserId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<OrderDetail> list = orders.getOrderDetails();
            StringBuilder sb = new StringBuilder();
            for (OrderDetail detail : list) {
                sb.append(detail.getName());
                sb.append("；");
            }
            // 构建订阅消息内容的JSON对象
            JSONObject messageData = new JSONObject();
            messageData.put("thing2", dataItem("物品名称", StringUtils.truncate(sb.toString(), 20)));
            messageData.put("character_string6", dataItem("快递单号", orders.getTrackNumber()));
            messageData.put("time1", dataItem("发货时间", orders.getSendTime().format(formatter)));
            // 将订阅消息内容转换为JSON字符串
            String jsonData = messageData.toJSONString();
            msgPush(openid, wxPushProperties.getMsgSendOutGoodsId(), jsonData);
        } catch (Exception e) {
            log.error("消息推送失败:{}", e.getMessage());
        }
    }


    /**
     * 评论删除提醒
     */
    @Override
    public void commentDeletionPush(Comments comments) {
        try {
            String openid = usersService.userId2OpenId(comments.getUserId());
            // 构建订阅消息内容的JSON对象
            JSONObject messageData = new JSONObject();
            messageData.put("thing1", dataItem("评论内容", StringUtils.truncate(comments.getComment(), 20)));
            messageData.put("thing2", dataItem("删除原因", "评论内容违规"));
            // 将订阅消息内容转换为JSON字符串
            String jsonData = messageData.toJSONString();
            msgPush(openid, wxPushProperties.getMsgCommentDeletionId(), jsonData);
        } catch (Exception e) {
            log.error("消息推送失败:{}", e.getMessage());
        }
    }

    /**
     * 审核结果通知
     */
    @Override
    public void reviewPush(ReviewPushDTO data) {
        try {
            String openid = usersService.userId2OpenId(data.getUserId());
            // 构建订阅消息内容的JSON对象
            JSONObject messageData = new JSONObject();
            messageData.put("phrase1", dataItem("审核结果", StringUtils.truncate(data.getResult(), 4)));
            messageData.put("thing4", dataItem("审核内容", StringUtils.truncate(data.getContent(), 20)));
            messageData.put("thing3", dataItem("备注", StringUtils.truncate(data.getNotes(), 20)));
            // 将订阅消息内容转换为JSON字符串
            String jsonData = messageData.toJSONString();
            msgPush(openid, wxPushProperties.getMsgReviewId(), jsonData);
        } catch (Exception e) {
            log.error("消息推送失败:{}", e.getMessage());
        }
    }

    /**
     * 支付成功通知
     * orders要包含orderDetails
     */
    @Override
    public void paymentSuccessPush(Orders orders) {
        try {
            String openid = usersService.userId2OpenId(orders.getUserId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<OrderDetail> list = orders.getOrderDetails();
            StringBuilder sb = new StringBuilder();
            for (OrderDetail detail : list) {
                sb.append(detail.getName());
                sb.append("；");
            }
            // 构建订阅消息内容的JSON对象
            JSONObject messageData = new JSONObject();
            messageData.put("thing6", dataItem("商品名称", StringUtils.truncate(sb.toString(), 20)));
            messageData.put("amount7", dataItem("支付金额", orders.getActualAmount().toPlainString()));
            messageData.put("date8", dataItem("下单时间", orders.getOrderTime().format(formatter)));
            messageData.put("character_string9", dataItem("订单号", orders.getOrderNo().toString()));
            // 将订阅消息内容转换为JSON字符串
            String jsonData = messageData.toJSONString();
            msgPush(openid, wxPushProperties.getMsgPaymentSuccessId(), jsonData);
        } catch (Exception e) {
            log.error("消息推送失败:{}", e.getMessage());
        }
    }


    /**
     * 校验微信服务器的签名
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        // 按字典序排序参数
        String[] params = {wxPushProperties.getMsgPostToken(), timestamp, nonce};
        Arrays.sort(params);

        // 拼接成一个字符串
        StringBuilder contentBuilder = new StringBuilder();
        for (String param : params) {
            contentBuilder.append(param);
        }

        // 使用SHA1进行加密
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] bytes = contentBuilder.toString().getBytes(StandardCharsets.UTF_8);
            byte[] sha1Bytes = md.digest(bytes);

            // 转换为十六进制字符串并与请求中的signature对比
            StringBuilder hexString = new StringBuilder();
            for (byte b : sha1Bytes) {
                String hex = Integer.toHexString(b & 0xFF);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString().equals(signature);
        } catch (Exception e) {
            throw new RuntimeException("校验微信服务器的签名失败", e);
        }
    }


    /**
     * 媒体检测接口 回调函数
     *
     * @param pushDTO
     */
    @Override
    public void mediaCheckCallBack(WxMediaCheckPushResponse pushDTO) {
        log.info("媒体检测接口返回的参数：" + JSON.toJSON(pushDTO));
        String key = pushDTO.getTrace_id();
        if (redisTemplate.hasKey(key)) {
            String imgUrl = (String) redisTemplate.opsForValue().get(key);
            redisTemplate.delete(key);
            // 解析返回参数
            WxMediaCheckPushResponse.Result result = pushDTO.getResult();
            if (!result.getSuggest().equals("pass")) {
                // 不通过
                log.info("图片：" + imgUrl + "审核不通过,suggest=" + result.getSuggest() + ",Label=" + result.getLabel());
                // 从oss删除图片
                ossService.deleteAsync(imgUrl);
            }
        }
    }


    /**
     * 创建 微信消息推送参数 的数据项
     *
     * @param name
     * @param value
     * @return
     */
    private static Map<String, Object> dataItem(String name, String value) {
        Map<String, Object> item = new HashMap<>();
        item.put("value", value);
        return item;
    }

    /**
     * 查询rid对应的错误消息
     * 微信消息推送失败会返回rid
     * 通过rid查询失败原因
     *
     * @param rid
     * @return
     */
    public String queryRid(String rid) {
        return wxApiUtil.queryRid(rid);
    }
}
