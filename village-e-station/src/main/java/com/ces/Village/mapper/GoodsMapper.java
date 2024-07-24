package com.ces.Village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ces.Village.pojo.entity.Goods;
import com.ces.Village.pojo.vo.SurplusVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 商品信息表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {

    /**
     * 根据id查询商品
     *
     * @param goodsId
     * @return
     */
    @Select("select * from goods where id = #{id}")
    Goods getById(Long goodsId);

    /**
     * 修改商品
     *
     * @param goods
     */
    int update(Goods goods);

    /**
     * 查询商品库存
     *
     * @param goodsId
     * @return
     */
    @Select("select id,surplus from goods where id = #{id};")
    List<SurplusVO> getsurplus(Long goodsId);
}
