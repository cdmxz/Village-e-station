package com.ces.Village.service;

import com.ces.Village.pojo.entity.GoodsCategory;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 商品分类 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
public interface GoodsCategoryService extends IService<GoodsCategory> {

    /**
     * 根据商品分类名，查询商品分类
     *
     * @param name
     * @return
     */
    GoodsCategory getByName(String name);
}
