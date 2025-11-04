package com.ces.village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@Schema(description = "评论列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentsVo {

    @JsonProperty("comment_id")
    private Long id;

    @JsonProperty("user")
    private String user;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("img_urls")
    private List<String> imgUrls;

    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    @JsonProperty("is_my_publish")
    private Integer isMyPublish;

    @JsonProperty("status")
    private Integer status;
}
