package com.ces.village.pojo.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.ces.village.pojo.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单详情数据
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetailsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "订单号")
    @JsonProperty("order_no")
    private Long orderNo;

    @Schema(name = "运费")
    @JsonProperty("postage")
    private BigDecimal postage;

    @Schema(name = "总金额")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @Schema(name = "优惠金额")
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @Schema(name = "实际价格")
    @JsonProperty("actual_amount")
    private BigDecimal actualAmount;

    @Schema(name = "收货信息：张x,139xx,广东省xx")
    @JsonProperty("receive_information")
    private String receiveInformation;

    @Schema(name = "物流单号")
    @JsonProperty("track_number")
    private String trackNumber;

    @Schema(name = "下单时间")
    @JsonProperty("order_time")
    private LocalDateTime orderTime;

    @Schema(name = "付款时间")
    @JsonProperty("pay_time")
    private LocalDateTime payTime;

    @Schema(name = "发货时间")
    @JsonProperty("send_time")
    private LocalDateTime sendTime;

    @Schema(name = "收货时间")
    @JsonProperty("receive_time")
    private LocalDateTime receiveTime;

    @Schema(name = "订单状态 1待付款、2待发货、3待收货、4已完成、5已关闭")
    @JsonProperty("status")
    private Integer orderStatus;

    @Schema(name = "外键：订单详情")
    @TableField(exist = false)
    @JsonProperty("goods")
    private List<OrderDetail> orderDetails;
}
