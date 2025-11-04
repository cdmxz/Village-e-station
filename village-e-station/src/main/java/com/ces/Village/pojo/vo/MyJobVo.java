package com.ces.village.pojo.vo;

import com.ces.village.constant.JobStatusConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "我发布的零工求职信息列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyJobVo {
    @Schema(name = "招聘信息id")
    @JsonProperty(value = "information_id")
    private Long id;

    @Schema(name = "标题")
    @JsonProperty(value = "title")
    private String title;

    @Schema(name = "工作岗位")
    @JsonProperty(value = "work")
    private String work;

    @Schema(name = "招聘人数")
    @JsonProperty(value = "recruits_number")
    private Integer recruitsNumber;

    @Schema(name = "薪水：6000-8000元/月")
    @JsonProperty(value = "salary")
    private String salary;

    @Schema(name = "工作地址")
    @JsonProperty(value = "work_address")
    private String workAddress;

    @Schema(name = "发布时间")
    @JsonProperty(value = "publish_time")
    private LocalDateTime publishTime;

    @Schema(name = "截止招聘时间")
    @JsonProperty(value = "deadline")
    private LocalDateTime deadline;

    @Schema(name = "审核失败理由")
    @JsonProperty(value = "reason")
    private String reason;

    @Schema(name = "状态：1、待审核2、审核成功，展示中3、审核失败，已打回4、到达截止时间，已下架")
    @JsonProperty(value = "status")
    private Integer status;

    @JsonProperty(value = "status_str")
    private String statusStr;

    public void setStatus(Integer status) {
        statusStr = JobStatusConstant.toString(status);
        this.status = status;
    }
}
