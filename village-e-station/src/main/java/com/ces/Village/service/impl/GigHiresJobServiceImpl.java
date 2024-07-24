package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.common.BaseContext;
import com.ces.Village.common.CurrentUser;
import com.ces.Village.common.R;
import com.ces.Village.exception.CustomException;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.constant.JobStatusConstant;
import com.ces.Village.constant.JobTypeConstant;
import com.ces.Village.mapper.GigHiresMapper;
import com.ces.Village.pojo.dto.JobAddDTO;
import com.ces.Village.pojo.dto.JobDTO;
import com.ces.Village.pojo.dto.ReviewPushDTO;
import com.ces.Village.pojo.entity.GigHires;
import com.ces.Village.pojo.entity.Reason;
import com.ces.Village.service.GigHiresJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.Village.service.OssService;
import com.ces.Village.service.ReasonService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 零工招聘信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Service
public class GigHiresJobServiceImpl extends ServiceImpl<GigHiresMapper, GigHires> implements GigHiresJobService {
    @Autowired
    private ReasonService reasonService;
    @Autowired
    private GigHiresMapper gigHiresMapper;

    /**
     * 修改零工招聘状态
     *
     * @param jobDTO
     */
    public boolean updateStatus(JobDTO jobDTO) {
        GigHires gigHires = gigHiresMapper.selectById(jobDTO.getInformationId());
        if (gigHires == null) {
            throw new CustomException(ErrorCodeEnum.GIG_HIRES_NOT_FOUND);
        }
        CurrentUser currentUser = BaseContext.getCurrentUser();
        // 当前用户不是管理员，这条消息也不是当前用户发布的
        if (!currentUser.isAdmin() &&
                !Objects.equals(currentUser.getId(), gigHires.getUserId())) {
            throw new CustomException(ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
        }
        // 设置状态
        gigHires.setStatus(jobDTO.getStatus());
        // 如果设置状态为拒绝，要把拒绝理由写进理由表
        if (gigHires.getStatus() == JobStatusConstant.REVIEW_FAILURE) {
            if (StringUtils.isEmpty(jobDTO.getReason())) {
                throw new CustomException(ErrorCodeEnum.PARAM_IS_NULL);
            }
            Reason reason = new Reason();
            reason.setReason(jobDTO.getReason());
            reason.setType(jobDTO.getType());
            reason.setInformationId(gigHires.getId());
            reasonService.save(reason);
        } else {
            // 设置为其他状态，查询理由表，如果存在拒接理由就删除
            Reason reason = reasonService.getReason(gigHires.getId(), JobTypeConstant.GIG_HIRES);
            if (reason != null) {
                reasonService.removeById(reason);
            }
        }
        return gigHiresMapper.updateById(gigHires) > 0;
    }

    /**
     * 分页查询
     * 零工招聘信息表的
     * 所有招聘信息
     *
     * @param curPage
     * @return 只返回status=审核成功的信息
     */
    @Override
    public Page<GigHires> listReviewSuccess(Integer curPage, String keyword) {
        int cur = (curPage != null) ? curPage : 1;
        Page<GigHires> page = new Page<>(cur, 10);
        return gigHiresMapper.getPageByUserIdAndStatus(page, null, JobStatusConstant.REVIEW_SUCCESS, keyword);
    }


    /**
     * 分页查询 用户发布在
     * 零工招聘信息表的招聘信息
     *
     * @param currentPage
     * @param status
     */
    @Override
    public Page<GigHires> listByUserIdAndStatus(Integer currentPage, Long userId, Integer status, String keyword) {
        int cur = (currentPage != null) ? currentPage : 1;
        Page<GigHires> page = new Page<>(cur, 10);
        return gigHiresMapper.getPageByUserIdAndStatus(page, userId, status, keyword);
    }

    /**
     * 发布零工招聘信息
     *
     * @param jobAddDTO
     * @return
     */
    @Override
    public boolean publish(JobAddDTO jobAddDTO) {
        GigHires gigHires = jobAddDTO.getGigHiresInformation();
        gigHires.setPublishTime(LocalDateTime.now());
        gigHires.setStatus(JobStatusConstant.REVIEW_PENDING);
        gigHires.setUserId(jobAddDTO.getUserId());
        return gigHiresMapper.insert(gigHires) > 0;
    }

    /**
     * 根据状态分页查询
     * 零工招聘表
     *
     * @param currentPage
     * @param status
     */
    @Override
    public Page<GigHires> listByStatus(Integer currentPage, Integer status, String keyword) {
        int cur = (currentPage != null) ? currentPage : 1;
        Page<GigHires> page = new Page<>(cur, 10);
        return gigHiresMapper.getPageByUserIdAndStatus(page, null, status, keyword);
    }

    /**
     * 更新招聘、求职信息内容
     */
    @Override
    public boolean updateMsg(JobAddDTO jobAddDTO) {
        GigHires gigHires = jobAddDTO.getGigHiresInformation();
        GigHires old = gigHiresMapper.selectById(gigHires.getId());
        if (old == null) {
            throw new CustomException(ErrorCodeEnum.GIG_HIRES_NOT_FOUND);
        }
        gigHires.setStatus(JobStatusConstant.REVIEW_PENDING);
        // 查询理由表，如果存在拒接理由就删除
        Reason reason = reasonService.getReason(gigHires.getId(), JobTypeConstant.GIG_JOB_HUNTING);
        if (reason != null) {
            reasonService.removeById(reason);
        }
        return gigHiresMapper.updateById(gigHires) > 0;
    }

    /**
     * 更新招聘表到达截止时间的信息的状态为已截止
     */
    @Override
    public void updateDeadlineStatus() {
        LambdaQueryWrapper<GigHires> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GigHires::getStatus, JobStatusConstant.REVIEW_SUCCESS);
        // 获取招聘信息的截止时间
        List<GigHires> enterpriseHires = gigHiresMapper.selectList(queryWrapper);
        // 到截止时间后,更新企业招聘信息表的状态为已下架
        for (GigHires gigHires : enterpriseHires) {
            if (gigHires.getDeadline().isBefore(LocalDateTime.now())) {
                gigHiresMapper.updateStatusToDeadline(gigHires.getId());
            }
        }
    }

    /**
     * 根据id 获取招聘信息详情
     *
     * @param id
     */
    @Override
    public GigHires getDetailsById(Long id) {
        GigHires gigHires = this.getById(id);
        if (gigHires == null) {
            throw new CustomException(ErrorCodeEnum.GIG_HIRES_NOT_FOUND);
        } else {
            return gigHires;
        }
    }

    /**
     * 根据id 获取ReviewPushDTO
     *
     * @param id
     */
    @Override
    public ReviewPushDTO getReviewPushById(Long id) {
        GigHires gigHires = getDetailsById(id);
        return new ReviewPushDTO()
                .setUserId(gigHires.getUserId().toString())
                .setContent(gigHires.getTitle())
                .setNotes(gigHires.getTitle());
    }

    /**
     * 根据id 删除招聘信息
     *
     * @param id
     */
    @Override
    public boolean deleteInfo(Long id) {
        GigHires gigHires = this.getById(id);
        if (gigHires == null) {
            throw new CustomException(ErrorCodeEnum.GIG_HIRES_NOT_FOUND);
        }
        return gigHiresMapper.deleteById(id) > 0;
    }

    /**
     * 根据用户id删除零工招聘信息
     *
     * @param userId
     * @return
     */
    @Override
    public boolean removeByUserId(Long userId) {
        return gigHiresMapper.deleteByUserId(userId);
    }
}
