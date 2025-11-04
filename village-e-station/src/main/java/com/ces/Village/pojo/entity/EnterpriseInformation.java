package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

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
@Schema(name="EnterpriseInformation对象", description="企业信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnterpriseInformation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
     @Schema(name = "企业信息id")
    @JsonProperty(value = "information_id")
    private Long id;

     @Schema(name = "企业名称")
    @JsonProperty(value = "name")
    private String name;

     @Schema(name = "信用代码")
    @JsonProperty(value = "credit_code")
    private String creditCode;

     @Schema(name = "成立时间")
    @JsonProperty(value = "established_time")
    private LocalDate establishedTime;

     @Schema(name = "地址")
    @JsonProperty(value = "address")
    private String address;

     @Schema(name = "营业执照url")
    @JsonProperty(value = "business_license_url")
    private String businessLicenseUrl;

     @Schema(name = "授权书url")
    @JsonProperty(value = "authorization_letter_url")
    private String authorizationLetterUrl;

     @Schema(name = "企业照片url")
    @JsonProperty(value = "photo_url")
    private String photoUrl;

     @Schema(name = "当前用户id")
    @JsonProperty(value = "user_id")
    private Long userId;

}
