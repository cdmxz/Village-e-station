package com.ces.Village.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.Village.pojo.entity.GigJobHunting;

/**
 * <p>
 * 零工求职信息表 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
public interface GigHuntingJobService extends IService<GigJobHunting>, BaseJobService<GigJobHunting> {

    /**
     * 根据用户id删除零工求职信息
     * @param userId
     * @return
     */
    boolean removeByUserId(Long userId);
}
