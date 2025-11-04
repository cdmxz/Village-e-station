package com.ces.village.controller.user;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.BaseContext;
import com.ces.village.common.CurrentUser;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.entity.ShoppingCart;
import com.ces.village.pojo.vo.ShoppingCartGoodsVO;
import com.ces.village.service.ShoppingCartService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 购物车
 */
@Log4j2
@RestController
@RequestMapping("/api/cart")
@Tag(name = "移动端购物车")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @LoginRequired
    @PostMapping("/goodadd")
    @Operation(summary = "添加到购物车")
    public R<?> add(@RequestBody ShoppingCart shoppingCart) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        shoppingCart.setUserId(currentUser.getId());
        shoppingCartService.addShoppingCart(shoppingCart);
        log.info("添加购物车...");
        return R.success();
    }

    /**
     * 查看购物车列表
     *
     * @return
     */
    @LoginRequired
    @GetMapping("/goodlist")
    @Operation(summary = "查看购物车的商品列表")
    public R<?> list(@RequestParam("page") Integer currentPage, ShoppingCart shoppingCart) {
        //获取到当前用户的id
        CurrentUser currentUser = BaseContext.getCurrentUser();
        shoppingCart.setUserId(currentUser.getId());
        Page<ShoppingCartGoodsVO> page = shoppingCartService.showShoppingCart(currentPage, shoppingCart);
        // 构建响应数据
        Map<String, Object> shoppingCatInfo = new HashMap<>();
        shoppingCatInfo.put("list", page.getRecords());
        shoppingCatInfo.put("pagecount", page.getPages());

        return R.success(shoppingCatInfo);
    }

    /**
     * 清空购物车
     *
     * @return
     */
    @LoginRequired
    @DeleteMapping("/clean")
    @Operation(summary = "清空购物车")
    public R<?> clean() {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        boolean result = shoppingCartService.deleteShoppingCart(currentUser.getId());
        if (!result) {
            return R.error(ErrorCodeEnum.DELETE_FAILED);
        }
        log.info("清空购物车:{}", result);
        return R.success();
    }

    /**
     * 删除商品
     *
     * @param goodsId
     * @return
     */
    @LoginRequired
    @DeleteMapping("/gooddelete")
    @Operation(summary = "删除购物车中的一件商品")
    public R<ShoppingCart> deleteGood(@RequestParam("good_id") Long goodsId) {
        log.info("删除购物车中的一件商品...");
        CurrentUser currentUser = BaseContext.getCurrentUser();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setGoodsId(goodsId);
        shoppingCart.setUserId(currentUser.getId());
        boolean res = shoppingCartService.deleteGood(shoppingCart);
        if (res)
            return R.success();
        else
            return R.error(ErrorCodeEnum.DELETE_FAILED);
    }

    /**
     * 修改购物车中该商品的数量
     *
     * @param shoppingCartDTO
     * @return
     */
    @LoginRequired
    @PutMapping("/update/goodnumber")
    @Operation(summary = "修改购物车中该商品的数量")
    public R<?> update(@RequestBody ShoppingCart shoppingCartDTO) {
        log.info("修改购物车中该商品的数量:{}", shoppingCartDTO);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        shoppingCartDTO.setUserId(currentUser.getId());
        boolean res = shoppingCartService.updategoodNumber(shoppingCartDTO);
        if (res)
            return R.success();
        else
            return R.error(ErrorCodeEnum.UPDATE_FAILED);
    }

    /**
     * 查询购物车中该商品的数量
     *
     * @param goodsId
     * @return
     */
    @LoginRequired
    @GetMapping("/goodnumber")
    @Operation(summary = "查询购物车中该商品的数量")
    public R<?> goodnumber(@RequestParam("good_id") Long goodsId) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        // 查询购物车中该商品的数量
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setGoodsId(goodsId);
        shoppingCart.setUserId(currentUser.getId());
        int number = shoppingCartService.getGoodNumber(shoppingCart);
        shoppingCart.setUserId(null);
        shoppingCart.setNumber(number);
        log.info("查询一种商品数量:{}", goodsId);
        return R.success(shoppingCart);
    }
}
