package com.ces.Village.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.io.Serial;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 购物车
 * </p>
 *
 * @author author
 * @since 2023-10-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("shopping_cart")
@ApiModel(value="ShoppingCart对象", description="购物车")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCart implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @ApiModelProperty(value = "主键")
    @TableField("user_id")
    @JsonProperty("user_id")
    private Long userId;

    @ApiModelProperty(value = "商品id")
    @TableField("goods_id")
    @JsonProperty("good_id")
    private Long goodsId;

    @ApiModelProperty(value = "数量")
    @TableField("number")
    private Integer number;

    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    @JsonProperty("create_time")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否选中")
    @TableField("checked")
    private Integer checked;
}
