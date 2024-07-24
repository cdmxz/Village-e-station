package com.ces.Village.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serial;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 企业招聘信息表
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("enterprise_hires")
@ApiModel(value = "EnterpriseHires对象", description = "企业招聘信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnterpriseHires implements Serializable {
    @Serial
    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "企业招聘信息id")
    @JsonProperty(value = "information_id")
    private Long id;

    @ApiModelProperty(value = "外键：企业信息id")
    @JsonIgnore
    private Long enterpriseInformationId;

    @ApiModelProperty(value = "标题")
    @JsonProperty("title")
    private String title;

    @ApiModelProperty(value = "工作岗位")
    @JsonProperty(value = "work")
    private String work;

    @ApiModelProperty(value = "薪水：6000-8000元/月")
    @JsonProperty(value = "salary")
    private String salary;

    @ApiModelProperty(value = "招聘人数")
    @JsonProperty(value = "recruits_number")
    private Integer recruitsNumber;

    @ApiModelProperty(value = "联系人")
    @JsonProperty(value = "contact_person")
    private String contactPerson;

    @ApiModelProperty(value = "联系方式")
    @JsonProperty(value = "contact_information")
    private String contactInformation;

    @ApiModelProperty(value = "工作地址")
    @JsonProperty(value = "work_address")
    private String workAddress;

    @ApiModelProperty(value = "发布时间")
    @JsonProperty(value = "publish_time")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "截止招聘时间")
    @JsonProperty(value = "deadline")
    private LocalDateTime deadline;

    @ApiModelProperty(value = "工作详情")
    @JsonProperty(value = "job_details")
    private String jobDetails;

    @ApiModelProperty(value = "状态：1、待审核2、审核成功，展示中3、审核失败，已打回4、到达截止时间，已下架")
    @JsonProperty(value = "status")
    private Integer status;

    @ApiModelProperty(value = "用户id")
    @JsonProperty(value = "user_id")
    private Long userId;

    /**
     * 外键：企业信息
     */
    @JsonProperty("enterprise_information")
    @TableField(exist = false)
    private EnterpriseInformation enterpriseInformation;

    /**
     * 外键：审核失败理由
     */
    @JsonProperty("reason")
    @TableField(exist = false)
    @ApiModelProperty(value = "审核失败理由")
    private String reason;
}
