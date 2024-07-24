package com.ces.Village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 内容审核接口DTO
 */
@Data
public class ContentReviewDTO {

    /**
     * 文本
     */
    @JsonProperty("text")
    String text;

    /**
     * 图片链接
     */
    @JsonProperty("img_url")
    String imgUrl;

    /**
     * 审核类型 具体看AliReviewTypeConstant类
     */
    @JsonProperty("type")
    String type;
}
