package com.ces.village.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.village.pojo.entity.Chart;

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
