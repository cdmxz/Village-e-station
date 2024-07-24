package com.ces.Village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "待审核评论列表")
@Accessors(chain = true)
public class CommentsStatusVo {
    @JsonProperty("comment_id")
    private Long id;

    @JsonProperty("user")
    private String user;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    @JsonProperty("article_title")
    private String articleTitle;

    @JsonProperty("article_id")
    private String articleId;

    @JsonProperty("img_urls")
    private List<String> imgUrls;
}
