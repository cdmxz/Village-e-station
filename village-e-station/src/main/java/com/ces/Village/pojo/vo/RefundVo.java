package com.ces.village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款列表vo
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RefundVo {
    /**
     * 商品名称
     */
    private String name;
    /**
     * 略缩图url
     */
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    /**
     * 商品价格
     */
    @JsonProperty("price")
    private BigDecimal price;
    /**
     * 该商品的数量
     */
    private Integer quantity;
    /**
     * 状态：2待退款，3已退款
     */
    private Integer status;
    /**
     * 申请时间
     */
    @JsonIgnore
    @JsonProperty("put_time")
    private LocalDateTime putTime;

    /**
     * 退款单号
     */
    @JsonProperty("refund_no")
    private Long refundNo;

    /**
     * 退款总金额
     */
    @JsonProperty("refund_amount")
    private BigDecimal refundAmount;

    /**
     * 退款出错原因
     */
    @JsonProperty("err_reason")
    private String errReason;

    /**
     * 退款原因
     */
    @JsonProperty("reason")
    private String reason;

    /**
     * 拒绝理由
     */
    @JsonProperty("reject_reason")
    private String rejectReason;

    /**
     * 订单号
     */
    @JsonProperty("order_no")
    private Long orderNo;

}
