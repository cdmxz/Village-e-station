package com.ces.Village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddressVO {
    @JsonProperty("consignee")
    private String consignee;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("district")
    private String district;

    @JsonProperty("detail")
    private String detail;

    @JsonProperty("is_default")
    private String isDefault;
}
