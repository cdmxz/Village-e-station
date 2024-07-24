package com.ces.Village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.pojo.entity.EnterpriseHires;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 企业招聘信息表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Mapper
public interface EnterpriseHiresMapper extends BaseMapper<EnterpriseHires> {

    /**
     * 查询企业招聘信息表和理由表
     * 根据用户id和状态查询
     *
     * @param page 分页参数
     * @param userId 用户id
     * @param status 招聘状态
     * @param keyword 关键字：标题、企业名称
     * @return
     */
    Page<EnterpriseHires> getPageByUserIdAndStatus(Page<EnterpriseHires> page, @Param("user_id") Long userId, @Param("status") Integer status, @Param("keyword") String keyword);

    /**
     * 查询企业招聘信息（定时器）
     *
     * @return
     */
    @Select("select * from enterprise_hires")
    List<EnterpriseHires> selectAll();

    /**
     * 根据id修改招聘状态为已下架（定时器）
     *
     * @param id
     */
    @Update("update enterprise_hires set status = 4 where id = #{id};")
    void updateStatusToDeadline(Long id);

    /**
     * 根据用户id删除企业招聘信息
     * @param userId
     * @return
     */
    @Delete("delete from enterprise_hires where user_id = #{userId}")
    boolean deleteByUserId(Long userId);
}
