package com.ces.village.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.BaseContext;
import com.ces.village.common.CurrentUser;
import com.ces.village.common.R;
import com.ces.village.common.UserFactory;
import com.ces.village.constant.ArticleTypeConstant;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.dto.ArticleDTO;
import com.ces.village.pojo.entity.Article;
import com.ces.village.pojo.entity.BaseUser;
import com.ces.village.pojo.vo.ArticleListVo;
import com.ces.village.pojo.vo.ArticleVo;
import com.ces.village.service.ArticleService;
import com.ces.village.service.CommentsService;
import com.ces.village.service.OssService;
import com.ces.village.utils.ConvertUtil;


import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


/**
 * 文章
 */
@Log4j2
@Tag(name = "文章接口")
@RestController
@RequestMapping("/api/article")
// 使用构造器注入，代替字段注入
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CommentsService commentsService;
    private final UserFactory userFactory;
    private final OssService ossService;

    /**
     * DELETE /api/article/delete : 删除文章或问题根据文章id
     */
    @LoginRequired
    @Operation(summary  = "删除文章或问题根据文章id")
    @DeleteMapping(value = "/delete")
    public R<?> deleteArticle(@RequestParam("article_id") String articleId) {
        log.info("删除文章或问题根据文章id：{}", articleId);
        Article article = articleService.getById(articleId);
        if (article == null) {
            return R.error(ErrorCodeEnum.ARTICLE_NOT_FOUND);
        }
        // 当前用户是管理员，或者 该文章的作者是当前用户的
        // 赋予删除权限
        CurrentUser currentUser = BaseContext.getCurrentUser();
        if (currentUser.isAdmin() ||
                article.getAuthorId().equals(currentUser.getId())) {
            // 从oss删除文章图片
            ossService.deleteByRichAsync(article.getContent());
            boolean result = articleService.removeById(articleId);
            if (article.getArticleType() == ArticleTypeConstant.QUESTION_AND_ANSWER) {
                // 将该文章id对应的所有评论的文章id，设置为-1
                commentsService.setNegativeArticleId(articleId);
            }
            if (!result) {
                return R.error(ErrorCodeEnum.DELETE_FAILED);
            } else {
                return R.success();
            }
        }
        return R.error(ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
    }


    /**
     * GET /api/article/details : 查询文章或问题详情根据文章id
     * 问题相比文章要多返回一个评论数据
     */
    @Operation(summary  = "查询文章或问题详情根据文章id")
    @GetMapping(value = "/details")
    public R<Article> getArticleDetails(@RequestParam("article_id") String articleId) {
        log.info("查询文章或问题详情根据文章id：{}", articleId);
        // 查询文章详情
        Article article = articleService.getById(articleId);
        if (article == null) {
            return R.error(ErrorCodeEnum.ARTICLE_NOT_FOUND);
        }
        // 修改文章的阅读量+1
        article.setReadNumber(article.getReadNumber() + 1);
        boolean result = articleService.updateById(article);
        if (!result)
            return R.error(ErrorCodeEnum.DELETE_FAILED);
        else
            return R.success(article);
    }


    /**
     * GET /api/article/list : 查询所有文章和问题列表
     */
    @Operation(summary  = "查询所有文章和问题列表")
    @GetMapping(value = "/list")
    public R<ArticleListVo> getArticleList(
            @RequestParam(value = "page", required = false) Integer currentPage,
            @RequestParam(value = "desc", required = false) Integer descNum,
            @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("查询评论列表，currentPage={}，descNum={}", currentPage, descNum);
        // 排序
        // descNum=1，表示降序排序，否则为升序排序
        boolean desc = (descNum == null) || (descNum == 1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        if (!StringUtil.isNullOrEmpty(keyword)) {
            queryWrapper.like(Article::getTitle, keyword);
        }
        Page<Article> page = articleService.selectPageOrderByCreateTime(currentPage, queryWrapper, desc);
        List<Article> list = page.getRecords();
        // 设置IsMyPublish字段的值
        CurrentUser currentUser = BaseContext.getCurrentUser();
        for (Article article : list) {
            if ((currentUser != null) && Objects.equals(currentUser.getId(), article.getAuthorId()))
                article.setIsMyPublish(1);
            else
                article.setIsMyPublish(0);
        }
        // entity转vo
        List<ArticleVo> voList = ConvertUtil.entityToVoList(list, ArticleVo.class);
        ArticleListVo vo = new ArticleListVo(page.getPages(), voList);
        return R.success(vo);
    }


    /**
     * GET /api/article/listbytype : 查询文章和问题列表（根据文章分类）
     *
     * @param articleType 文章分类：1、乡村振兴2、特产介绍3、农业技术4、问题答疑 (required)
     * @param currentPage 第几页 (optional)
     */
    @Operation(summary  = "查询文章和问题列表（根据文章分类）")
    @GetMapping(value = "/listbytype")
    public R<ArticleListVo> getArticleListByType(
            @RequestParam("article_type") Integer articleType,
            @RequestParam(value = "desc", required = false) Integer descNum,
            @RequestParam(value = "page", required = false) Integer currentPage,
            @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("查询文章和问题列表（根据文章分类，articleType={}， currentPage={}， descNum={}", articleType, currentPage, descNum);
        // 排序
        // descNum=1，表示降序排序，否则为升序排序
        boolean desc = (descNum == null) || (descNum == 1);
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getArticleType, articleType);
        if (!StringUtil.isNullOrEmpty(keyword)) {
            queryWrapper.like(Article::getTitle, keyword);
        }
        Page<Article> page = articleService.selectPageOrderByCreateTime(currentPage, queryWrapper, desc);
        List<Article> list = page.getRecords();
        // 设置IsMyPublish字段的值
        CurrentUser currentUser = BaseContext.getCurrentUser();
        for (Article article : list) {
            if ((currentUser != null) && Objects.equals(currentUser.getId(), article.getAuthorId()))
                article.setIsMyPublish(1);
            else
                article.setIsMyPublish(0);
        }
        // entity转vo
        List<ArticleVo> voList = ConvertUtil.entityToVoList(list, ArticleVo.class);
        ArticleListVo vo = new ArticleListVo(page.getPages(), voList);
        return R.success(vo);
    }


    /**
     * GET /api/article/mypublish : 查询我发布的问题
     *
     * @param currentPage (optional)
     */
    @LoginRequired
    @Operation(summary  = "查询我发布的问题列表")
    @GetMapping(value = "/mypublish")
    public R<ArticleListVo> getArticleMyPublish(
            @RequestParam(value = "page", required = false) Integer currentPage,
            @RequestParam(value = "desc", required = false) Integer descNum,
            @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("查询我发布的问题列表，currentPage={}， descNum={}", currentPage, descNum);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Article::getAuthorId, currentUser.getId())
                .and(wq -> wq.eq(Article::getArticleType, ArticleTypeConstant.QUESTION_AND_ANSWER));
        if (!StringUtil.isNullOrEmpty(keyword)) {
            queryWrapper.like(Article::getTitle, keyword);
        }
        // descNum = null
        // 或
        // descNum=1
        // 表示降序排序，否则为升序排序
        boolean desc = (descNum == null) || (descNum == 1);
        Page<Article> page = articleService.selectPageOrderByCreateTime(currentPage, queryWrapper, desc);
        ArticleListVo articleListVo = ArticleListVo.createByEntity(page);
        return R.success(articleListVo);
    }


    /**
     * POST /api/article/publish : 发布文章（或问题）
     * 后台生成文章id
     */
    @LoginRequired
    @Operation(summary  = "发布文章（或问题）")
    @PostMapping(value = "/publish")
    public R<?> publishArticle(@RequestBody() ArticleDTO articleDTO) {
        log.info("发布文章（或问题）：{}", articleDTO);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        // 判断文章分类是否合法
        if (!ArticleTypeConstant.isValid(articleDTO.getArticleType()))
            return R.error(ErrorCodeEnum.PARAM_IS_INVALID);
        if (
            // 当前用户是管理员，赋予权限
                currentUser.isAdmin() ||
                        // 或者是普通用户，且发布的文章分类是问题，赋予权限
                        articleDTO.getArticleType().equals(ArticleTypeConstant.QUESTION_AND_ANSWER)) {
            Article article = ConvertUtil.dtoToEntity(articleDTO, Article.class);
            // 查询用户，为了获取当前用户用户名
            BaseUser baseUser = userFactory.create(currentUser);
            String userName = baseUser.getName();
            article.setDateCreated(LocalDateTime.now())
                    .setDateUpdated(LocalDateTime.now())
                    .setAuthorId(currentUser.getId())
                    .setAuthor(userName);
            boolean result = articleService.save(article);
            if (result) {
                // 审核图片
                ossService.imgReviewByRichAsync(article.getContent());
                return R.success();
            }
            return R.error(ErrorCodeEnum.INSERT_FAILED);
        } else
            return R.error(ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
    }

    /**
     * PUT /api/article/update : 修改文章（或问题）
     * <p>
     * 自己发布的问题（非文章），只有自己才能修改，其他人（包括管理员）不能修改
     */
    @LoginRequired
    @Operation(summary  = "修改文章（或问题）")
    @PutMapping(value = "/update")
    public R<?> updateArticle(@Valid @RequestBody() ArticleDTO articleDTO) {
        log.info("修改文章（或问题）：{}", articleDTO);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        // 判断文章分类是否合法
        if (!ArticleTypeConstant.isValid(articleDTO.getArticleType()))
            return R.error(ErrorCodeEnum.PARAM_IS_INVALID);
        // 判断文章是否存在
        Article article = articleService.getById(articleDTO.getId());
        if (article == null)
            return R.error(ErrorCodeEnum.ARTICLE_NOT_FOUND);

        // 如果文章不是该用户发布的，禁止修改
        if (!article.getAuthorId().equals(currentUser.getId())) {
            return R.error(ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
        }

        // 查询用户，获取当前用户名
        BaseUser baseUser = userFactory.create(currentUser);
        String userName = baseUser.getName();
        // 修改文章
        article.setTitle(articleDTO.getTitle())
                .setContent(articleDTO.getContent())
                .setThumbnailUrl(articleDTO.getThumbnailUrl())
                .setArticleType(articleDTO.getArticleType())
                .setDateUpdated(LocalDateTime.now())
                .setAuthor(userName) // 避免当前用户更改用户名
                .setSynopsis(article.getSynopsis());
        boolean result = articleService.updateById(article);
        if (result) {
            // 审核图片
            ossService.imgReviewByRichAsync(article.getContent());
            return R.success();
        }
        return R.error(ErrorCodeEnum.UPDATE_FAILED);
    }

}
