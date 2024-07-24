package com.ces.Village.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.aliyun.green20220302.models.ImageModerationResponse;
import com.aliyun.green20220302.models.ImageModerationResponseBody;
import com.aliyun.green20220302.models.TextModerationResponse;
import com.ces.Village.constant.AliReviewTypeConstant;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.exception.CustomException;
import com.ces.Village.pojo.dto.WxMediaCheckResponse;
import com.ces.Village.pojo.dto.WxMsgCheckResponse;
import com.ces.Village.service.ContentReviewService;
import com.ces.Village.utils.AliSecCheckUtil;
import com.ces.Village.utils.StringUtils;
import com.ces.Village.utils.WxApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

/**
 * 内容审核服务
 * TODO 前端审核文本；后端审核图片，不需要审核文本
 *
 */
@Service
@RequiredArgsConstructor
@Log4j2
public class ContentReviewServiceImpl implements ContentReviewService {
    private final AliSecCheckUtil aliSecCheckUtil;
    private final WxApiUtil wxApiUtil;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 是否为违规文本
     *
     * @param text
     * @return
     */
    public boolean isAbnormalText(String text) {
        String result = aliTextReview(text);
        // 违规文本返回违规结果，正常文本，返回""
        return !StringUtils.isEmptyOrNull(result);
    }

    /**
     * 阿里云 文本审核
     *
     * @return 违规文本返回违规结果，如：辱骂。如果是正常文本，返回""
     */
    @Override
    public String aliTextReview(String text) {
        try {
            if (StringUtils.isEmptyOrNull(text)) {
                throw new CustomException(ErrorCodeEnum.PARAM_IS_NULL);
            }
            TextModerationResponse response = aliSecCheckUtil.msgSecCheck(text, AliReviewTypeConstant.COMMENT_DETECTION);
            if (response.getBody().getCode() != 200) {
                throw new Exception("text=" + text + "，errMsg=" + response.getBody().getMessage());
            }
            String reason = response.getBody().getData().getReason();
//            HashMap<String, Object> map = new HashMap<>();
//            String key = "risk_tips";
//            map.put(key, "");
            if (!StringUtils.isEmptyOrNull(reason)) {
                // 解析参数
                reason = JSONObject.parse(reason).get("riskTips").toString();
                StringBuilder sb = new StringBuilder();
                String[] reasons = reason.split(",");
                for (int i = 0; i < reasons.length; i++) {
                    String item = reasons[i];
                    item = item.split("_")[0];
                    if (sb.indexOf(item) != -1) {
                        // 去重
                        if (i != 0) {
                            sb.append(",");
                        }
                        sb.append(item);
                    }
                }
                reason = sb.toString();
            }
            return reason;
        } catch (Exception e) {
            log.error("文本检测接口出错：" + e.getMessage());
            throw new CustomException(ErrorCodeEnum.CONTENT_REVIEW_FAILED);
        }
    }

    /**
     * 阿里云 图片审核
     */
    @Override
    public List<ImageModerationResponseBody.ImageModerationResponseBodyDataResult> aliImgReview(String imgUrl) {
        try {
            if (StringUtils.isEmptyOrNull(imgUrl)) {
                throw new CustomException(ErrorCodeEnum.PARAM_IS_NULL);
            }
            ImageModerationResponse response = aliSecCheckUtil.imgSecCheck(imgUrl);
            if (response.getBody().getCode() != 200) {
                throw new Exception("imgUrl=" + imgUrl + "，errMsg=" + response.getBody().msg);
            }
            List<ImageModerationResponseBody.ImageModerationResponseBodyDataResult> resultList = response.getBody().getData().getResult();
//            HashMap<String, Object> map = new HashMap<>();
//            String key = "result";
//            map.put(key, resultList);
            return resultList;
        } catch (Exception e) {
            log.error("图片审核接口出错：" + e.getMessage());
            throw new CustomException(ErrorCodeEnum.CONTENT_REVIEW_FAILED);
        }
    }

    /**
     * 微信 文本审核
     * 不支持管理员调用，管理员无openid
     * @return
     */
    @Override
    public WxMsgCheckResponse wxTextReview(String text, String openId) {
        try {
            if (StringUtils.isEmptyOrNull(text) || StringUtils.isEmptyOrNull(openId)) {
                throw new CustomException(ErrorCodeEnum.PARAM_IS_NULL);
            }
            return wxApiUtil.msgSecCheck(text, openId);
        } catch (Exception e) {
            log.error("文本检测接口出错：" + e.getMessage());
            throw new CustomException(ErrorCodeEnum.CONTENT_REVIEW_FAILED);
        }
    }


    /**
     * 微信 图片审核
     * 不支持管理员调用，管理员无openid
     * @return
     */
    @Override
    public WxMediaCheckResponse wxImgReviewAsync(String imgUrl, String openId) {
        try {
            if (StringUtils.isEmptyOrNull(imgUrl) || StringUtils.isEmptyOrNull(openId)) {
                throw new CustomException(ErrorCodeEnum.PARAM_IS_NULL);
            }
            WxMediaCheckResponse wxMediaCheckResponse = wxApiUtil.ImageCheckAsync(imgUrl, openId);
            // 将trace_id和图片url保存到redis
            String key = wxMediaCheckResponse.getTrace_id();
            redisTemplate.opsForValue().set(key, imgUrl, Duration.ofMinutes(40));
            return wxMediaCheckResponse;
        } catch (Exception e) {
            log.error("图片检测接口出错：" + e.getMessage());
            throw new CustomException(ErrorCodeEnum.CONTENT_REVIEW_FAILED);
        }
    }
}
