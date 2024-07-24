package com.ces.Village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(description = "我发布的评论列表")
public class MyCommentsVo {
    @JsonProperty("comment_id")
    private Long id;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("comment_date_created")
    private LocalDateTime commentDateCreated;

//    @JsonProperty("article_date_created")
//    private LocalDateTime articleDateCreated;

    @JsonProperty("article_title")
    private String articleTitle;

    @JsonProperty("article_id")
    private String articleId;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("img_urls")
    private List<String> imgUrls;
}
