package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.common.BaseContext;
import com.ces.Village.common.CurrentUser;
import com.ces.Village.exception.CustomException;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.constant.JobStatusConstant;
import com.ces.Village.constant.JobTypeConstant;
import com.ces.Village.mapper.EnterpriseHiresMapper;
import com.ces.Village.pojo.dto.JobAddDTO;
import com.ces.Village.pojo.dto.JobDTO;
import com.ces.Village.pojo.dto.ReviewPushDTO;
import com.ces.Village.pojo.entity.*;
import com.ces.Village.service.EnterpriseHiresJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.Village.service.EnterpriseInformationService;
import com.ces.Village.service.OssService;
import lombok.extern.java.Log;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 企业招聘信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Service
public class EnterpriseHiresJobServiceImpl extends ServiceImpl<EnterpriseHiresMapper, EnterpriseHires> implements EnterpriseHiresJobService {

    @Autowired
    private EnterpriseHiresMapper enterpriseHiresMapper;
    @Autowired
    private EnterpriseInformationService enterpriseInformationService;
    @Autowired
    private ReasonServiceImpl reasonService;
    @Autowired
    private OssService ossService;

    /**
     * 修改企业招聘状态
     *
     * @param jobDTO
     */
    public boolean updateStatus(JobDTO jobDTO) {
        EnterpriseHires enterpriseHires = enterpriseHiresMapper.selectById(jobDTO.getInformationId());
        if (enterpriseHires == null) {
            throw new CustomException(ErrorCodeEnum.ENTERPRISE_HIRES_NOT_FOUND);
        }
        CurrentUser currentUser = BaseContext.getCurrentUser();
        // 当前用户不是管理员，这条消息也不是当前用户发布的
        if (!currentUser.isAdmin() &&
                !Objects.equals(currentUser.getId(), enterpriseHires.getUserId())) {
            throw new CustomException(ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
        }
        // 设置状态
        enterpriseHires.setStatus(jobDTO.getStatus());
        // 如果设置状态为拒绝，要把拒绝理由写进理由表
        if (enterpriseHires.getStatus() == JobStatusConstant.REVIEW_FAILURE) {
            if (StringUtils.isEmpty(jobDTO.getReason())) {
                throw new CustomException(ErrorCodeEnum.PARAM_IS_NULL);
            }
            Reason reason = new Reason();
            reason.setReason(jobDTO.getReason());
            reason.setType(jobDTO.getType());
            reason.setInformationId(enterpriseHires.getId());
            reasonService.save(reason);
        } else {
            // 设置为其他状态，查询理由表，如果存在拒接理由就删除
            Reason reason = reasonService.getReason(enterpriseHires.getId(), JobTypeConstant.ENTERPRISE_HIRES);
            if (reason != null) {
                reasonService.removeById(reason);
            }
        }
        int ret = enterpriseHiresMapper.updateById(enterpriseHires);
        return ret > 0;
    }

    /**
     * 分页查询
     * 企业招聘表的
     * 所有招聘信息
     *
     * @return 只返回status=审核成功的信息
     */
    @Override
    public Page<EnterpriseHires> listReviewSuccess(Integer curPage, String keyword) {
        int cur = (curPage != null) ? curPage : 1;
        Page<EnterpriseHires> page = new Page<>(cur, 10);
        return enterpriseHiresMapper.getPageByUserIdAndStatus(page, null, JobStatusConstant.REVIEW_SUCCESS, keyword);
    }

    /**
     * 分页查询 用户发布在
     * 企业招聘表的招聘信息
     *
     * @param currentPage
     * @param userId
     * @param status
     */
    @Override
    public Page<EnterpriseHires> listByUserIdAndStatus(Integer currentPage, Long userId, Integer status, String keyword) {
        if (currentPage == null)
            currentPage = 1;
        Page<EnterpriseHires> page = new Page<>(currentPage, 10);
        return enterpriseHiresMapper.getPageByUserIdAndStatus(page, userId, status, keyword);
    }

    /**
     * 发布企业招聘信息
     *
     * @param jobAddDTO
     * @return
     */
    @Override
    public boolean publish(JobAddDTO jobAddDTO) {
        EnterpriseHires enterpriseHires = jobAddDTO.getEnHiresInformation();
        enterpriseHires.setPublishTime(LocalDateTime.now());
        // 查询当前用户的企业信息，判断用户是否填写企业信息
        CurrentUser currentUser = BaseContext.getCurrentUser();
        EnterpriseInformation information = enterpriseInformationService.getByUserId(currentUser.getId());
        if (information == null) {
            throw new CustomException(ErrorCodeEnum.ENTERPRISE_INFORMATION_NOT_FOUND);
        }
        // 修改刚刚发布的招聘信息状态为待审核
        enterpriseHires.setStatus(JobStatusConstant.REVIEW_PENDING);
        enterpriseHires.setEnterpriseInformationId(information.getId());
        enterpriseHires.setUserId(jobAddDTO.getUserId());
        boolean result = enterpriseHiresMapper.insert(enterpriseHires) > 0;
        return result;
    }

    /**
     * 根据状态分页查询
     * 企业招聘表
     *
     * @param currentPage
     * @param status
     */
    @Override
    public Page<EnterpriseHires> listByStatus(Integer currentPage, Integer status, String keyword) {
        int cur = (currentPage != null) ? currentPage : 1;
        Page<EnterpriseHires> page = new Page<>(cur, 10);
        return enterpriseHiresMapper.getPageByUserIdAndStatus(page, null, status, keyword);
    }

    /**
     * 更新招聘、求职信息内容
     */
    @Override
    public boolean updateMsg(JobAddDTO jobAddDTO) {
        EnterpriseHires enterpriseHires = jobAddDTO.getEnHiresInformation();
        EnterpriseHires old = enterpriseHiresMapper.selectById(enterpriseHires.getId());
        if (old == null) {
            throw new CustomException(ErrorCodeEnum.ENTERPRISE_HIRES_NOT_FOUND);
        }
        // 修改招聘信息状态为待审核
        enterpriseHires.setStatus(JobStatusConstant.REVIEW_PENDING);
        boolean result = enterpriseHiresMapper.updateById(enterpriseHires) > 0;
        // 查询理由表，如果存在拒接理由就删除
        Reason reason = reasonService.getReason(enterpriseHires.getId(), JobTypeConstant.ENTERPRISE_HIRES);
        if (reason != null) {
            reasonService.removeById(reason);
        }
        return result;
    }

    /**
     * 根据id删除企业招聘信息
     *
     * @return
     */
    @Override
    public boolean removeJobById(EnterpriseHires enterpriseHires) {
        boolean result = enterpriseHiresMapper.deleteById(enterpriseHires.getId()) > 0;
        return result;
    }

    /**
     * 根据id 获取企业招聘信息详情
     *
     * @param id
     * @return
     */
    @Override
    public EnterpriseHires getDetailsById(Long id) {
        EnterpriseHires enterpriseHires = enterpriseHiresMapper.selectById(id);
        if (enterpriseHires == null) {
            throw new CustomException(ErrorCodeEnum.ENTERPRISE_HIRES_NOT_FOUND);
        }
        Long enterpriseInfoId = enterpriseHires.getEnterpriseInformationId();
        EnterpriseInformation information = enterpriseInformationService.getById(enterpriseInfoId);
        enterpriseHires.setEnterpriseInformation(information);
        return enterpriseHires;
    }

    /**
     * 根据id 获取招聘信息标题
     *
     * @param id
     */
    @Override
    public ReviewPushDTO getReviewPushById(Long id) {
        EnterpriseHires enterpriseHires = enterpriseHiresMapper.selectById(id);
        if (enterpriseHires == null) {
            throw new CustomException(ErrorCodeEnum.ENTERPRISE_HIRES_NOT_FOUND);
        }
        return new ReviewPushDTO().
                setUserId(enterpriseHires.getUserId().toString())
                .setContent(enterpriseHires.getTitle())
                .setNotes(enterpriseHires.getTitle());
    }

    /**
     * 根据id 删除招聘信息
     *
     * @param id
     */
    @Override
    public boolean deleteInfo(Long id) {
        EnterpriseHires enterpriseHires = enterpriseHiresMapper.selectById(id);
        if (enterpriseHires == null) {
            throw new CustomException(ErrorCodeEnum.ENTERPRISE_HIRES_NOT_FOUND);
        }
        return enterpriseHiresMapper.deleteById(id) > 0;
    }

    /**
     * 根据用户id
     * 删除企业招聘信息
     *
     * @param userId
     * @return
     */
    @Override
    public boolean removeByUserId(Long userId) {
        return enterpriseHiresMapper.deleteByUserId(userId);
    }

    /**
     * 更新招聘表到达截止时间的信息的状态为已截止
     */
    @Override
    public void updateDeadlineStatus() {
        LambdaQueryWrapper<EnterpriseHires> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseHires::getStatus, JobStatusConstant.REVIEW_SUCCESS);
        // 获取招聘信息的截止时间
        List<EnterpriseHires> enterpriseHires = enterpriseHiresMapper.selectList(queryWrapper);
        // 到截止时间后,更新企业招聘信息表的状态为已下架
        for (EnterpriseHires enterpriseHire : enterpriseHires) {
            if (enterpriseHire.getDeadline().isBefore(LocalDateTime.now())) {
                enterpriseHiresMapper.updateStatusToDeadline(enterpriseHire.getId());
            }
        }
    }
}
