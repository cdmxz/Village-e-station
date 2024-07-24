package com.ces.Village.pojo.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 问题评论表
 * @TableName comments
 */
@ApiModel(description = "问题评论表")
@TableName(value ="comments")
@Data
@Accessors(chain = true)
public class Comments implements Serializable {
    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "评论id", position = 1)
    @JsonProperty("comment_id")
    private Long id;

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id", position = 2)
    @JsonProperty("article_id")
    private Long articleId;

    /**
     * 发布评论的用户的id
     */
    @ApiModelProperty(value = "发布评论的用户的id", position = 3)
    @JsonProperty("user_id")
    private Long userId;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容", position = 4)
    private String comment;

    /**
     * 评论图片
     */
    @ApiModelProperty(value = "评论图片", position = 5)
    @JsonProperty("img_urls")
    private String imgUrls;

    /**
     * 评论创建时间
     */
    @ApiModelProperty(value = "评论创建时间", position = 6)
    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    /**
     * 1删除
     * 0未删除
     */
    @ApiModelProperty(value = "1删除、0未删除", position = 7)
    @JsonProperty("is_deleted")
    private Integer isDeleted;
    /**
     * 评论状态
     */
    @ApiModelProperty(value = "1待审核、2审核成功、3审核失败",position = 8)
    @JsonProperty("status")
    private Integer status;

    @TableField(exist = false)
    private Users user;      // 一对一映射 添加User模型
    @TableField(exist = false)
    private Article article;// 一对一映射 添加Article模型

    @Serial
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}