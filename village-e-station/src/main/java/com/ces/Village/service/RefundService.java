package com.ces.village.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.village.pojo.entity.Refund;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【refund(待退款表)】的数据库操作Service
 * @createDate 2024-01-20 19:13:23
 */
public interface RefundService extends IService<Refund> {
    /**
     * 根据退款单号查询退款列表
     *
     * @param refundNo
     * @return
     */
    List<Refund> listByRefundNo(String refundNo);

    /**
     * 根据退款单号查询退款列表
     *
     * @param refundNo
     * @return
     */
    Refund getByRefundNo(String refundNo);

    /**
     * 根据退款单号删除退款列表
     *
     * @param refundNo
     * @return
     */
    boolean deleteByRefundNo(String refundNo);

    /**
     * 查询退款列表
     */
    Page<Refund> getRefundList(Integer curPage);

    /**
     * 查询退款列表根据用户id
     */
    Page<Refund> getRefundListByUserId(Integer curPage, Long userId);

    /**
     * 修改退款状态
     * @param refundNo
     * @param status
     */
    boolean updateStatusByRefundNo(String refundNo, Integer status);

    /**
     * 获取待退款列表
     * @return
     */
    List<Refund> getPendingRefundList();

    Refund getByOrderNoAndGoodId(String orderNo, String goodId);
}
