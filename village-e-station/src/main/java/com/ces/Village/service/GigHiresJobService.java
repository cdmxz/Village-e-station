package com.ces.Village.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.Village.pojo.entity.GigHires;

/**
 * <p>
 * 零工招聘信息表 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
public interface GigHiresJobService extends IService<GigHires>, BaseJobService<GigHires> {
    /**
     * 根据用户id删除零工招聘信息
     * @param userId
     * @return
     */
    boolean removeByUserId(Long userId);
}
