package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 服务协议表
 */
@Data
@TableName("service_protocol")
@Schema(name="ServiceProtocol", description="服务协议表")
public class ServiceProtocol implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonIgnore
     @Schema(name = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @JsonProperty(value = "title")
    @TableField(exist = false)
     @Schema(name = "标题")
    private String title;

    @JsonProperty("service")
    @TableField("service")
     @Schema(name = "服务协议")
    private String service;
}
