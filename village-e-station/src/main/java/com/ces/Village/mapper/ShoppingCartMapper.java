package com.ces.village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.pojo.entity.ShoppingCart;
import com.ces.village.pojo.vo.ShoppingCartGoodsVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 购物车 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-10-30
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

    /**
     * 修改该用户的购物车中，该商品的数量
     *
     * @param shoppingCart
     * @return
     */
    @Update("update shopping_cart set number = #{number} where goods_id = #{goodsId} AND user_id = #{userId}")
    int updateNumber(ShoppingCart shoppingCart);

    /**
     * 删除该用户的购物车
     *
     * @param userId
     * @return
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    Long deleteShoppingCart(Long userId);

    /**
     * 从该用户的购物车中 删除指定商品
     *
     * @param shoppingCart
     */
    @Delete("delete from shopping_cart where goods_id = #{goodsId} AND user_id = ${userId}")
    int deleteGoods(ShoppingCart shoppingCart);

    /**
     * 分页查询 该用户的购物车中的商品列表
     *
     * @param userId
     * @return
     */
    @Select("select sc.goods_id,g.name,g.price,g.thumbnail_url,g.category_id," +
            "g.surplus,g.status,sc.number from goods g join shopping_cart sc on g.id = sc.goods_id where user_id = #{userId}")
    Page<ShoppingCartGoodsVO> getGoods(Page<ShoppingCartGoodsVO> page, Long userId);

    /**
     * 查询该用户购物车 中的指定商品
     *
     * @param shoppingCart
     * @return
     */
    @Select("select * from shopping_cart where goods_id = #{goodsId} and user_id = #{userId}")
    ShoppingCart getGood(ShoppingCart shoppingCart);

    /**
     * 根据用户id删除购物车数据
     * @param userId
     * @return
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    boolean deleteByUserId(Long userId);
}
