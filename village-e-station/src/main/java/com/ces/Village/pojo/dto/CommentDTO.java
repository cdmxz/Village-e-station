package com.ces.Village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel(description = "发布评论时传递的数据模型")
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
