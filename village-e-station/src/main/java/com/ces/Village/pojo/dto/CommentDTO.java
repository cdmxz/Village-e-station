package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "发布评论时传递的数据模型")
public class CommentDTO {
    @JsonProperty("user_token")
    private String userToken;

    @NotNull(message = "文章id不能为空")
    @JsonProperty("article_id")
    private Long articleId;

    @NotBlank(message = "评论内容不能为空")
    @JsonProperty("comment")
    private String comment;

    @JsonProperty("img_urls")
    private List<String> imgUrls;
}
