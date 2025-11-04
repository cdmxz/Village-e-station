package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 修改招聘信息状态DTO
 */
@Data
public class JobDTO {
    // 招聘id
    @JsonProperty("information_id")
    private Long informationId;
    // 招聘类型
    private Integer type;
    // 招聘状态
    private Integer status;
    // 拒绝理由
    private String reason;
}
