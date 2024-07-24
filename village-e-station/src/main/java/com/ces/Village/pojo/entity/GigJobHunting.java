package com.ces.Village.pojo.entity;



import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
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
 * 零工求职信息表
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gig_job_hunting")
@ApiModel(value = "GigJobHunting对象", description = "零工求职信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GigJobHunting implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonProperty("information_id")
    private Long id;

    @ApiModelProperty(value = "标题")
    @JsonProperty("title")
    private String title;

    @ApiModelProperty(value = "期望工作")
    @JsonProperty("expected_work")
    private String expectedWork;

    @ApiModelProperty(value = "期望薪酬")
    @JsonProperty("expected_salary")
    private String expectedSalary;

    @ApiModelProperty(value = "求职者")
    @JsonProperty("contact_person")
    private String contactPerson;

    @ApiModelProperty(value = "联系方式，手机或邮箱")
    @JsonProperty("contact_information")
    private String contactInformation;

    @ApiModelProperty(value = "工作地址")
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

    @ApiModelProperty(value = "招聘下架时间")
    @JsonProperty("deadline")
    private LocalDateTime deadline;

    @ApiModelProperty(value = "状态：1、待审核2、审核成功，展示中3、审核失败，已打回4、到达截止时间，已下架")
    @JsonProperty("status")
    private Integer status;

    @JsonIgnore
    @ApiModelProperty(value = "用户id")
    private Long userId;

    /**
     * 外键：审核失败理由
     */
    @JsonProperty("reason")
    @TableField(exist = false)
    @ApiModelProperty(value = "审核失败理由")
    private String reason;
}
