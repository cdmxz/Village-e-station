package com.ces.village.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.BaseContext;
import com.ces.village.common.CurrentUser;
import com.ces.village.common.R;
import com.ces.village.common.UserFactory;
import com.ces.village.constant.CommentStatusConstant;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.dto.CommentDTO;
import com.ces.village.pojo.dto.CommentStatusDTO;
import com.ces.village.pojo.entity.BaseUser;
import com.ces.village.pojo.entity.Comments;
import com.ces.village.pojo.vo.CommentListVo;
import com.ces.village.pojo.vo.CommentsStatusVo;
import com.ces.village.pojo.vo.CommentsVo;
import com.ces.village.pojo.vo.MyCommentsVo;
import com.ces.village.properties.JwtProperties;
import com.ces.village.service.CommentsService;
import com.ces.village.service.OssService;
import com.ces.village.utils.JsonConvertUtil;
import com.ces.village.utils.JwtUtil;
import com.ces.village.utils.StringUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 评论
 */
@Log4j2
@Tag(name = "评论接口")
@RestController
@RequestMapping("/api/article/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentsService commentsService;
    private final JwtProperties jwtProperties;
    private final OssService ossService;
    private final UserFactory userFactory;

    /**
     * 删除评论
     */
    @LoginRequired()
    @DeleteMapping("/delete")
    @Operation(summary = "删除评论")
    public R<?> deleteComment(@RequestParam("comment_id") String commentId) {
        log.info("删除评论，id={}", commentId);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Comments comment = commentsService.getById(commentId);
        if (comment == null) {
            return R.error(ErrorCodeEnum.COMMENT_NOT_FOUND);
        }
        // 如果当前用户是管理员，或当前用户不是管理员，但是评论是该用户发布的
        // 允许删除
        if (currentUser.isAdmin()
                || Objects.equals(comment.getUserId(), currentUser.getId())) {
            // 删除评论时，从oss删除图片
            ossService.deleteByJsonAsync(comment.getImgUrls());
            boolean result = commentsService.removeById(comment);

            if (!result)
                return R.error(ErrorCodeEnum.DELETE_FAILED);
            else
                return R.success();
        }
        return R.error(ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
    }


    /**
     * 查询一篇文章的评论列表
     * 只返回审核成功的评论
     *
     * @param articleId 文章id
     */
    @Operation(summary  = "查询一篇文章的评论列表")
    @GetMapping(value = "/list")
    public R<CommentListVo<CommentsVo>> getCommentList(
            @RequestParam(value = "page", required = false) Integer currentPage,
            @RequestParam(value = "desc", required = false) Integer descNum,
            @RequestParam("article_id") String articleId,
            @RequestHeader(value = "User-Token", required = false) String userToken) {
        log.info("查询评论列表，articleId={}，currentPage={}，descNum={}", articleId, currentPage, descNum);
        // 查询评论表和用户表
        CurrentUser currentUser = BaseContext.getCurrentUser();
        if (currentUser == null && !StringUtils.isEmptyOrNull(userToken)) {
            currentUser = JwtUtil.parseJWTToCurUser(jwtProperties, userToken);
        }
        boolean desc = (descNum != null && descNum == 1);
        Page<Comments> page = commentsService.getCommentListPage(articleId, currentPage, desc);
        List<CommentsVo> list = new ArrayList<>();
        for (Comments c : page.getRecords()) {
            CommentsVo vo = new CommentsVo();
            vo.setId(c.getId())
                    .setComment(c.getComment())
                    .setUser(c.getUser().getNickName())
                    .setAvatarUrl(c.getUser().getAvatarUrl())
                    .setDateCreated(c.getDateCreated());
            int isMyPublish = 0;
            if (currentUser != null && Objects.equals(c.getUserId(), currentUser.getId())) {
                isMyPublish = 1;
            }
            vo.setIsMyPublish(isMyPublish);
            List<String> urls = JsonConvertUtil.toJavaObject(c.getImgUrls(), List.class);
            vo.setImgUrls(urls);
            list.add(vo);
        }
        CommentListVo<CommentsVo> commentListVo = new CommentListVo<>(page.getPages(), list);
        return R.success(commentListVo);
    }

    /**
     * 查询我发布的评论列表
     */
    @LoginRequired
    @Operation(summary  = "查询我发布的评论列表")
    @GetMapping(value = "/mycomment")
    public R<CommentListVo<MyCommentsVo>> getMyCommentList(
            @RequestParam(value = "page", required = false) Integer currentPage,
            @RequestParam(value = "desc", required = false) Integer descNum,
            @RequestParam(value = "keyword", required = false) String keyword) {
        log.info("查询我发布的评论列表，currentPage={}，descNum={}", currentPage, descNum);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        BaseUser baseUser = userFactory.create(currentUser);
        String userName = baseUser.getName();
        String avatar = baseUser.getAvatarUrl();
        // 查询评论
        boolean desc = (descNum == null || descNum == 1);
        Page<Comments> page = commentsService.getMyCommentListPage(currentUser.getId().toString(), currentPage, desc);
        List<MyCommentsVo> list = new ArrayList<>();
        // 查询评论表和文章表
        for (Comments c : page.getRecords()) {
            MyCommentsVo vo = new MyCommentsVo();
            List<String> urls = JsonConvertUtil.toJavaObject(c.getImgUrls(), List.class);
            vo.setComment(c.getComment())
                    .setId(c.getId())
                    .setCommentDateCreated(c.getDateCreated())
                    .setArticleId(c.getArticleId().toString())
                    .setArticleTitle(c.getArticle().getTitle())
                    .setStatus(c.getStatus())
                    .setImgUrls(urls);
            list.add(vo);
        }
        CommentListVo<MyCommentsVo> commentListVo = new CommentListVo<>(page.getPages(), userName, avatar, list);
        return R.success(commentListVo);
    }


    /**
     * 发布评论
     */
    @LoginRequired
    @Operation(summary  = "发布评论")
    @PostMapping(value = "/publish")
    public R<?> publishComment(@Valid @RequestBody() CommentDTO commentDTO) {
        log.info("发布评论：{}", commentDTO);
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Comments comments = new Comments();
        String urlJson = JsonConvertUtil.toJsonString(commentDTO.getImgUrls());
        // 审核图片
        ossService.imgReviewByJsonAsync(urlJson);
        comments.setComment(commentDTO.getComment())
                .setArticleId(commentDTO.getArticleId())
                .setImgUrls(urlJson)
                // 查询用户
                .setUserId(currentUser.getId())
                .setDateCreated(LocalDateTime.now())
                // 设置状态为带审核
                .setStatus(CommentStatusConstant.REVIEW_PENDING);
        commentsService.save(comments);
        return R.success();
    }

    /**
     * 查询，整个数据库，状态为待审核的评论
     *
     */
    @LoginRequired(requireAdmin = true)
    @Operation(summary  = "查询，整个数据库，状态为待审核的评论")
    @GetMapping("/admin/review/list")
    public R<CommentListVo<CommentsStatusVo>> getReviewComment(
            @RequestParam(value = "page", required = false) Integer currentPage,
            @RequestParam(value = "desc", required = false) Integer descNum) {
        boolean desc = (descNum == null || descNum == 1);
        Page<Comments> page = commentsService.getCommentOfDbByStatus(currentPage, CommentStatusConstant.REVIEW_PENDING, desc);
        List<CommentsStatusVo> list = new ArrayList<>();
        for (Comments c : page.getRecords()) {
            CommentsStatusVo vo = new CommentsStatusVo();
            List<String> urls = JsonConvertUtil.toJavaObject(c.getImgUrls(), List.class);
            vo.setId(c.getId())
                    .setComment(c.getComment())
                    .setImgUrls(urls)
                    .setUser(c.getUser().getNickName())
                    .setAvatarUrl(c.getUser().getAvatarUrl())
                    .setDateCreated(c.getDateCreated())
                    .setArticleId(c.getArticle().getId().toString())
                    .setArticleTitle(c.getArticle().getTitle());
            list.add(vo);
        }
        CommentListVo<CommentsStatusVo> commentListVo = new CommentListVo<>(page.getPages(), list);
        return R.success(commentListVo);
    }

    /**
     * 审核评论
     *
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @Operation(summary  = "审核评论")
    @PostMapping("/admin/review")
    public R<?> review(@RequestBody CommentStatusDTO commentStatusDTO) {
        log.info("审核评论");
        // 判断状态是否合法
        if (!CommentStatusConstant.isValid(commentStatusDTO.getStatus()))
            return R.error(ErrorCodeEnum.PARAM_IS_INVALID);
        boolean result = commentsService.updateStatus(commentStatusDTO);
        if (!result) {
            return R.error(ErrorCodeEnum.UPDATE_FAILED);
        }
        return R.success();
    }
}
