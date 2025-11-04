package com.ces.village.service;


import com.aliyun.green20220302.models.ImageModerationResponseBody;
import com.ces.village.pojo.dto.WxMediaCheckResponse;
import com.ces.village.pojo.dto.WxMsgCheckResponse;

import java.util.List;

/**
 * 内容审核服务
 */
public interface ContentReviewService {

    /**
     * 是否为违规文本
     *
     * @param text
     * @return
     */
    boolean isAbnormalText(String text);

    /**
     * 阿里云 文本审核
     *
     * @return 违规文本返回违规结果，如：辱骂。如果是正常文本，返回""
     */
    String aliTextReview(String text);

    /**
     * 阿里云 图片审核
     */
    List<ImageModerationResponseBody.ImageModerationResponseBodyDataResult> aliImgReview(String imgUrl);

    WxMsgCheckResponse wxTextReview(String text, String userId);

    WxMediaCheckResponse wxImgReviewAsync(String imgUrl, String userId);
}
