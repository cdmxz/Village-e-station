package com.ces.village.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.village.common.BaseContext;
import com.ces.village.common.CurrentUser;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.exception.CustomException;
import com.ces.village.mapper.ShoppingCartMapper;
import com.ces.village.pojo.entity.Goods;
import com.ces.village.pojo.entity.ShoppingCart;
import com.ces.village.pojo.vo.ShoppingCartGoodsVO;
import com.ces.village.service.GoodsService;
import com.ces.village.service.ShoppingCartService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 购物车 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-10-30
 */
@Service
@Log4j2
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private GoodsService goodsService;


    /**
     * 添加商品到购物车
     *
     * @param shoppingCartDTO
     */
    @Override
    public boolean addShoppingCart(ShoppingCart shoppingCartDTO) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Long userId = currentUser.getId();
        shoppingCartDTO.setUserId(userId);
        // 判断当前加入到购物车中的商品是否已经存在了
        ShoppingCart cart = shoppingCartMapper.getGood(shoppingCartDTO);
        boolean res;
        if (cart != null) {
            // 如果已经存在了，只需要将数量加一
            cart.setNumber(cart.getNumber() + 1);
            res = updategoodNumber(cart);
        } else {
            // 如果不存在，需要插入一条购物车数据
            Long goodsId = shoppingCartDTO.getGoodsId();
            Goods goods = goodsService.getById(goodsId);
            if (goods == null) {
                throw new CustomException(ErrorCodeEnum.GOODS_NOT_FOUND);
            }
            shoppingCartDTO.setGoodsId(goodsId);
            shoppingCartDTO.setNumber(1);
            shoppingCartDTO.setCreateTime(LocalDateTime.now());
            // 判断库存不足，当前数量大于库存
            int com = shoppingCartDTO.getNumber().compareTo(goods.getSurplus());
            if (com > 0) {
                throw new CustomException(ErrorCodeEnum.OUT_OF_STOCK);
            }
            res = shoppingCartMapper.insert(shoppingCartDTO) > 0;
        }
        return res;
    }

    /**
     * 清空购物车
     *
     * @param userId
     * @return
     */
    @Override
    public boolean deleteShoppingCart(Long userId) {
        return shoppingCartMapper.deleteShoppingCart(userId) > 0L;
    }

    /**
     * 删除购物车中的一件商品
     *
     * @param shoppingCart
     * @return
     */
    @Override
    public boolean deleteGood(ShoppingCart shoppingCart) {
        return shoppingCartMapper.deleteGoods(shoppingCart) > 0;
    }

    /**
     * 根据用户id删除购物车数据
     *
     * @param userId
     * @return
     */
    @Override
    public boolean removeByUserId(Long userId) {
        return shoppingCartMapper.deleteByUserId(userId);
    }

    /**
     * 修改购物车商品的数量
     *
     * @param shoppingCartDTO
     * @return
     */
    public boolean updategoodNumber(ShoppingCart shoppingCartDTO) {
        // 限制最大 100 件商品
        int result = shoppingCartDTO.getNumber().compareTo(100);
        if (result > 0) {
            throw new CustomException(ErrorCodeEnum.OVER_LIMIT);
        }
        Goods goods = goodsService.getById(shoppingCartDTO.getGoodsId());
        // 当前数量大于库存
        result = shoppingCartDTO.getNumber().compareTo(goods.getSurplus());
        if (result > 0) {
            throw new CustomException(ErrorCodeEnum.OUT_OF_STOCK);
        }
        return shoppingCartMapper.updateNumber(shoppingCartDTO) > 0;
    }


    /**
     * 查询购物车中该商品的数量
     *
     * @return
     */
    public int getGoodNumber(ShoppingCart shoppingCart) {
        shoppingCart = shoppingCartMapper.getGood(shoppingCart);
        if (shoppingCart == null) {
            throw new CustomException(ErrorCodeEnum.GOODS_NOT_FOUND);
        }
        return shoppingCart.getNumber();
    }

    /**
     * 查看购物车
     *
     * @return
     */
    public Page<ShoppingCartGoodsVO> showShoppingCart(Integer currentPage, ShoppingCart shoppingCart) {
        Page<ShoppingCartGoodsVO> page = new Page<>();
        page.setCurrent(currentPage);
        return shoppingCartMapper.getGoods(page, shoppingCart.getUserId());
    }


}
