package com.ces.Village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderAddDTO implements Serializable {
    @JsonProperty("address_id")
    private String userAddressId;

    @JsonProperty("goods")
    private List<OrderAddGoodsDTO> goods;
}
