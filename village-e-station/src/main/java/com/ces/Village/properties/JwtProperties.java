package com.ces.Village.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "ces.jwt")
@Data
public class JwtProperties {

    private long tokenTtl;

    private String tokenName;
    /**
     * 生成jwt令牌
     */
    private String secretKey;

}
