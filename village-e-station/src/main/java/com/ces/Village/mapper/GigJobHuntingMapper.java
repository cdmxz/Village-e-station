package com.ces.village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.pojo.entity.GigJobHunting;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 零工求职信息表 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-11-23
 */
@Mapper
public interface GigJobHuntingMapper extends BaseMapper<GigJobHunting> {

    /**
     * 查询零工求职表和理由表
     *
     * @param page 分页参数
     * @param userId 用户id
     * @param status 招聘状态
     * @param keyword 关键字：标题、企业名称
     * @return
     */
    Page<GigJobHunting> getPageByUserIdAndStatus(@Param("page") Page<GigJobHunting> page, @Param("user_id") Long userId, @Param("status") Integer status, @Param("keyword")String keyword);

    /**
     * 查询零工求职表和理由表
     * 根据用户id
     */
    @Select("SELECT g.*, r.reason FROM gig_job_hunting g LEFT JOIN reason r ON g.id =r.information_id WHERE user_id = #{userId} order by publish_time desc")
    Page<GigJobHunting> getPageByUserId(Page<GigJobHunting> page, Long userId);

    /**
     * 查询零工求职信息（定时器1）
     */
    @Select("select * from gig_job_hunting")
    List<GigJobHunting> selectAll();

    @Update("update gig_job_hunting set status = 4 where id = #{id};")
    void updateStatusToDeadline(Long id);

    /**
     * 根据用户id删除零工求职信息
     * @param userId
     * @return
     */
    @Delete("delete from gig_job_hunting where user_id = #{userId}")
    boolean deleteByUserId(Long userId);
}
