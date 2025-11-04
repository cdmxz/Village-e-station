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
 * 零工求职信息表
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("gig_job_hunting")
@Schema(name = "GigJobHunting对象", description = "零工求职信息表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GigJobHunting implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

     @Schema(name = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonProperty("information_id")
    private Long id;

     @Schema(name = "标题")
    @JsonProperty("title")
    private String title;

     @Schema(name = "期望工作")
    @JsonProperty("expected_work")
    private String expectedWork;

     @Schema(name = "期望薪酬")
    @JsonProperty("expected_salary")
    private String expectedSalary;

     @Schema(name = "求职者")
    @JsonProperty("contact_person")
    private String contactPerson;

     @Schema(name = "联系方式，手机或邮箱")
    @JsonProperty("contact_information")
    private String contactInformation;

     @Schema(name = "工作地址")
    @JsonProperty("work_address")
    private String workAddress;

     @Schema(name = "工作开始时间")
    @JsonProperty("start_time")
    private LocalDateTime startTime;

     @Schema(name = "工作结束时间")
    @JsonProperty("end_time")
    private LocalDateTime endTime;

     @Schema(name = "招聘发布时间")
    @JsonProperty("publish_time")
    private LocalDateTime publishTime;

     @Schema(name = "招聘下架时间")
    @JsonProperty("deadline")
    private LocalDateTime deadline;

     @Schema(name = "状态：1、待审核2、审核成功，展示中3、审核失败，已打回4、到达截止时间，已下架")
    @JsonProperty("status")
    private Integer status;

    @JsonIgnore
     @Schema(name = "用户id")
    private Long userId;

    /**
     * 外键：审核失败理由
     */
    @JsonProperty("reason")
    @TableField(exist = false)
     @Schema(name = "审核失败理由")
    private String reason;
}
