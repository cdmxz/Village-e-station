package com.ces.village.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ces.village.pojo.entity.Comments;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【comments(问题评论表)】的数据库操作Mapper
 * @createDate 2023-11-15 12:39:16
 * @Entity
 */
@Mapper
public interface CommentsMapper extends BaseMapper<Comments> {

    /**
     * 查询一篇文章的评论列表，根据评论发布时间排序
     * 同时查询评论表和用户表
     *
     * 只返回审核成功的评论
     */
    IPage<Comments> getCommentListPage(IPage<Comments> iPage, String articleId, boolean desc);

    /**
     * 获取我发布的评论列表，根据评论发布时间排序
     * 同时查询评论表和文章表
     */
    IPage<Comments> getMyCommentListPage(IPage<Comments> iPage, String userId, boolean desc);

    /**
     * 根据状态查询，整个数据库，的评论列表
     *
     * @param iPage
     */
    IPage<Comments> getCommentOfDbByStatus(IPage<Comments> iPage, @Param("status") Integer status, boolean desc);

    /**
     * 根据用户id删除评论
     * @param userId
     * @return
     */
    @Delete("delete from comments where user_id = #{userId}")
    boolean deleteByUserId(Long userId);

    /**
     * 根据用户id查询评论信息
     * @param userId
     * @return
     */
    @Select("select img_urls from comments where user_id = #{userId}")
    List<String> getImgUrl(Long userId);
}




