package com.ces.village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
 
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Schema(description = "文章列表")
public class ArticleVo {
    @JsonProperty("article_id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("author")
    private String author;

    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    @JsonProperty("read_number")
    private Integer readNumber;

    @JsonProperty("article_type")
    private Integer articleType;

    @JsonProperty("subtitle")
    private String synopsis;

    @JsonProperty("is_my_publish")
    private Integer isMyPublish;

    /**
     * 作者的用户id
     */
     @Schema(name  = "作者的用户id" )
    @JsonIgnore
    private Long authorId;
}
