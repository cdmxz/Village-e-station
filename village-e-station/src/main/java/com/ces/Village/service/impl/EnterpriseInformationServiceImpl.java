package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ces.Village.mapper.EnterpriseInformationMapper;
import com.ces.Village.pojo.entity.EnterpriseInformation;
import com.ces.Village.service.EnterpriseInformationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.Village.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 企业信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Service
public class EnterpriseInformationServiceImpl extends ServiceImpl<EnterpriseInformationMapper, EnterpriseInformation> implements EnterpriseInformationService {
    @Autowired
    private EnterpriseInformationMapper enterpriseInformationMapper;
    @Autowired
    private OssService ossService;

    /**
     * 获取企业信息根据用户id
     *
     * @param userId
     * @return
     */
    @Override
    public EnterpriseInformation getByUserId(Long userId) {
        LambdaQueryWrapper<EnterpriseInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseInformation::getUserId, userId);
        return enterpriseInformationMapper.selectOne(queryWrapper);
    }

    /**
     * 删除企业信息
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteInfo(Long id) {
        EnterpriseInformation information = enterpriseInformationMapper.selectById(id);
        return deleteByEntity(information);
    }

    /**
     * 删除企业信息根据用户id
     *
     * @param userId
     * @return
     */
    @Override
    public boolean removeByUserId(Long userId) {
        LambdaQueryWrapper<EnterpriseInformation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EnterpriseInformation::getUserId, userId);
        EnterpriseInformation info = enterpriseInformationMapper.selectOne(queryWrapper);
        return deleteByEntity(info);
    }

    /**
     * 删除企业信息
     * 必须最终调用此方法删除企业信息（此方法会删除oss图片）
     * @return
     */
    private boolean deleteByEntity(EnterpriseInformation info) {
        if (info == null)
            return false;
        // 从oss删除图片
        ossService.deleteAsync(info.getBusinessLicenseUrl());
        ossService.deleteAsync(info.getPhotoUrl());
        ossService.deleteAsync(info.getAuthorizationLetterUrl());
        return enterpriseInformationMapper.deleteById(info.getId()) > 0;
    }
}
