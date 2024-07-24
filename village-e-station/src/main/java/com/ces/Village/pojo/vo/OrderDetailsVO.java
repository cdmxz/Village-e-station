package com.ces.Village.pojo.vo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.ces.Village.pojo.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_no")
    private Long orderNo;

    @ApiModelProperty(value = "运费")
    @JsonProperty("postage")
    private BigDecimal postage;

    @ApiModelProperty(value = "总金额")
    @JsonProperty("total_amount")
    private BigDecimal totalAmount;

    @ApiModelProperty(value = "优惠金额")
    @JsonProperty("discount_amount")
    private BigDecimal discountAmount;

    @ApiModelProperty(value = "实际价格")
    @JsonProperty("actual_amount")
    private BigDecimal actualAmount;

    @ApiModelProperty(value = "收货信息：张x,139xx,广东省xx")
    @JsonProperty("receive_information")
    private String receiveInformation;

    @ApiModelProperty(value = "物流单号")
    @JsonProperty("track_number")
    private String trackNumber;

    @ApiModelProperty(value = "下单时间")
    @JsonProperty("order_time")
    private LocalDateTime orderTime;

    @ApiModelProperty(value = "付款时间")
    @JsonProperty("pay_time")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "发货时间")
    @JsonProperty("send_time")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "收货时间")
    @JsonProperty("receive_time")
    private LocalDateTime receiveTime;

    @ApiModelProperty(value = "订单状态 1待付款、2待发货、3待收货、4已完成、5已关闭")
    @JsonProperty("status")
    private Integer orderStatus;

    @ApiModelProperty(value = "外键：订单详情")
    @TableField(exist = false)
    @JsonProperty("goods")
    private List<OrderDetail> orderDetails;
}
