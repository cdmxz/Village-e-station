package com.ces.Village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartVO implements Serializable {
    //商品id
    @JsonProperty("good_id")
    private Long goodsId;
    //商品数量
    @JsonProperty("count")
    private Integer number;
}
