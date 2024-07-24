package com.ces.Village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 发送短信验证码dto
 */
@Data
@ApiModel(description = "发送短信验证码dto")
public class SMSDTO {
    @NotBlank(message = "手机号不能为空")
    @JsonProperty("phone")
    private String phone;
}
