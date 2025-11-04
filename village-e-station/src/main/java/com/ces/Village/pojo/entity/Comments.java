package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问题评论表
 *
 * @TableName comments
 */
@Schema(description = "问题评论表")
@TableName(value = "comments")
@Data
@Accessors(chain = true)
public class Comments implements Serializable {
    /**
     * 评论id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(name = "评论id")
    @JsonProperty("comment_id")
    private Long id;

    /**
     * 文章id
     */
    @Schema(name = "文章id")
    @JsonProperty("article_id")
    private Long articleId;

    /**
     * 发布评论的用户的id
     */
    @Schema(name = "发布评论的用户的id")
    @JsonProperty("user_id")
    private Long userId;

    /**
     * 评论内容
     */
    @Schema(name = "评论内容")
    private String comment;

    /**
     * 评论图片
     */
    @Schema(name = "评论图片")
    @JsonProperty("img_urls")
    private String imgUrls;

    /**
     * 评论创建时间
     */
    @Schema(name = "评论创建时间")
    @JsonProperty("date_created")
    private LocalDateTime dateCreated;

    /**
     * 1删除
     * 0未删除
     */
    @Schema(name = "1删除、0未删除")
    @JsonProperty("is_deleted")
    private Integer isDeleted;
    /**
     * 评论状态
     */
    @Schema(name = "1待审核、2审核成功、3审核失败")
    @JsonProperty("status")
    private Integer status;

    @TableField(exist = false)
    private Users user;      // 一对一映射 添加User模型
    @TableField(exist = false)
    private Article article;// 一对一映射 添加Article模型

    @Serial
    @Schema(hidden = true)
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}