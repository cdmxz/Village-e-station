package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 地址管理
 * </p>
 *
 * @author author
 * @since 2023-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user_address")
@Schema(name = "UserAddress对象", description = "地址管理")
public class UserAddress implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

     @Schema(name = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonProperty("address_id")
    private Long id;

     @Schema(name = "用户openid")
    @TableField("user_id")
    @JsonProperty("user_id")
    private Long userId;

     @Schema(name = "收货人")
    @TableField("consignee")
    private String consignee;

     @Schema(name = "手机号")
    @TableField("phone")
    private String phone;

     @Schema(name = "省级名称")
    @TableField("province")
    private String province;

     @Schema(name = "市级名称")
    @TableField("city")
    private String city;

     @Schema(name = "区级名称")
    @TableField("district")
    private String district;

     @Schema(name = "详细地址")
    @TableField("detail")
    private String detail;

     @Schema(name = "默认 0 否 1是")
    @TableField("is_default")
    @JsonProperty("is_default")
    private Integer isDefault;

     @Schema(name = "是否删除")
    @TableField("is_deleted")
    @JsonProperty("is_deleted")
    private Integer isDeleted;

    /**
     * 拼接地址
     * @return
     */
    public String combination() {
        return consignee + "," + phone + "," + province + city + district + detail;
    }
}
