package com.ces.village.pojo.vo;

import com.ces.village.pojo.entity.ShoppingCart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddVO implements Serializable {
    @JsonProperty("good_id")
    private Long goodsId;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("userToken")
    private String user_token;

    @JsonProperty("address_id")
    private Long userAddressId;

    @JsonProperty("goods")
    private List<ShoppingCart> goods;

}
