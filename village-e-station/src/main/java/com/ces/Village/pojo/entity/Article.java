package com.ces.Village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 文章表
 * &#064;TableName  article
 */
//@Accessors(chain = true)
@ApiModel(description = "文章表")
@TableName(value = "article")
@Data
@Accessors(chain = true)
public class Article implements Serializable {
    /**
     * 文章id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    @JsonProperty("article_id")
    private Long id;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题", position = 2)
    @JsonProperty("title")
    private String title;

    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容", position = 3)
    @JsonProperty("content")
    private String content;

    /**
     * 文章分类1、乡村振兴2、特产介绍3、农业技术4、问题答疑
     */
    @ApiModelProperty(value = "文章分类1、乡村振兴2、特产介绍3、农业技术4、问题答疑", position = 4)
    @JsonProperty("article_type")
    private Integer articleType;

    /**
     * 作者用户名
     */
    @ApiModelProperty(value = "作者用户名", position = 5)
    @JsonProperty("author")
    private String author;

    /**
     * 作者的用户id
     */
    @ApiModelProperty(value = "作者的用户id", position = 6)
    @JsonIgnore
    private Long authorId;

    /**
     * 文章创建时间
     */
    @ApiModelProperty(value = "文章创建时间", position = 7)
    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    /**
     * 文章更新时间
     */
    @ApiModelProperty(value = "文章更新时间", position = 8)
    @JsonIgnore
    private LocalDateTime dateUpdated;

    /**
     * 阅读量
     */
    @ApiModelProperty(value = "阅读量", position = 9)
    @JsonProperty("read_number")
    private Integer readNumber;

    /**
     * 评论数量
     */
    @ApiModelProperty(value = "评论数量", position = 10)
    @JsonProperty("comment_number")
    private Integer commentNumber;

    /**
     * 文章略缩图链接
     */
    @ApiModelProperty(value = "文章略缩图链接", position = 11)
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
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}