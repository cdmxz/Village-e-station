package com.ces.village.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.village.pojo.dto.GoodsDTO;
import com.ces.village.pojo.entity.Goods;
import com.ces.village.pojo.vo.GoodsDetailVO;
import com.ces.village.pojo.vo.SurplusVO;

import java.util.List;

/**
 * <p>
 * 商品信息表 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
public interface GoodsService extends IService<Goods> {

    /**
     * 查看商品详情
     *
     * @param goodsId
     */
    GoodsDetailVO detail(Long goodsId);

    /**
     * 添加商品
     *
     * @param goodsDTO
     * @return
     */
    boolean addGood(GoodsDTO goodsDTO);

    /**
     * 修改商品
     *
     * @param goods
     */
    boolean updateInfo(GoodsDTO goods);

    /**
     * 查询商品库存
     *
     * @param goodsId
     * @return
     */
    List<SurplusVO> getsurplus(Long goodsId);

    /**
     * 查询销售状态为 在售的商品
     *
     * @param currentPage 用于分页查询
     * @param keyword     关键字（商品名）
     * @param priceDesc   =1根据价格降序排序，=0升序排序
     * @param salesDesc   =1根据销量降序排序，=0升序排序
     * @return
     */
    Page<Goods> getGoodsForSale(Integer currentPage,
                                Long categoryId,
                                String keyword,
                                Integer priceDesc,
                                Integer salesDesc);

    /**
     * 查询所有商品
     *
     * @param currentPage 当前页 用于分页查询
     * @param categoryId  商品目录id
     * @param keyword     关键字（商品名）
     * @param priceDesc   =1根据价格降序排序，=0升序排序
     * @param salesDesc   =1根据销量降序排序，=0升序排序
     * @return
     */
    Page<Goods> getGoods(Integer currentPage,
                         Long categoryId,
                         Integer status,
                         String keyword,
                         Integer priceDesc,
                         Integer salesDesc);
}
