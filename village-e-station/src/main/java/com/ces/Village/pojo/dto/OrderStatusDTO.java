package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatusDTO implements Serializable {
    // 订单号
    @JsonProperty("order_no")
    private String orderNo;

    // 订单状态
    @JsonProperty("status")
    private Integer status;
}
