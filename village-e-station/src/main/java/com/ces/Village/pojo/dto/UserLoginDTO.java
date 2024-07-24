package com.ces.Village.pojo.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

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
