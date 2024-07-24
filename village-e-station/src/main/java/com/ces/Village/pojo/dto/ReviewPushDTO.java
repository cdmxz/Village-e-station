package com.ces.Village.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ReviewPushDTO {
    // 要发给目标用户的用户id
    private String userId;
    // 审核结果
    private String result;
    // 审核内容
    private String content;
    // 审核备注
    private String notes;
}
