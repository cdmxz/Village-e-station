package com.ces.Village.service;

import com.ces.Village.pojo.entity.Chart;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2023-12-21
 */
public interface ChartService extends IService<Chart> {
    /**
     * 获取轮播图列表，根据分类
     * @param type
     * @return
     */
    List<Chart> getChartList(Integer type);
}
