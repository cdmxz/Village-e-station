package com.ces.Village.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serial;
import java.time.LocalDate;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 企业信息表
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("enterprise_information")
@ApiModel(value="EnterpriseInformation对象", description="企业信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnterpriseInformation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "企业信息id")
    @JsonProperty(value = "information_id")
    private Long id;

    @ApiModelProperty(value = "企业名称")
    @JsonProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "信用代码")
    @JsonProperty(value = "credit_code")
    private String creditCode;

    @ApiModelProperty(value = "成立时间")
    @JsonProperty(value = "established_time")
    private LocalDate establishedTime;

    @ApiModelProperty(value = "地址")
    @JsonProperty(value = "address")
    private String address;

    @ApiModelProperty(value = "营业执照url")
    @JsonProperty(value = "business_license_url")
    private String businessLicenseUrl;

    @ApiModelProperty(value = "授权书url")
    @JsonProperty(value = "authorization_letter_url")
    private String authorizationLetterUrl;

    @ApiModelProperty(value = "企业照片url")
    @JsonProperty(value = "photo_url")
    private String photoUrl;

    @ApiModelProperty(value = "当前用户id")
    @JsonProperty(value = "user_id")
    private Long userId;

}
