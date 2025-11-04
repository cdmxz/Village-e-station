package com.ces.village.controller.user;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.dto.GoodsDTO;
import com.ces.village.pojo.entity.Goods;
import com.ces.village.pojo.vo.GoodsDetailVO;
import com.ces.village.pojo.vo.GoodsListVO;
import com.ces.village.pojo.vo.SurplusVO;
import com.ces.village.service.GoodsService;
import com.ces.village.service.OssService;
import com.ces.village.utils.ConvertUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品信息表
 */
@Log4j2
@Tag(name = "商品接口")
@RestController
@RequestMapping("/api/good")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OssService ossService;

    /**
     * 添加商品
     *
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @PostMapping("/add")
    @Operation(summary = "添加商品")
    public R<Goods> add(@RequestBody GoodsDTO goodsDTO) {
        log.info("添加商品：{}", goodsDTO);
        goodsService.addGood(goodsDTO);
        return R.success();
    }

    /**
     * 删除商品
     *
     * @param goodsId
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @DeleteMapping("/delete")
    @Operation(summary = "删除商品")
    public R<Goods> delete(@RequestParam("good_id") Long goodsId) {
        log.info("删除商品成功...");
        Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            return R.error(ErrorCodeEnum.GOODS_NOT_FOUND);
        }
        goodsService.removeById(goodsId);
        ossService.deleteByRichAsync(goods.getDescription());
        ossService.deleteByJsonAsync(goods.getRotationUrls());
        return R.success();
    }

    /**
     * 修改商品
     *
     * @param goods
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @PutMapping("/update")
    @Operation(summary = "修改商品")
    public R<Goods> update(@RequestBody GoodsDTO goods) {
        log.info("修改商品：{}", goods);
        goodsService.updateInfo(goods);
        return R.success();
    }

    /**
     * 查询所有的商品数据 管理员
     *
     * @param currentPage 当前页 用于分页查询
     * @param keyword     关键字（商品名）
     * @param priceDesc   =1根据价格降序排序，=0升序排序
     * @param salesDesc   =1根据销量降序排序，=0升序排序
     * @return 返回所有状态的商品
     */
    @LoginRequired(requireAdmin = true)
    @GetMapping("/admin/list")
    @Operation(summary = "查询所有的商品数据 管理员")
    public R<?> listAd(@RequestParam("page") Integer currentPage,
                       @RequestParam(value = "keyword", required = false) String keyword,
                       @RequestParam(value = "pricedesc", required = false) Integer priceDesc,
                       @RequestParam(value = "salesdesc", required = false) Integer salesDesc) {
        // 调用goodsService的getGoods方法获取商品列表
        Page<Goods> page = goodsService.getGoods(currentPage, null,
                null, keyword, priceDesc, salesDesc);
        List<GoodsListVO> goodsListVO = ConvertUtil.entityToVoList(page.getRecords(), GoodsListVO.class);
        Map<String, Object> goodsInfo = new HashMap<>();
        goodsInfo.put("list", goodsListVO);
        goodsInfo.put("pagecount", page.getPages());
        return R.success(goodsInfo);
    }


    /**
     * 查询所有的商品数据 用户
     *
     * @param currentPage 当前页 用于分页查询
     * @param keyword     关键字（商品名）
     * @param priceDesc   =1根据价格降序排序，=0升序排序
     * @param salesDesc   =1根据销量降序排序，=0升序排序
     * @return 用户返回 销售状态的商品
     */
    @GetMapping("/list")
    @Operation(summary = "查询所有的商品数据")
    public R<?> list(@RequestParam("page") Integer currentPage,
                     @RequestParam(value = "keyword", required = false) String keyword,
                     @RequestParam(value = "pricedesc", required = false) Integer priceDesc,
                     @RequestParam(value = "salesdesc", required = false) Integer salesDesc) {
        // 调用goodsService的getGoods方法获取商品列表
        Page<Goods> page = goodsService.getGoodsForSale(currentPage, null,
                keyword, priceDesc, salesDesc);
        List<GoodsListVO> goodsListVO = ConvertUtil.entityToVoList(page.getRecords(), GoodsListVO.class);
        Map<String, Object> goodsInfo = new HashMap<>();
        goodsInfo.put("list", goodsListVO);
        goodsInfo.put("pagecount", page.getPages());
        return R.success(goodsInfo);
    }

    /**
     * 根据分类查询商品列表 管理员
     *
     * @param currentPage 当前页 用于分页查询
     * @param categoryId  目录id
     * @param keyword     关键字（商品名）
     * @param priceDesc   =1根据价格降序排序，=0升序排序
     * @param salesDesc   =1根据销量降序排序，=0升序排序
     * @return 返回所有状态的商品
     */
    @LoginRequired(requireAdmin = true)
    @GetMapping("/admin/listbytype")
    @Operation(summary = "根据分类查询商品列表 管理员")
    public R<Map<String, Object>> listByTypeAd(@RequestParam("page") Integer currentPage,
                                               @RequestParam("category_id") Long categoryId,
                                               @RequestParam(value = "keyword", required = false) String keyword,
                                               @RequestParam(value = "pricedesc", required = false) Integer priceDesc,
                                               @RequestParam(value = "salesdesc", required = false) Integer salesDesc) {
        // 调用goodsService的getGoods方法获取商品列表
        Page<Goods> page = goodsService.getGoods(currentPage, categoryId, null, keyword, priceDesc, salesDesc);
        List<GoodsListVO> goodsListVO = ConvertUtil.entityToVoList(page.getRecords(), GoodsListVO.class);
        Map<String, Object> goodsInfo = new HashMap<>();
        goodsInfo.put("list", goodsListVO);
        goodsInfo.put("pagecount", page.getPages());
        return R.success(goodsInfo);
    }

    /**
     * 根据分类查询商品列表 用户
     *
     * @param currentPage 当前页 用于分页查询
     * @param categoryId  目录id
     * @param keyword     关键字（商品名）
     * @param priceDesc   =1根据价格降序排序，=0升序排序
     * @param salesDesc   =1根据销量降序排序，=0升序排序
     * @return 返回所有状态的商品
     */
    @GetMapping("/listbytype")
    @Operation(summary = "根据分类查询商品列表 用户")
    public R<Map<String, Object>> listByType(@RequestParam("page") Integer currentPage,
                                             @RequestParam("category_id") Long categoryId,
                                             @RequestParam(value = "keyword", required = false) String keyword,
                                             @RequestParam(value = "pricedesc", required = false) Integer priceDesc,
                                             @RequestParam(value = "salesdesc", required = false) Integer salesDesc) {
        // 调用goodsService的getGoods方法获取商品列表
        Page<Goods> page = goodsService.getGoodsForSale(currentPage, categoryId, keyword, priceDesc, salesDesc);
        List<GoodsListVO> goodsListVO = ConvertUtil.entityToVoList(page.getRecords(), GoodsListVO.class);
        Map<String, Object> goodsInfo = new HashMap<>();
        goodsInfo.put("list", goodsListVO);
        goodsInfo.put("pagecount", page.getPages());
        return R.success(goodsInfo);
    }

    /**
     * 查看商品剩余库存
     *
     * @param
     * @return
     */
    @GetMapping("/surplus")
    @Operation(summary  = "查看商品剩余库存")
    public R<?> surplus(@RequestParam("good_id") Long goodsId) {
        List<SurplusVO> surplusVOList = goodsService.getsurplus(goodsId);
        return R.success(surplusVOList);
    }

    /**
     * 查看商品详情
     *
     * @param goodsId
     * @return
     */
    @GetMapping("/details")
    @Operation(summary = "查看商品详情")
    public R<GoodsDetailVO> details(@RequestParam("good_id") Long goodsId) {
        log.info("商品详情：{}", goodsId);
        GoodsDetailVO detail = goodsService.detail(goodsId);
        return R.success(detail);
    }
}
