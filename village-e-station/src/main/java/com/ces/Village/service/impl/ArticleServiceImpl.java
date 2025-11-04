package com.ces.village.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.village.mapper.ArticleMapper;
import com.ces.village.pojo.entity.Article;
import com.ces.village.service.ArticleService;
import com.ces.village.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Administrator
 * @description 针对表【article(文章表)】的数据库操作Service实现
 * @createDate 2023-11-15 12:29:19
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private OssService ossService;

    @Override
    public Page<Article> selectPageArticle(Integer currentPage, Wrapper<Article> queryWrapper) {
        // 设置分页条件
        currentPage = (currentPage != null) ? currentPage : 0;
        Page<Article> articlePage = new Page<>();
        articlePage.setCurrent(currentPage);
        // 查询
        articleMapper.selectPage(articlePage, queryWrapper);
        return articlePage;
    }

    @Override
    public Page<Article> selectPageDescByCreateTime(Integer currentPage, LambdaQueryWrapper<Article> queryWrapper) {
        return selectPageOrderByCreateTime(currentPage, queryWrapper, true);
    }


    @Override
    public Page<Article> selectPageOrderByCreateTime(Integer currentPage, LambdaQueryWrapper<Article> queryWrapper, boolean desc) {
        if (desc)
            // 增加 降序排序条件
            queryWrapper.orderByDesc(Article::getDateCreated, Article::getDateUpdated);
        else
            // 增加 升序排序条件
            queryWrapper.orderByAsc(Article::getDateCreated, Article::getDateUpdated);
        return selectPageArticle(currentPage, queryWrapper);
    }

    /**
     * 根据用户id删除文章信息
     * @param userId
     * @return
     */
    @Override
    public boolean removeByUserId(Long userId) {
        //获取文章缩略图url列表
        List<String> thumbnailUrls = articleMapper.getThumbnailUrls(userId);

        //调用oss删除文件
        ossService.deleteBatchAsync(thumbnailUrls);

        return articleMapper.deleteByUserId(userId);
    }

}




