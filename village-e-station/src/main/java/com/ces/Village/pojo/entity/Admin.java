package com.ces.Village.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 管理员和用户使用同一张表（Users）
 *
 * users表中，管理员的password不为空
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("users")
@ApiModel(value = "UserList对象", description = "用户信息")
public class Admin extends BaseUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户密码（管理员使用）")
    @TableField("password")
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "微信用户OPENID")
    @TableField("openid")
    @JsonIgnore
    private String openId;

    @ApiModelProperty(value = "外键：用户默认地址")
    @TableField(exist = false)
    @JsonIgnore
    private UserAddress address;
}
