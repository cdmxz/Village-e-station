package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 用户信息
 * 管理员和用户使用同一张表（Users）
 *
 * users表中，用户的openid不为空，但是password为空
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("users")
@Schema(name = "UserList对象", description = "用户信息")
public class Users extends BaseUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

     @Schema(name = "微信用户OPENID")
    @TableField("openid")
    @JsonIgnore
    private String openId;

     @Schema(name = "身份证号")
    @TableField("id_number")
    @JsonProperty("id_number")
    private String idNumber;

     @Schema(name = "外键：用户默认地址")
    @TableField(exist = false)
    @JsonIgnore
    private UserAddress address;
}
