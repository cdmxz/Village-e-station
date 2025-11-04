package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 文章表
 * &#064;TableName  article
 */
//@Accessors(chain = true)
@Schema(description = "文章表")
@TableName(value = "article")
@Data
@Accessors(chain = true)
public class Article implements Serializable {
    /**
     * 文章id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(name = "主键")
    @JsonProperty("article_id")
    private Long id;

    /**
     * 文章标题
     */
    @Schema(name = "文章标题")
    @JsonProperty("title")
    private String title;

    /**
     * 文章内容
     */
    @Schema(name = "文章内容")
    @JsonProperty("content")
    private String content;

    /**
     * 文章分类1、乡村振兴2、特产介绍3、农业技术4、问题答疑
     */
    @Schema(name = "文章分类1、乡村振兴2、特产介绍3、农业技术4、问题答疑")
    @JsonProperty("article_type")
    private Integer articleType;

    /**
     * 作者用户名
     */
    @Schema(name = "作者用户名")
    @JsonProperty("author")
    private String author;

    /**
     * 作者的用户id
     */
    @Schema(name = "作者的用户id")
    @JsonIgnore
    private Long authorId;

    /**
     * 文章创建时间
     */
    @Schema(name = "文章创建时间")
    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    /**
     * 文章更新时间
     */
    @Schema(name = "文章更新时间")
    @JsonIgnore
    private LocalDateTime dateUpdated;

    /**
     * 阅读量
     */
    @Schema(name = "阅读量")
    @JsonProperty("read_number")
    private Integer readNumber;

    /**
     * 评论数量
     */
    @Schema(name = "评论数量")
    @JsonProperty("comment_number")
    private Integer commentNumber;

    /**
     * 文章略缩图链接
     */
    @Schema(name = "文章略缩图链接")
    @JsonIgnore
    private String thumbnailUrl;

    /**
     * 文章简介
     */
    @JsonProperty("synopsis")
    private String synopsis;

    @TableField(exist = false)
    @JsonProperty("is_my_publish")
    private Integer isMyPublish;

    @Serial
    @Schema(hidden = true)
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}