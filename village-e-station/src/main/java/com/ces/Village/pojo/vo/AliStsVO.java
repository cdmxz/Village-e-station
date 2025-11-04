package com.ces.village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AliStsVO {
    @JsonProperty("AccessKeyId")
    private String AccessKeyId;

    @JsonProperty("AccessKeySecret")
    private String AccessKeySecret;

    @JsonProperty("SecurityToken")
    private String SecurityToken;

    @JsonProperty("Expiration")
    private String Expiration;

}
