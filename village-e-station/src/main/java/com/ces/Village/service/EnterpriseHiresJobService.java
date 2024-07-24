package com.ces.Village.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.Village.pojo.entity.EnterpriseHires;

/**
 * <p>
 * 企业招聘信息表 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
public interface EnterpriseHiresJobService extends IService<EnterpriseHires>, BaseJobService<EnterpriseHires> {
    /**
     * 根据id删除企业招聘信息
     *
     * @return
     */
    boolean removeJobById(EnterpriseHires enterpriseHires);


    /**
     * 根据id删除企业招聘信息
     * @param userId
     * @return
     */
    boolean removeByUserId(Long userId);
}
