package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 发送短信验证码dto
 */
@Data
@Schema(description = "发送短信验证码dto")
public class SMSDTO {
    @NotBlank(message = "手机号不能为空")
    @JsonProperty("phone")
    private String phone;
}
