package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ces.Village.pojo.entity.GoodsCategory;
import com.ces.Village.mapper.GoodsCategoryMapper;
import com.ces.Village.service.GoodsCategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品分类 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
@Service
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {
    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    /**
     * 根据商品分类名，查询商品分类
     *
     * @param name
     * @return
     */
    @Override
    public GoodsCategory getByName(String name) {
        LambdaQueryWrapper<GoodsCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(GoodsCategory::getName, name);
        return goodsCategoryMapper.selectOne(queryWrapper);
    }
}
