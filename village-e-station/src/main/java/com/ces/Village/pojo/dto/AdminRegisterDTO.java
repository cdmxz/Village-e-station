package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 管理员注册DTO
 */
@Data
public class AdminRegisterDTO {
    @NotBlank(message = "乡村不能为空")
    @JsonProperty("village")
    private String village;

    @NotBlank(message = "手机号不能为空")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @JsonProperty("password")
    private String password;

    @NotBlank(message = "头像不能为空")
    @JsonProperty("avatar_url")
    private String avatarUrl;

    @NotBlank(message = "昵称不能为空")
    @JsonProperty("nick_name")
    private String nickName;

    @NotBlank(message = "姓名不能为空")
    @JsonProperty("name")
    private String name;
}
