package com.ces.village.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.village.pojo.entity.EnterpriseInformation;

/**
 * <p>
 * 企业信息表 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
public interface EnterpriseInformationService extends IService<EnterpriseInformation> {

    /**
     * 删除企业信息
     *
     * @param id
     * @return
     */
    boolean deleteInfo(Long id);

    /**
     * 获取企业信息根据用户id
     *
     * @param userId
     * @return
     */
    EnterpriseInformation getByUserId(Long userId);

    /**
     * 删除企业信息根据用户id
     *
     * @param userId
     * @return
     */
    boolean removeByUserId(Long userId);
}
