package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.common.BaseContext;
import com.ces.Village.common.CurrentUser;
import com.ces.Village.exception.CustomException;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.constant.JobStatusConstant;
import com.ces.Village.constant.JobTypeConstant;
import com.ces.Village.mapper.GigJobHuntingMapper;
import com.ces.Village.pojo.dto.JobAddDTO;
import com.ces.Village.pojo.dto.JobDTO;
import com.ces.Village.pojo.dto.ReviewPushDTO;
import com.ces.Village.pojo.entity.GigHires;
import com.ces.Village.pojo.entity.GigJobHunting;
import com.ces.Village.pojo.entity.Reason;
import com.ces.Village.service.GigHuntingJobService;
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
 * 零工求职信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Service
public class GigHuntingJobServiceImpl extends ServiceImpl<GigJobHuntingMapper, GigJobHunting> implements GigHuntingJobService {
    @Autowired
    private GigJobHuntingMapper gigJobHuntingMapper;

    @Autowired
    private ReasonService reasonService;

    @Override
    public boolean updateStatus(JobDTO jobDTO) {
        GigJobHunting gigJobHunting = gigJobHuntingMapper.selectById(jobDTO.getInformationId());
        if (gigJobHunting == null) {
            throw new CustomException(ErrorCodeEnum.GIG_JOB_HUNTING_NOT_FOUND);
        }
        CurrentUser currentUser = BaseContext.getCurrentUser();
        // 当前用户不是管理员，这条消息也不是当前用户发布的
        if (!currentUser.isAdmin() &&
                !Objects.equals(currentUser.getId(), gigJobHunting.getUserId())) {
            throw new CustomException(ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
        }
        gigJobHunting.setStatus(jobDTO.getStatus());
        // 如果设置状态为拒绝，要把拒绝理由写进理由表
        if (gigJobHunting.getStatus() == JobStatusConstant.REVIEW_FAILURE) {
            if (StringUtils.isEmpty(jobDTO.getReason())) {
                throw new CustomException(ErrorCodeEnum.PARAM_IS_NULL);
            }
            Reason reason = new Reason();
            reason.setReason(jobDTO.getReason());
            reason.setType(jobDTO.getType());
            reason.setInformationId(gigJobHunting.getId());
            reasonService.save(reason);
        } else {
            // 设置为其他状态，查询理由表，如果存在拒接理由就删除
            Reason reason = reasonService.getReason(gigJobHunting.getId(), JobTypeConstant.GIG_JOB_HUNTING);
            if (reason != null) {
                reasonService.removeById(reason);
            }
        }
        return gigJobHuntingMapper.updateById(gigJobHunting) > 0;
    }

    /**
     * 分页查询
     * 零工求职信息表的
     * 所有招聘信息
     *
     * @param curPage
     * @return 只返回status=审核成功的信息
     */
    @Override
    public Page<GigJobHunting> listReviewSuccess(Integer curPage, String keyword) {
        int cur = (curPage != null) ? curPage : 1;
        Page<GigJobHunting> page = new Page<>(cur, 10);
        return gigJobHuntingMapper.getPageByUserIdAndStatus(page, null, JobStatusConstant.REVIEW_SUCCESS, keyword);
    }

    /**
     * 分页查询 用户发布在
     * 零工求职信息表的招聘信息
     *
     * @param curPage
     * @param userId
     * @param status
     */
    @Override
    public Page<GigJobHunting> listByUserIdAndStatus(Integer curPage, Long userId, Integer status, String keyword) {
        int cur = (curPage != null) ? curPage : 1;
        Page<GigJobHunting> page = new Page<>(cur, 10);
        return gigJobHuntingMapper.getPageByUserIdAndStatus(page, userId, status, keyword);
    }

    /**
     * 发布零工求职信息
     *
     * @param jobAddDTO
     * @return
     */
    @Override
    public boolean publish(JobAddDTO jobAddDTO) {
        GigJobHunting gigJobHunting = jobAddDTO.getJobHuntingInformation();
        gigJobHunting.setPublishTime(LocalDateTime.now());
        gigJobHunting.setStatus(JobStatusConstant.REVIEW_PENDING);
        gigJobHunting.setUserId(jobAddDTO.getUserId());
        return gigJobHuntingMapper.insert(gigJobHunting) > 0;
    }

    /**
     * 根据信息状态 分页查询
     * 零工求职信息表
     *
     * @param currentPage
     * @param status
     */
    @Override
    public Page<GigJobHunting> listByStatus(Integer currentPage, Integer status, String keyword) {
        int cur = (currentPage != null) ? currentPage : 1;
        Page<GigJobHunting> page = new Page<>(cur, 10);
        return gigJobHuntingMapper.getPageByUserIdAndStatus(page, null, status, keyword);
    }

    /**
     * 更新招聘、求职信息内容
     *
     * @param jobAddDTO
     */
    @Override
    public boolean updateMsg(JobAddDTO jobAddDTO) {
        GigJobHunting gigJobHunting = jobAddDTO.getJobHuntingInformation();
        GigJobHunting old = gigJobHuntingMapper.selectById(gigJobHunting.getId());
        if (old == null) {
            throw new CustomException(ErrorCodeEnum.GIG_JOB_HUNTING_NOT_FOUND);
        }
        gigJobHunting.setStatus(JobStatusConstant.REVIEW_PENDING);
        // 查询理由表，如果存在拒接理由就删除
        Reason reason = reasonService.getReason(gigJobHunting.getId(), JobTypeConstant.GIG_JOB_HUNTING);
        if (reason != null) {
            reasonService.removeById(reason);
        }
        return gigJobHuntingMapper.updateById(gigJobHunting) > 0;
    }

    /**
     * 更新招聘表到达截止时间的信息的状态为已截止
     */
    @Override
    public void updateDeadlineStatus() {
        LambdaQueryWrapper<GigJobHunting> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GigJobHunting::getStatus, JobStatusConstant.REVIEW_SUCCESS);
        // 获取招聘信息的截止时间
        List<GigJobHunting> gigJobHuntingList = gigJobHuntingMapper.selectList(queryWrapper);
        // 到截止时间后,更新企业招聘信息表的状态为已下架
        for (GigJobHunting gigJobHunting : gigJobHuntingList) {
            if (gigJobHunting.getDeadline().isBefore(LocalDateTime.now())) {
                gigJobHuntingMapper.updateStatusToDeadline(gigJobHunting.getId());
            }
        }
    }

