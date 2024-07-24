package com.ces.Village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户请求退款DTO
 */
@Data
public class RefundDTO {
    // 订单号
    @JsonProperty("order_no")
    private String orderNo;

    // 退款订单号
    @JsonProperty("refund_no")
    private String refundNo;

    // 退款理由
    @JsonProperty("reason")
    private String reason;

//    // 退款商品
//    @JsonProperty("good_ids")
//    private List<String> goodIds;
    // 退款商品
    @JsonProperty("good_id")
    private String goodId;

    /**
     * 是否同意退款
     */
    @JsonProperty("is_agree")
    private Integer isAgree;
}
