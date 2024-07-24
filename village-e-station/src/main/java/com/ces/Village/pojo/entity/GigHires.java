package com.ces.Village.pojo.entity;

import java.io.Serial;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
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
 * 零工招聘信息表
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gig_hires")
@ApiModel(value = "GigHires对象", description = "零工招聘信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GigHires implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonProperty("information_id")
    private Long id;

    @ApiModelProperty(value = "标题")
    @JsonProperty("title")
    private String title;

    @ApiModelProperty(value = "工作岗位")
    @JsonProperty("work")
    private String work;

    @ApiModelProperty(value = "招聘人数")
    @JsonProperty("recruits_number")
    private Integer recruitsNumber;

    @ApiModelProperty(value = "薪酬")
    @JsonProperty("salary")
    private String salary;

    @ApiModelProperty(value = "联系人")
    @JsonProperty("contact_person")
    private String contactPerson;

    @ApiModelProperty(value = "联系方式，手机邮箱")
    @JsonProperty("contact_information")
    private String contactInformation;

    @ApiModelProperty(value = "工作地")
    @JsonProperty("work_address")
    private String workAddress;

    @ApiModelProperty(value = "工作开始时间")
    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "工作结束时间")
    @JsonProperty("end_time")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "招聘发布时间")
    @JsonProperty("publish_time")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "招聘截止（下架）时间")
    @JsonProperty("deadline")
    private LocalDateTime deadline;

    @ApiModelProperty(value = "工作详情")
    @JsonProperty("job_details")
    private String jobDetails;

    @ApiModelProperty(value = "状态：1、待审核2、审核成功，展示中3、审核失败，已打回4、到达截止时间，已下架")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "用户id")
    @JsonIgnore
    private Long userId;

    /**
     * 外键：审核失败理由
     */
    @JsonProperty("reason")
    @TableField(exist = false)
    @ApiModelProperty(value = "审核失败理由")
    private String reason;
}
