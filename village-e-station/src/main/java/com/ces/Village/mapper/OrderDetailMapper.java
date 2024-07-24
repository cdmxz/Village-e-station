package com.ces.Village.mapper;

import com.ces.Village.pojo.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 订单明细表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-11-18
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    /**
     * 更改订单号
     * @param oldOrderNo
     * @param newOrderNo
     * @return
     */
    @Update("UPDATE order_detail SET order_no=#{newOrderNo} WHERE order_no = #{oldOrderNo}")
    int changeOrderNo(Long oldOrderNo, Long newOrderNo);
}
