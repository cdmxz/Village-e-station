package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录
 */
@Data
public class UserLoginDTO implements Serializable {

    @JsonProperty("code")
    private String code;

    @JsonProperty("openid")
    private String openid;
}
