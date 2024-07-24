package com.ces.Village.pojo.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.pojo.entity.Orders;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 订单列表返回数据
 */
@Data
@Builder
public class OrderListVO {

    @JsonProperty("pagecount")
    private Long pageCount;

    @JsonProperty("list")
    private List<Orders> list;

    public static OrderListVO create(Page<Orders> page) {
        return OrderListVO.builder()
                .list(page.getRecords())
                .pageCount(page.getPages())
                .build();
    }
}
