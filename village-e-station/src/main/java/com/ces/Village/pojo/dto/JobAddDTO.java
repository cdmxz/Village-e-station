package com.ces.Village.pojo.dto;

import com.ces.Village.pojo.entity.EnterpriseHires;
import com.ces.Village.pojo.entity.GigHires;
import com.ces.Village.pojo.entity.GigJobHunting;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "添加招聘信息")
public class JobAddDTO {
    /**
     * 招聘类型
     */
    @JsonProperty("type")
    private Integer type;
    /**
     * 零工求职信息表
     */
    @JsonProperty("job_hunting_information")
    private GigJobHunting jobHuntingInformation;
    /**
     * 零工招聘信息表
     */
    @JsonProperty("gig_hires_information")
    private GigHires gigHiresInformation;
    /**
     * 企业招聘信息表
     */
    @JsonProperty("en_hires_information")
    private EnterpriseHires enHiresInformation;

    /**
     * 用户id
     */
    private Long userId;
}
