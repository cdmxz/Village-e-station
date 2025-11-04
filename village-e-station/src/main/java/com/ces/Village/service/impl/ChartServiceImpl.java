package com.ces.village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.village.mapper.ChartMapper;
import com.ces.village.pojo.entity.Chart;
import com.ces.village.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-12-21
 */
@Service
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService {

    @Autowired
    private ChartMapper chartMapper;

    /**
     * 获取轮播图列表，根据分类
     *
     * @param type
     * @return
     */
    @Override
    public List<Chart> getChartList(Integer type) {
        LambdaQueryWrapper<Chart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Chart::getType, type);
        return chartMapper.selectList(queryWrapper);
    }
}
