package com.ces.Village.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.Village.pojo.dto.CommentStatusDTO;
import com.ces.Village.pojo.entity.Comments;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @description 针对表【comments(问题评论表)】的数据库操作Service
 * @createDate 2023-11-15 12:39:16
 */
@Service
public interface CommentsService extends IService<Comments> {
    /**
     * 查询一篇文章的评论列表，根据评论发布时间排序
     * 同时查询评论表和用户表
     *
     * 只返回审核成功的评论
     */
    Page<Comments> getCommentListPage(String articleId, Integer currentPage, boolean desc);

    /**
     * 获取我发布的评论列表，根据评论发布时间排序
     * 同时查询评论表和文章表
     */
    Page<Comments> getMyCommentListPage(String userId, Integer currentPage, boolean desc);

    /**
     * 删除该文章id对应的所有评论
     *
     * @param articleId
     * @return
     */
    boolean removeByArticleId(String articleId);

    /**
     * 根据状态查询，整个数据库，的评论列表
     *
     * @return
     */
     Page<Comments> getCommentOfDbByStatus(Integer currentPage, Integer status, boolean desc);

    /**
     * 修改评论状态
     *
     * @param commentStatusDTO
     */
    boolean updateStatus(CommentStatusDTO commentStatusDTO);

    /**
     * 根据用户id删除评论
     * @param userId
     * @return
     */
    boolean removeByUserId(Long userId);

    /**
     * 将该文章id对应的所有评论的文章id，设置为-1
     * @param articleId
     */
    void setNegativeArticleId(String articleId);
}
