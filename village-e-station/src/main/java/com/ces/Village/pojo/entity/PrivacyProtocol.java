package com.ces.Village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 隐私协议表
 */
@Data
@TableName("privacy_protocol")
@ApiModel(value="PrivacyProtocol", description="隐私协议表")
public class PrivacyProtocol implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonProperty(value = "title")
    @TableField(exist = false)
    @ApiModelProperty(value = "标题")
    private String title;

    @JsonProperty("privacy")
    @TableField("privacy")
    @ApiModelProperty(value = "隐私协议")
    private String privacy;
}
