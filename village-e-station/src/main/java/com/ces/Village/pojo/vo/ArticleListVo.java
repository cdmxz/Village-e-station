package com.ces.village.pojo.vo;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.pojo.entity.Article;
import com.ces.village.utils.ConvertUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@Schema(description = "文章列表")
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
