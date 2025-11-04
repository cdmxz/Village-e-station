package com.ces.village.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.pojo.dto.JobAddDTO;
import com.ces.village.pojo.dto.JobDTO;
import com.ces.village.pojo.dto.ReviewPushDTO;

/**
 * 企业招聘、零工招聘、零工求职 都拥有的方法
 *
 * @param <T>
 */
public interface BaseJobService<T> {
    /**
     * 更新招聘、求职信息状态
     *
     * @param jobDTO
     */
    boolean updateStatus(JobDTO jobDTO);

    /**
     * 分页查询
     * 所有招聘、求职信息
     *
     * @return 只返回status=审核成功的信息
     */
    Page<T> listReviewSuccess(Integer curPage, String keyword);

    /**
     * 根据用户id和状态
     * 分页查询招聘、求职信息
     */
    Page<T> listByUserIdAndStatus(Integer curPage, Long userId, Integer status, String keyword);

    /**
     * 发布招聘信息
     */
    boolean publish(JobAddDTO jobAddDTO);

    /**
     * 根据信息状态
     * 分页查询招聘、求职信息
     */
    Page<T> listByStatus(Integer currentPage, Integer status, String keyword);

    /**
     * 更新招聘、求职信息内容
     */
    boolean updateMsg(JobAddDTO jobAddDTO);

    /**
     * 更新招聘表到达截止时间的信息的状态为已截止
     */
    void updateDeadlineStatus();

    /**
     * 根据id 获取招聘信息详情
     */
    T getDetailsById(Long id);

    /**
     * 根据id 删除招聘信息
     */
    boolean deleteInfo(Long id);

    /**
     * 根据id 获取ReviewPushDTO
     */
    ReviewPushDTO getReviewPushById(Long informationId);
}
