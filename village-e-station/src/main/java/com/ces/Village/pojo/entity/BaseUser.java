package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@Accessors(chain = true)
public class BaseUser implements Serializable {
    @Serial
    @Schema(hidden = true)
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

     @Schema(name = "主键 ")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

     @Schema(name = "姓名")
    @TableField("name")
    @JsonProperty("name")
    private String name;

     @Schema(name = "昵称")
    @TableField("nick_name")
    @JsonProperty("nick_name")
    private String nickName;

     @Schema(name = "手机号")
    @TableField("phone")
    @JsonProperty("phone")
    private String phone;

     @Schema(name = "头像的url地址")
    @TableField("avatar_url")
    @JsonProperty("avatar_url")
    private String avatarUrl;

     @Schema(name = "乡村地址")
    @TableField("village")
    @JsonProperty("village")
    private String village;

     @Schema(name = "外键：用户默认地址id")
    @TableField("address_default_id")
    @JsonIgnore
    private Long addressDefaultId;

     @Schema(name = "创建时间")
    @TableField("create_time")
    @JsonIgnore
    private LocalDateTime createTime;

}
