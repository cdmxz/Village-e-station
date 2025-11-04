package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 获取微信api的访问token时，返回的json数据
 */
@Builder
@Data
public class WxTokenResponse {

    @JsonProperty("errcode")
    private String errCode;

    @JsonProperty("errmsg")
    private String errMsg;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("access_token")
    private String accessToken;

    /**
     * token到期时间戳（毫秒）
     */
    private long expireTime;
}
