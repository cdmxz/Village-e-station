package com.ces.Village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.pojo.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-11-18
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    /**
     * 根据订单状态查询订单列表
     *
     * @param orderStatus
     * @return
     */
    Page<Orders> getOrderListByStatus(Page<Orders> page,
                                      @Param("keyword") String keyword,
                                      @Param("orderStatus") Integer orderStatus);

    /**
     * 根据订单号查询订单详情
     * @param orderNo
     * @return
     */
    Orders getDetailsByOrderNo(String orderNo);

    /**
     * 根据订单状态和用户id 查询订单列表
     *
     * @param orderStatus
     * @return
     */
    Page<Orders> getOrderListByStatusAndUserId(Page<Orders> page,
                                               @Param("orderStatus") Integer orderStatus,
                                               @Param("userId")Long userId);

    /**
     * 根据订单号查询订单
     * @param orderNo
     */
    @Select("select * from orders where order_no = #{orderNo}")
    Orders getByOrderNo(String orderNo);
}