    /**
     * 根据id 获取招聘信息详情
     *
     * @param id
     */
    @Override
    public GigJobHunting getDetailsById(Long id) {
        GigJobHunting gigJobHunting = gigJobHuntingMapper.selectById(id);
        if (gigJobHunting == null) {
            throw new CustomException(ErrorCodeEnum.GIG_JOB_HUNTING_NOT_FOUND);
        } else {
            return gigJobHunting;
        }
    }

    /**
     * 根据id 获取ReviewPushDTO
     *
     * @param id
     */
    @Override
    public ReviewPushDTO getReviewPushById(Long id) {
        GigJobHunting gigJobHunting = getDetailsById(id);
        return new ReviewPushDTO()
                .setUserId(gigJobHunting.getUserId().toString())
                .setContent(gigJobHunting.getTitle())
                .setNotes(gigJobHunting.getTitle());
    }

    /**
     * 根据id 删除招聘信息
     *
     * @param id
     */
    @Override
    public boolean deleteInfo(Long id) {
        GigJobHunting gigJobHunting = gigJobHuntingMapper.selectById(id);
        if (gigJobHunting == null) {
            throw new CustomException(ErrorCodeEnum.GIG_JOB_HUNTING_NOT_FOUND);
        }
        return gigJobHuntingMapper.deleteById(id) > 0;
    }

    /**
     * 根据用户id删除零工求职信息
     *
     * @param userId
     * @return
     */
    @Override
    public boolean removeByUserId(Long userId) {
        return gigJobHuntingMapper.deleteByUserId(userId);
    }
}
