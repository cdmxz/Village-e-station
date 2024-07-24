package com.ces.Village.pojo.vo;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.pojo.entity.Article;
import com.ces.Village.utils.ConvertUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@ApiModel(description = "文章列表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleListVo {
    @JsonProperty("pagecount")
    private Long pageCount;

    @JsonProperty("list")
    private List<ArticleVo> list;

    public static ArticleListVo createByEntity(Page<Article> page) {
        List<ArticleVo> list = ConvertUtil.entityToVoList(page.getRecords(), ArticleVo.class);
        return ArticleListVo.builder()
                .list(list)
                .pageCount(page.getPages())
                .build();
    }
}
