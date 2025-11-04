package com.ces.village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartGoodsVO {
    //商品idd
    @JsonProperty("good_id")
    private Long goodsId;
    //商品名称
    @JsonProperty("name")
    private String name;
    //数量
    @JsonProperty("number")
    private Integer number;
    //商品图片
    @JsonProperty("thumbnail_url")
    private String thumbnailUrl;
    //分类id
    @JsonProperty("category_id")
    private Long categoryId;
    //库存
    @JsonProperty("surplus")
    private String surplus;
    //价格
    @JsonProperty("price")
    private Integer price;
    //状态
    @JsonProperty("status")
    private Integer status;
}
