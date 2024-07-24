package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.constant.RefundAgreeConstant;
import com.ces.Village.constant.RefundApplyConstant;
import com.ces.Village.constant.RefundStatusConstant;
import com.ces.Village.exception.CustomException;
import com.ces.Village.pojo.entity.Refund;
import com.ces.Village.service.RefundService;
import com.ces.Village.mapper.RefundMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Ref;
import java.util.List;

/**
 * @author Administrator
 * @description 针对表【refund(待退款表)】的数据库操作Service实现
 * @createDate 2024-01-20 19:13:23
 */
@Service
public class RefundServiceImpl extends ServiceImpl<RefundMapper, Refund>
        implements RefundService {
    @Autowired
    private RefundMapper refundMapper;


    /**
     * 根据退款单号查询退款列表
     *
     * @param refundNo
     * @return
     */
    @Override
    public List<Refund> listByRefundNo(String refundNo) {
        LambdaQueryWrapper<Refund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Refund::getRefundNo, refundNo);
        return refundMapper.selectList(queryWrapper);
    }

    /**
     * 根据退款单号查询退款列表
     *
     * @param refundNo
     * @return
     */
    @Override
    public Refund getByRefundNo(String refundNo) {
        LambdaQueryWrapper<Refund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Refund::getRefundNo, refundNo);
        return refundMapper.selectOne(queryWrapper);
    }

    /**
     * 根据退款单号删除退款列表
     *
     * @param refundNo
     * @return
     */
    @Override
    public boolean deleteByRefundNo(String refundNo) {
        LambdaQueryWrapper<Refund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Refund::getRefundNo, refundNo);
        return refundMapper.delete(queryWrapper) > 0;
    }

    /**
     * 查询退款列表
     */
    @Override
    public Page<Refund> getRefundList(Integer curPage) {
        Page<Refund> page = new Page<>();
        page.setCurrent(curPage);
        LambdaQueryWrapper<Refund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Refund::getPutTime);
        return refundMapper.selectPage(page, queryWrapper);
    }

    /**
     * 查询退款列表根据用户id
     */
    @Override
    public Page<Refund> getRefundListByUserId(Integer curPage, Long userId) {
        Page<Refund> page = new Page<>();
        page.setCurrent(curPage);
        LambdaQueryWrapper<Refund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Refund::getUserId, userId);
        queryWrapper.orderByDesc(Refund::getPutTime);
        return refundMapper.selectPage(page, queryWrapper);
    }

    /**
     * 修改退款状态
     *
     * @param refundNo
     * @param status
     */
    @Override
    public boolean updateStatusByRefundNo(String refundNo, Integer status) {
        Refund refund = getByRefundNo(refundNo);
        if (refund == null) {
            throw new CustomException(ErrorCodeEnum.NOT_FOUND_REFUND);
        }
        refund.setStatus(status);
        return refundMapper.updateById(refund) > 0;
    }

    /**
     * 获取待退款列表
     *
     * @return
     */
    @Override
    public List<Refund> getPendingRefundList() {
        LambdaQueryWrapper<Refund> queryWrapper = new LambdaQueryWrapper<>();
        // 1、这个订单状态为待退款
        // 2、没有向微信发起退款申请
        // 3、管理员同意退款
        queryWrapper.eq(Refund::getIsApply, RefundApplyConstant.NO_APPLY)
                .eq(Refund::getStatus, RefundStatusConstant.PENDING_REFUND)
                .eq(Refund::getIsAgree, RefundAgreeConstant.AGREE);
        return refundMapper.selectList(queryWrapper);
    }


    @Override
    public Refund getByOrderNoAndGoodId(String orderNo, String goodId) {
        LambdaQueryWrapper<Refund> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Refund::getOrderNo, orderNo)
                .eq(Refund::getGoodId, goodId)
                .ne(Refund::getStatus, RefundStatusConstant.REJECT_REFUND);
        return refundMapper.selectOne(queryWrapper);
    }

}




