package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name="Admin对象", description="管理员信息")
public class AdminBackup implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

     @Schema(name = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

     @Schema(name = "姓名")
    @TableField("name")
    private String name;

     @Schema(name = "昵称")
    @TableField("nick_name")
    private String nickName;

     @Schema(name = "密码")
    @TableField("password")
    private String password;

     @Schema(name = "手机号")
    @TableField("phone")
    private String phone;

     @Schema(name = "头像的url地址")
    @TableField("avatar_url")
    private String avatarUrl;

     @Schema(name = "乡村地址")
    @TableField("village")
    private String village;

     @Schema(name = "用户默认地址id")
    @TableField("address_default_id")
    private Long addressDefaultId;

     @Schema(name = "创建时间")
    @TableField("create_time")
    private LocalDateTime createTime;


}
