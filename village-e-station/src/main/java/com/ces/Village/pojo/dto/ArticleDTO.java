package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "发布或修改文章时传递的数据模型")
public class ArticleDTO {

    @JsonProperty("subtitle")
    private String synopsis;

    @JsonProperty("article_id")
    private String id;

    @NotBlank(message = "文章标题不能为空")
    @JsonProperty("title")
    private String title;

    @NotBlank(message = "文章内容不能为空")
    @JsonProperty("content")
    private String content;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @NotNull(message = "文章类型不能为空")
    @JsonProperty("article_type")
    private Integer articleType;

}
