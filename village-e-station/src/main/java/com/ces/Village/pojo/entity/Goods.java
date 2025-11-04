package com.ces.village.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

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
@Schema(name = "Goods对象", description = "商品信息表")
public class Goods implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

     @Schema(name = "主键")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonProperty("good_id")
    private Long id;

     @Schema(name = "商品名称")
    @JsonProperty("name")
    private String name;

     @Schema(name = "商品价格")
    @JsonProperty("price")
    private BigDecimal price;

     @Schema(name = "邮费")
    @JsonProperty("postage")
    private BigDecimal postage;

     @Schema(name = "商品码")
    @JsonProperty("code")
    private String code;

     @Schema(name = "略缩图url")
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

     @Schema(name = "轮播图urls")
    @JsonProperty("rotation_urls")
    private String rotationUrls;

     @Schema(name = "描述信息")
    @JsonProperty("description")
    private String description;

     @Schema(name = "是否删除")
    @JsonProperty("is_deleted")
    private Integer isDeleted;

     @Schema(name = "分类id")
    @JsonProperty("category_id")
    private Integer categoryId;
    //1 在售 2 下架 3 库存不足
     @Schema(name = "销售状态")
    @JsonProperty("status")
    private Integer status;

     @Schema(name = "总货存")
    @JsonProperty("stock")
    private Integer stock;

     @Schema(name = "剩余库存")
    @JsonProperty("surplus")
    private Integer surplus;

     @Schema(name = "近30天销量")
    @JsonProperty("recent_sales")
    private Integer recentSales;

     @Schema(name = "销售总量")
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
