package com.ces.Village.pojo.entity;

import java.io.Serial;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author author
 * @since 2023-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("orders")
@ApiModel(value = "Orders对象", description = "订单表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Orders implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long orderId;

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

    @ApiModelProperty(value = "用户id")
    @JsonIgnore
    @JsonProperty("user_id")
    private Long userId;

    @ApiModelProperty(value = "到期时间 未付款订单使用")
    @JsonProperty("expire_time")
    private LocalDateTime expireTime;

    @ApiModelProperty(value = "外键：订单详情")
    @TableField(exist = false)
    @JsonProperty("goods")
    private List<OrderDetail> orderDetails;
}
