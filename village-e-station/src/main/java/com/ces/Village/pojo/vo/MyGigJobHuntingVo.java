package com.ces.Village.pojo.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ces.Village.constant.JobStatusConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "我发布的企业招聘、零工招聘信息列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyGigJobHuntingVo{
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

    @ApiModelProperty(value = "审核失败理由")
    @JsonProperty(value = "reason")
    private String reason;

    @ApiModelProperty(value = "状态：1、待审核2、审核成功，展示中3、审核失败，已打回4、到达截止时间，已下架")
    @JsonProperty(value = "status")
    private Integer status;
    @JsonProperty(value = "status_str")
    private String statusStr;

    public void setStatus(Integer status) {
        statusStr = JobStatusConstant.toString(status);
        this.status = status;
    }
}
