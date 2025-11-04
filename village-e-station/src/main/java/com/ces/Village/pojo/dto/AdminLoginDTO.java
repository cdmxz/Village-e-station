package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "管理员登录时传递的数据模型")
public class AdminLoginDTO implements Serializable {

    @JsonProperty("username")
    private String phone;//手机号码

    @JsonProperty("password")
    private String password;//密码

    @JsonProperty("captcha")
    private String captcha;//图片验证码

    @JsonProperty("code")
    private String code;// 微信登录code

}
