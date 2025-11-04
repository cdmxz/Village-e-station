package com.ces.village.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.exception.CustomException;
import com.ces.village.mapper.CommentsMapper;
import com.ces.village.pojo.dto.CommentStatusDTO;
import com.ces.village.pojo.entity.Comments;
import com.ces.village.service.CommentsService;
import com.ces.village.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Administrator
 * @description 针对表【comments(问题评论表)】的数据库操作Service实现
 * @createDate 2023-11-15 12:39:16
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
        implements CommentsService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private OssService ossService;


    @Override
    public Page<Comments> getCommentListPage(String articleId, Integer currentPage, boolean desc) {
        int curPage = currentPage == null ? 1 : currentPage;
        Page<Comments> page = new Page<>(curPage, 10L);
        commentsMapper.getCommentListPage(page, articleId, desc);
        return page;
    }

    @Override
    public Page<Comments> getMyCommentListPage(String userId, Integer currentPage, boolean desc) {
        int curPage = currentPage == null ? 1 : currentPage;
        Page<Comments> page = new Page<>(curPage, 10L);
        commentsMapper.getMyCommentListPage(page, userId, desc);
        return page;
    }

    /**
     * 删除该文章id对应的所有评论
     *
     * @param articleId
     * @return
     */
    @Override
    public boolean removeByArticleId(String articleId) {
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getArticleId, articleId);
        List<Comments> list = commentsMapper.selectList(queryWrapper);
        if (list.isEmpty()) {
            return false;
        }
        boolean result = false;
        for (Comments item : list) {
            // 删除评论图片
            ossService.deleteByJsonAsync(item.getImgUrls());
            result = commentsMapper.deleteById(item.getId()) > 0;
        }
        return result;
    }

    /**
     * 根据状态查询，整个数据库，的评论列表
     *
     * @param currentPage
     * @param desc
     * @return
     */
    @Override
    public Page<Comments> getCommentOfDbByStatus(Integer currentPage, Integer status, boolean desc) {
        int curPage = currentPage == null ? 1 : currentPage;
        Page<Comments> page = new Page<>(curPage, 10L);
        commentsMapper.getCommentOfDbByStatus(page, status, desc);
        return page;
    }

    /**
     * 修改评论状态
     *
     * @param commentStatusDTO
     */
    @Override
    public boolean updateStatus(CommentStatusDTO commentStatusDTO) {
        Comments comments = commentsMapper.selectById(commentStatusDTO.getCommentId());
        if (comments == null) {
            throw new CustomException(ErrorCodeEnum.COMMENT_NOT_FOUND);
        }
        // 设置状态
        comments.setStatus(commentStatusDTO.getStatus());
        return commentsMapper.updateById(comments) > 0;
    }

    /**
     * 根据用户id删除评论
     *
     * @param userId
     * @return
     */
    @Override
    public boolean removeByUserId(Long userId) {
        //获取文章缩略图url列表
        List<String> url = commentsMapper.getImgUrl(userId);

        //调用oss删除文件
        for (String urls : url) {
            ossService.deleteAsync(urls);
        }

        return commentsMapper.deleteByUserId(userId);
    }

    /**
     * 将该文章id对应的所有评论的文章id，设置为-1
     *
     * @param articleId
     */
    @Override
    public void setNegativeArticleId(String articleId) {
        LambdaQueryWrapper<Comments> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comments::getArticleId, articleId);
        List<Comments> list = commentsMapper.selectList(queryWrapper);
        for (Comments item : list) {
            item.setArticleId((long) -1);
            commentsMapper.updateById(item);
        }
    }
}




