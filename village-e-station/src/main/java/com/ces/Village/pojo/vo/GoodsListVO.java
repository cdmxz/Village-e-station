package com.ces.village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 查询商品列表返回数据
 */
@Data
public class GoodsListVO {

    @JsonProperty("good_id")
    private Long id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;

    @JsonProperty("category_id")
    private Integer categoryId;

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("stock")
    private Integer stock;

    @JsonProperty("surplus")
    private Integer surplus;

    @JsonProperty("recent_sales")
    private Integer recentSales;

    @JsonProperty("sales_quantity")
    private Integer salesQuantity;

}
