package com.ces.village.pojo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommentStatusDTO {

    @JsonProperty("comment_id")
    private Long commentId;

    @JsonProperty("status")
    private Integer status;
}
