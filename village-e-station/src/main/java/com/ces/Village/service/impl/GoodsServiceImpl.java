package com.ces.village.service.impl;


import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.village.constant.CategoryIdConstant;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.constant.GoodStatusConstant;
import com.ces.village.exception.CustomException;
import com.ces.village.mapper.GoodsMapper;
import com.ces.village.pojo.dto.GoodsDTO;
import com.ces.village.pojo.entity.Goods;
import com.ces.village.pojo.vo.GoodsDetailVO;
import com.ces.village.pojo.vo.SurplusVO;
import com.ces.village.service.GoodsService;
import com.ces.village.utils.ConvertUtil;
import com.ces.village.utils.JsonConvertUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 商品信息表 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 查看商品详情
     *
     * @param goodsId
     * @return
     */
    public GoodsDetailVO detail(Long goodsId) {
        // 根据商品ID查询商品信息
        Goods goodsInfo = goodsMapper.getById(goodsId);
        if (goodsInfo == null) {
            throw new CustomException(ErrorCodeEnum.GOODS_NOT_FOUND);
        }

        // 构建商品VO对象
        GoodsDetailVO goodsDetailVO = ConvertUtil.entityToVo(goodsInfo, GoodsDetailVO.class);
        List<String> list = JsonConvertUtil.toJavaObject(goodsInfo.getRotationUrls(), List.class);
        goodsDetailVO.setRotationUrls(list);
//        goodsDetailVO.setSalesQuantity(goodsInfo.getSalesQuantity());

        return goodsDetailVO;
    }

    /**
     * 添加商品
     *
     * @param goodsDTO
     * @return
     */
    public boolean addGood(GoodsDTO goodsDTO) {
        String[] rotationUrls = goodsDTO.getRotationUrls();
        String jsonString = JSON.toJSONString(rotationUrls);

        Goods goods = new Goods();
        // 将商品DTO的属性值复制到商品对象中
        goodsDTO.setId(null);
        BeanUtils.copyProperties(goodsDTO, goods, "rotationUrls");
        goods.setRotationUrls(jsonString);
        // 插入
        return goodsMapper.insert(goods) > 0;
    }

    /**
     * 修改商品
     */
    public boolean updateInfo(GoodsDTO goodsDTO) {
        Goods old = goodsMapper.getById(goodsDTO.getId());
        if (old == null) {
            throw new CustomException(ErrorCodeEnum.GOODS_NOT_FOUND);
        }
        Goods goods = ConvertUtil.dtoToEntity(goodsDTO, Goods.class);
        String urlString = JsonConvertUtil.toJsonString(goodsDTO.getRotationUrls());
        goods.setRotationUrls(urlString);
        return goodsMapper.update(goods) > 0;
    }

    /**
     * 查询商品库存
     *
     * @param goodsId
     * @return
     */
    public List<SurplusVO> getsurplus(Long goodsId) {
        return goodsMapper.getsurplus(goodsId);
    }


    /**
     * 查询销售状态为 在售的商品
     *
     * @param currentPage 用于分页查询
     * @param keyword     关键字（商品名）
     * @param priceDesc   =1根据价格降序排序，=0升序排序
     * @param salesDesc   =1根据销量降序排序，=0升序排序
     * @return
     */
    public Page<Goods> getGoodsForSale(Integer currentPage,
                                       Long categoryId,
                                       String keyword,
                                       Integer priceDesc,
                                       Integer salesDesc) {

        return getGoods(currentPage, categoryId, GoodStatusConstant.FOR_SALE, keyword, priceDesc, salesDesc);
    }

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
    public Page<Goods> getGoods(Integer currentPage,
                                Long categoryId,
                                Integer status,
                                String keyword,
                                Integer priceDesc,
                                Integer salesDesc) {
        int curPage = 1;
        if (currentPage != null) {
            curPage = currentPage;
        }
        // 设置分页参数
        Page<Goods> page = new Page<>();
        page.setCurrent(curPage);
        // 构建查询参数
        LambdaQueryWrapper<Goods> queryWrapper = new LambdaQueryWrapper<>();
        // categoryId 和 status
        if (categoryId != null && categoryId != CategoryIdConstant.ALL) {
            queryWrapper.eq(Goods::getCategoryId, categoryId);
        }
        if (status != null) {
            queryWrapper.eq(Goods::getStatus, status);
        }
        // 关键字查询
        if (keyword != null && !keyword.isEmpty()) {
            queryWrapper.like(Goods::getName, keyword);
        }
        // 根据价格排序
        if (priceDesc != null) {
            if (priceDesc == 1) {
                queryWrapper.orderByDesc(Goods::getPrice);
            } else if (priceDesc == 0) {
                queryWrapper.orderByAsc(Goods::getPrice);
            }
        }
        // 根据总销量排序
        if (salesDesc != null) {
            if (salesDesc == 1) {
                queryWrapper.orderByDesc(Goods::getSalesQuantity);
            } else if (salesDesc == 0) {
                queryWrapper.orderByAsc(Goods::getSalesQuantity);
            }
        }
        return this.page(page, queryWrapper);
    }
}
