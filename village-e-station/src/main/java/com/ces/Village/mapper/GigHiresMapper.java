package com.ces.village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.pojo.entity.GigHires;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 零工招聘信息表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Mapper
public interface GigHiresMapper extends BaseMapper<GigHires> {

    /**
     * 查询零工招聘列表和理由表
     *
     * @param page
     * @param userId
     * @param status
     * @return
     */
    Page<GigHires> getPageByUserIdAndStatus(Page<GigHires> page, @Param("user_id") Long userId, @Param("status") Integer status, @Param("keyword")String keyword);

    /**
     * 查询零工招聘列表和理由表
     * 根据用户id
     */
    @Select("SELECT g.*, r.reason FROM gig_hires g LEFT JOIN reason r ON g.id = r.information_id WHERE user_id = #{userId} order by publish_time desc")
    Page<GigHires> getPageByUserId(Page<GigHires> page, Long userId);

    /**
     * 查询零工招聘信息（定时器）
     * @return
     */
    @Select("select * from gig_hires")
    List<GigHires> selectAll();

    @Update("update gig_hires set status = 4 where id = #{id};")
    void updateStatusToDeadline(Long id);

    /**
     * 根据用户id删除零工招聘信息
     * @param userId
     * @return
     */
    @Delete("delete from gig_hires where user_id = #{userId}")
    boolean deleteByUserId(Long userId);
}
