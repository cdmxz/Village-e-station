package com.ces.Village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ces.Village.pojo.entity.Reason;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 审核失败的理由 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Mapper
public interface ReasonMapper extends BaseMapper<Reason> {

    /**
     * 根据信息id和类型获取理由
     * @param informationId
     * @param type
     * @return
     */
    @Select("SELECT * FROM reason WHERE information_id=#{informationId} AND type=#{type}")
    Reason getReason(Long informationId, Integer type);

}
