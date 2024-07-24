package com.ces.Village.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.Village.pojo.entity.Article;

/**
 * @author Administrator
 * @description 针对表【article(文章表)】的数据库操作Service
 * @createDate 2023-11-15 12:29:19
 */
public interface ArticleService extends IService<Article> {

    /**
     * 分页查询 文章表
     *
     * @param currentPage
     * @param queryWrapper
     * @return
     */
    Page<Article> selectPageArticle(Integer currentPage, Wrapper<Article> queryWrapper);

    /**
     * 分页查询 文章表
     * 根据发布时间 降序排序
     *
     * @param currentPage
     * @param queryWrapper
     * @return
     */
    Page<Article> selectPageDescByCreateTime(Integer currentPage, LambdaQueryWrapper<Article> queryWrapper);

    /**
     * 分页查询 文章表
     * 根据发布时间 升序排序或降序排序
     *
     * @param currentPage
     * @param queryWrapper
     * @return
     */
    Page<Article> selectPageOrderByCreateTime(Integer currentPage, LambdaQueryWrapper<Article> queryWrapper, boolean desc);

    /**
     * 根据用户id删除文章信息
     * @param userId
     * @return
     */
    boolean removeByUserId(Long userId);

}
