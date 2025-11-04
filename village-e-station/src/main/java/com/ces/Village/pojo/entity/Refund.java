package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 待退款表
 *
 * @TableName refund
 */
@Data
@Accessors(chain = true)
@TableName("refund")
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class Refund implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @JsonIgnore
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 商品名称
     */
    @JsonProperty("name")
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
     * 商品码
     */
    @JsonProperty("code")
    private String code;
    /**
     * 该商品的数量
     */
    @JsonProperty("quantity")
    private Integer quantity;
    /**
     * 订单号
     */
    @JsonIgnore
    @JsonProperty("order_no")
    private Long orderNo;
    /**
     * 要退款的商品id
     */
    @JsonProperty("good_id")
    private Long goodId;
    /**
     * 状态：2待退款，3已退款
     */
    @JsonProperty("refund_status")
    private Integer status;
    /**
     * 用户id
     */
    @JsonIgnore
    @JsonProperty("user_id")
    private Long userId;
    /**
     * 申请时间
     */
    @JsonProperty("put_time")
    private LocalDateTime putTime;
    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款单号
     */
    @JsonProperty("refund_no")
    private Long refundNo;

    /**
     * 退款金额
     */
    @JsonProperty("refund_amount")
    private BigDecimal refundAmount;

    /**
     * 是否同意退款
     */
    @JsonIgnore
    private Integer isAgree;

    /**
     * 是否已经向微信发起退款申请
     */
    @JsonIgnore
    private Integer isApply;

    /**
     * 退款出错原因
     */
    @JsonProperty("err_reason")
    private String errReason;

    /**
     * 拒绝理由
     */
    @JsonProperty("reject_reason")
    private String rejectReason;
}
