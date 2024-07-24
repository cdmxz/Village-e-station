package com.ces.Village.pojo.vo;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.pojo.entity.Orders;
import com.ces.Village.pojo.entity.Refund;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 退款列表返回数据
 */
@Data
@Builder
public class RefundListVO {

    @JsonProperty("pagecount")
    private Long pageCount;

    @JsonProperty("list")
    private List<RefundVo> list;

//    public static RefundListVO create(Page<Refund> page) {
//        return RefundListVO.builder()
//                .list(page.getRecords())
//                .pageCount(page.getPages())
//                .build();
//    }
}
