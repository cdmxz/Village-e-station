package com.ces.village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
 
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "企业招聘、零工招聘信息列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JobVo {
     @Schema(name  = "招聘信息id")
    @JsonProperty(value = "information_id")
    private Long id;

     @Schema(name  = "标题")
    @JsonProperty(value = "title")
    private String title;

     @Schema(name  = "工作岗位")
    @JsonProperty(value = "work")
    private String work;

     @Schema(name  = "招聘人数")
    @JsonProperty(value = "recruits_number")
    private Integer recruitsNumber;

     @Schema(name  = "薪水：6000-8000元/月")
    @JsonProperty(value = "salary")
    private String salary;

     @Schema(name  = "工作地址")
    @JsonProperty(value = "work_address")
    private String workAddress;

     @Schema(name  = "发布时间")
    @JsonProperty(value = "publish_time")
    private LocalDateTime publishTime;

     @Schema(name  = "截止招聘时间")
    @JsonProperty(value = "deadline")
    private LocalDateTime deadline;
}
