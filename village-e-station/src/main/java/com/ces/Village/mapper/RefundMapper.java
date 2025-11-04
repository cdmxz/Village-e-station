package com.ces.village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ces.village.pojo.entity.Refund;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【refund(待退款表)】的数据库操作Mapper
* @createDate 2024-01-20 19:13:23
* @Entity com.ces.village.pojo.entity.Refund
*/
@Mapper
public interface RefundMapper extends BaseMapper<Refund> {

}




