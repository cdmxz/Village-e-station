package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@Schema(name = "EnterpriseHires对象", description = "企业招聘信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnterpriseHires implements Serializable {
    @Serial
    @Schema(hidden = true)
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(name = "企业招聘信息id")
    @JsonProperty(value = "information_id")
    private Long id;

    @Schema(name = "外键：企业信息id")
    @JsonIgnore
    private Long enterpriseInformationId;

    @Schema(name = "标题")
    @JsonProperty("title")
    private String title;

    @Schema(name = "工作岗位")
    @JsonProperty(value = "work")
    private String work;

    @Schema(name = "薪水：6000-8000元/月")
    @JsonProperty(value = "salary")
    private String salary;

    @Schema(name = "招聘人数")
    @JsonProperty(value = "recruits_number")
    private Integer recruitsNumber;

    @Schema(name = "联系人")
    @JsonProperty(value = "contact_person")
    private String contactPerson;

    @Schema(name = "联系方式")
    @JsonProperty(value = "contact_information")
    private String contactInformation;

    @Schema(name = "工作地址")
    @JsonProperty(value = "work_address")
    private String workAddress;

    @Schema(name = "发布时间")
    @JsonProperty(value = "publish_time")
    private LocalDateTime publishTime;

    @Schema(name = "截止招聘时间")
    @JsonProperty(value = "deadline")
    private LocalDateTime deadline;

    @Schema(name = "工作详情")
    @JsonProperty(value = "job_details")
    private String jobDetails;

    @Schema(name = "状态：1、待审核2、审核成功，展示中3、审核失败，已打回4、到达截止时间，已下架")
    @JsonProperty(value = "status")
    private Integer status;

    @Schema(name = "用户id")
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
    @Schema(name = "审核失败理由")
    private String reason;
}
