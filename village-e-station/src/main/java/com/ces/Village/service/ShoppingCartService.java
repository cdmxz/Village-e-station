package com.ces.Village.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.pojo.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.Village.pojo.vo.ShoppingCartGoodsVO;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
public interface ShoppingCartService extends IService<ShoppingCart> {


    /**
     * 修改购物车中该商品的数量
     *
     * @param shoppingCartDTO
     * @return
     */
    boolean updategoodNumber(ShoppingCart shoppingCartDTO);

    /**
     * 查询购物车中该商品的数量
     *
     * @param shoppingCart
     * @return
     */
    int getGoodNumber(ShoppingCart shoppingCart);

    /**
     * 查看购物车
     *
     * @return
     */
    Page<ShoppingCartGoodsVO> showShoppingCart(Integer currentPage, ShoppingCart shoppingCart);

    /**
     * 添加购物车
     *
     * @param shoppingCart
     */
    boolean addShoppingCart(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     *
     * @return
     */
    boolean deleteShoppingCart(Long userId);

    /**
     * 删除购物车中的一件商品
     *
     * @param shoppingCart
     * @return
     */
    boolean deleteGood(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车数据
     * @param userId
     * @return
     */
    boolean removeByUserId(Long userId);
}
