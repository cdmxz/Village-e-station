package com.ces.village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SurplusVO {

    @JsonProperty("good_id")
    private Long id;

    @JsonProperty("surplus")
    private Integer surplus;
}
