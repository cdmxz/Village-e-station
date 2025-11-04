package com.ces.village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户登录时返回的token
 */
@Data
@AllArgsConstructor
public class LoginVO {
    @JsonProperty("user_token")
    private String userToken;

//    @JsonProperty("openid")
//    private String openId;

}
