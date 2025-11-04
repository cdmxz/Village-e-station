package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


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
@Schema(name = "OrderDetail对象", description = "订单明细表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDetail implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(name = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonIgnore
    private Long id;

    @Schema(name = "外键：商品id")
    @JsonProperty("good_id")
    private Long goodId;

    @Schema(name = "商品名称")
    @JsonProperty("name")
    private String name;

    @Schema(name = "略缩图url")
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @Schema(name = "商品价格")
    @JsonProperty("price")
    private BigDecimal price;

    @Schema(name = "商品码")
    @JsonProperty("code")
    private String code;

    @Schema(name = "每种商品的数量")
    @JsonProperty("quantity")
    private Integer quantity;

    @JsonIgnore
    @Schema(name = "订单号")
    @JsonProperty("order_no")
    private Long orderNo;

    @Schema(name = "商品状态：1、正常2、退款中3、已退款")
    @JsonProperty("status")
    private Integer status;

    @Schema(name = "用户id")
    @JsonProperty("user_id")
    private Long userId;

//    @Schema("退款单号")
//    @JsonProperty("refund_no")
//    private Long refundNo;
}
