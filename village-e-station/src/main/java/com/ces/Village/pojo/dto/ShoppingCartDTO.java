package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("good_id")
    private Long goodsId;

    @JsonProperty("number")
    private Integer number;

}
