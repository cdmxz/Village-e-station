package com.ces.Village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDefaultVO {
    @JsonProperty("address_id")
    private Long id;

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
}
