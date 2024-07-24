package com.ces.Village.redis;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * redis存的sms对象
 * 用于短信验证
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SMSObject {
    /**
     * 验证码
     */
    @JsonProperty("code")
    public String code;
    /**
     * 验证码发送时间戳（毫秒）
     */
    @JsonProperty("sendingTimeMillis")
    public long sendingTimeMillis;
}
