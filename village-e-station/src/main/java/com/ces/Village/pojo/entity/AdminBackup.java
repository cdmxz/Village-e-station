package com.ces.Village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 管理员信息表
 *
 * 此类暂时作废，但暂时保留
 *
 * </p>
 *
 * @author author
 * @since 2023-11-11
 */
@Data
@Accessors(chain = true)
@TableName("admin")
@ApiModel(value="Admin对象", description="管理员信息")
public class AdminBackup implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "姓名")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "昵称")
    @TableField("nick_name")
    private String nickName;

    @ApiModelProperty(value = "密码")
    @TableField("password")
    private String password;

    @ApiModelProperty(value = "手机号")
    @TableField("phone")
    private String phone;

    @ApiModelProperty(value = "头像的url地址")
    @TableField("avatar_url")
    private String avatarUrl;

    @ApiModelProperty(value = "乡村地址")
    @TableField("village")
    private String village;

    @ApiModelProperty(value = "用户默认地址id")
    @TableField("address_default_id")
    private Long addressDefaultId;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
