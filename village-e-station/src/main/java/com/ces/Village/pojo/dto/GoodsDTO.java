package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsDTO {
    @JsonProperty("good_id")
    private Long id;
    //商品名称
    private String name;
    //商品分类id
    @JsonProperty("category_id")
    private Integer categoryId;
    //商品价格
    private BigDecimal price;
    //邮费
    private BigDecimal postage;
    //缩略图url
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    //轮播图
    @JsonProperty("rotation_urls")
    private String[] rotationUrls;
    //描述信息
    private String description;
    //1 在售 2 下架 3 库存不足
    private Integer status;
    //总库存
    private Integer stock;
    //库存
    private Integer surplus;
    //近30天销量
    @JsonProperty("recent_sales")
    private Integer recentSales;
    //销售总量
    @JsonProperty("sales_quantity")
    private Integer salesQuantity;
}
