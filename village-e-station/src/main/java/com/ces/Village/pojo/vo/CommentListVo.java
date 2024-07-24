package com.ces.Village.pojo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@ApiModel(description = "评论列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentListVo<T> {

    @JsonProperty("pagecount")
    private Long pageCount;

    @JsonProperty("user")
    private String userName;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("list")
    private List<T> list;

    public CommentListVo(Long pageCount, List<T> list) {
        this.pageCount = pageCount;
        this.list = list;
    }
}
