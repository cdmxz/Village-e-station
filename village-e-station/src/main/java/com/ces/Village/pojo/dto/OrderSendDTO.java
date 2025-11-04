package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderSendDTO implements Serializable {
    // 订单号
    @JsonProperty("order_no")
    private String orderNo;
    // 物流单号
    @JsonProperty("track_number")
    private String trackNumber;
}
