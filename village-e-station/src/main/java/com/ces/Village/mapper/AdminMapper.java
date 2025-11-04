package com.ces.village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ces.village.pojo.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 管理员信息 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-11-11
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
