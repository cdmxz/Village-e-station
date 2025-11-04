package com.ces.village.controller.user;

import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.entity.GoodsCategory;
import com.ces.village.service.GoodsCategoryService;
import com.ces.village.utils.StringUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@Tag(name = "商品分类接口")
@RestController
@RequestMapping("/api/good/category")
@RequiredArgsConstructor
public class GoodsCategoryController {
    @Autowired
    private GoodsCategoryService goodsCategoryService;


    /**
     * 删除商品分类
     */
    @LoginRequired(requireAdmin = true)
    @DeleteMapping("/delete")
    @Operation(summary = "删除商品分类")
    public R<?> delete(@RequestParam("category_id") String id) {
        log.info("删除商品分类，id={}", id);
        GoodsCategory goodsCategory = goodsCategoryService.getById(id);
        if (goodsCategory == null) {
            return R.error(ErrorCodeEnum.GOODS_CATEGORY_NOT_FOUND);
        }
        boolean result = goodsCategoryService.removeById(id);
        if (!result)
            return R.error(ErrorCodeEnum.DELETE_FAILED);
        else
            return R.success();
    }

    /**
     * 增加商品分类
     */
    @LoginRequired(requireAdmin = true)
    @PostMapping("/add")
    @Operation(summary = "增加商品分类")
    public R<?> add(@RequestBody GoodsCategory param) {
        log.info("增加商品分类，name={}", param.getName());
        if (StringUtils.isEmptyOrNull(param.getName())) {
            return R.error(ErrorCodeEnum.PARAM_IS_NULL);
        }
        GoodsCategory goodsCategory = goodsCategoryService.getByName(param.getName());
        if (goodsCategory != null) {
            return R.error(ErrorCodeEnum.GOODS_CATEGORY_HAS_EXISTED);
        }
        goodsCategory = new GoodsCategory();
        goodsCategory.setName(param.getName());
        boolean result = goodsCategoryService.save(goodsCategory);
        if (!result)
            return R.error(ErrorCodeEnum.INSERT_FAILED);
        else
            return R.success(goodsCategory);
    }

    /**
     * 查询商品分类
     */
    @GetMapping("/")
    @Operation(summary = "查询商品分类")
    public R<?> query() {
        List<GoodsCategory> list = goodsCategoryService.list();
        return R.success(list);
    }

    /**
     * 修改商品分类
     */
    @LoginRequired(requireAdmin = true)
    @PutMapping("/update")
    @Operation(summary = "修改商品分类")
    public R<?> update(@RequestBody GoodsCategory param) {
        log.info("增加商品分类，name={}", param.getName());
        if (StringUtils.isEmptyOrNull(param.getName())) {
            return R.error(ErrorCodeEnum.PARAM_IS_NULL);
        }
        GoodsCategory goodsCategory = goodsCategoryService.getById(param.getId());
        if (goodsCategory == null) {
            return R.error(ErrorCodeEnum.GOODS_CATEGORY_NOT_FOUND);
        }
        goodsCategory.setName(param.getName());
        boolean result = goodsCategoryService.updateById(goodsCategory);
        if (!result)
            return R.error(ErrorCodeEnum.INSERT_FAILED);
        else
            return R.success();
    }

}
