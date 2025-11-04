package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 重置密码dto
 */
@Data
@Schema(description = "重置密码dto")
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
