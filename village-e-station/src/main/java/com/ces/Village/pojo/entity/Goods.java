package com.ces.Village.pojo.entity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品信息表
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("goods")
@ApiModel(value = "Goods对象", description = "商品信息表")
public class Goods implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonProperty("good_id")
    private Long id;

    @ApiModelProperty(value = "商品名称")
    @JsonProperty("name")
    private String name;

    @ApiModelProperty(value = "商品价格")
    @JsonProperty("price")
    private BigDecimal price;

    @ApiModelProperty(value = "邮费")
    @JsonProperty("postage")
    private BigDecimal postage;

    @ApiModelProperty(value = "商品码")
    @JsonProperty("code")
    private String code;

    @ApiModelProperty(value = "略缩图url")
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @ApiModelProperty(value = "轮播图urls")
    @JsonProperty("rotation_urls")
    private String rotationUrls;

    @ApiModelProperty(value = "描述信息")
    @JsonProperty("description")
    private String description;

    @ApiModelProperty(value = "是否删除")
    @JsonProperty("is_deleted")
    private Integer isDeleted;

    @ApiModelProperty(value = "分类id")
    @JsonProperty("category_id")
    private Integer categoryId;
    //1 在售 2 下架 3 库存不足
    @ApiModelProperty(value = "销售状态")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "总货存")
    @JsonProperty("stock")
    private Integer stock;

    @ApiModelProperty(value = "剩余库存")
    @JsonProperty("surplus")
    private Integer surplus;

    @ApiModelProperty(value = "近30天销量")
    @JsonProperty("recent_sales")
    private Integer recentSales;

    @ApiModelProperty(value = "销售总量")
    @JsonProperty("sales_quantity")
    private Integer salesQuantity;

    public void setRecentSales(Integer recentSales) {
        if (recentSales < 0)
            recentSales = 0;
        this.recentSales = recentSales;
    }

    public void setSalesQuantity(Integer salesQuantity) {
        if (salesQuantity < 0)
            salesQuantity = 0;
        this.salesQuantity = salesQuantity;
    }
}
