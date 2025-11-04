package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 添加订单时 传过来的商品列表数据
 */
@Data
public class OrderAddGoodsDTO {
    @JsonProperty("good_id")
    private String goodId;
    private Integer number;
}
