package com.ces.Village.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 重置密码dto
 */
@Data
@ApiModel(description = "重置密码dto")
public class ResetPwdDTO {
    @NotBlank(message = "手机号不能为空")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @JsonProperty("password")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @JsonProperty("code")
    private String code;

}
