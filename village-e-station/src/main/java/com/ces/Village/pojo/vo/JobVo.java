package com.ces.Village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(description = "企业招聘、零工招聘信息列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobVo {
    @ApiModelProperty(value = "招聘信息id")
    @JsonProperty(value = "information_id")
    private Long id;

    @ApiModelProperty(value = "标题")
    @JsonProperty(value = "title")
    private String title;

    @ApiModelProperty(value = "工作岗位")
    @JsonProperty(value = "work")
    private String work;

    @ApiModelProperty(value = "招聘人数")
    @JsonProperty(value = "recruits_number")
    private Integer recruitsNumber;

    @ApiModelProperty(value = "薪水：6000-8000元/月")
    @JsonProperty(value = "salary")
    private String salary;

    @ApiModelProperty(value = "工作地址")
    @JsonProperty(value = "work_address")
    private String workAddress;

    @ApiModelProperty(value = "发布时间")
    @JsonProperty(value = "publish_time")
    private LocalDateTime publishTime;

    @ApiModelProperty(value = "截止招聘时间")
    @JsonProperty(value = "deadline")
    private LocalDateTime deadline;
}
