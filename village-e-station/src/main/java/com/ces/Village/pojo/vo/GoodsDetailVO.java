package com.ces.Village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 查看商品详情返回数据
 */
@Data
public class GoodsDetailVO implements Serializable {

    @JsonProperty("good_id")
    private Long id;

    //商品名称
    @JsonProperty("name")
    private String name;
    //商品价格
    @JsonProperty("price")
    private BigDecimal price;
    //邮费
    @JsonProperty("postage")
    private BigDecimal postage;
    //缩略图
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    //轮播图
    @JsonProperty("rotation_urls")
    private List<String> rotationUrls;
    
     @JsonProperty("category_id")
    private Integer categoryId;

    //1 在售 2 下架 3 库存不足
     @JsonProperty("status")
    private Integer status;
    //描述信息
    @JsonProperty("description")
    private String description;
    //总库存
    @JsonProperty("stock")
    private Integer stock;
    //剩余库存
    @JsonProperty("surplus")
    private Integer surplus;
    //近30天销量
    @JsonProperty("recent_sales")
    private Integer recentSales;
    //销售总量
    @JsonProperty("sales_quantity")
    private Integer salesQuantity;
}
