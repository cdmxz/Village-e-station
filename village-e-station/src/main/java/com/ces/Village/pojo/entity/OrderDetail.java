package com.ces.Village.pojo.entity;

import java.io.Serial;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author author
 * @since 2023-11-18
 */
@Data
@Accessors(chain = true)
@TableName("order_detail")
@ApiModel(value = "OrderDetail对象", description = "订单明细表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonIgnore
    private Long id;

    @ApiModelProperty(value = "外键：商品id")
    @JsonProperty("good_id")
    private Long goodId;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(value = "略缩图url")
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @ApiModelProperty(value = "商品价格")
    @JsonProperty("price")
    private BigDecimal price;

    @ApiModelProperty(value = "商品码")
    @JsonProperty("code")
    private String code;

    @ApiModelProperty(value = "每种商品的数量")
    @JsonProperty("quantity")
    private Integer quantity;

    @JsonIgnore
    @ApiModelProperty(value = "订单号")
    @JsonProperty("order_no")
    private Long orderNo;

    @ApiModelProperty("商品状态：1、正常2、退款中3、已退款")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty("用户id")
    @JsonProperty("user_id")
    private Long userId;

//    @ApiModelProperty("退款单号")
//    @JsonProperty("refund_no")
//    private Long refundNo;
}
