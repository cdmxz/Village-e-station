package com.ces.village.controller.user;

import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.BaseContext;
import com.ces.village.common.CurrentUser;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.dto.UserInformationDTO;
import com.ces.village.pojo.dto.UserLoginDTO;
import com.ces.village.pojo.dto.UserRegisterDTO;
import com.ces.village.pojo.dto.WxOpenIdResponse;
import com.ces.village.pojo.entity.Admin;
import com.ces.village.pojo.entity.Users;
import com.ces.village.pojo.vo.LoginVO;
import com.ces.village.pojo.vo.UserInformationVO;
import com.ces.village.properties.JwtProperties;
import com.ces.village.service.*;
import com.ces.village.utils.ConvertUtil;
import com.ces.village.utils.JwtUtil;
import io.netty.util.internal.StringUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户信息
 */
@Log4j2
@Tag(name = "用户接口")
@RestController
@RequestMapping("/api/user")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private OssService ossService;

    /**
     * 零工求职信息
     */
    @Autowired
    private GigHuntingJobService gigJobHuntingService;
    /**
     * 零工招聘信息
     */
    @Autowired
    private GigHiresJobService gigHiresService;
    /**
     * 企业招聘
     */
    @Autowired
    private EnterpriseHiresJobService enterpriseHiresService;

    /**
     * 企业信息表
     */
    @Autowired
    private EnterpriseInformationService enterpriseInformationService;

    /**
     * 文章
     */
    @Autowired
    private ArticleService articleService;
    /**
     * 评论
     */
    @Autowired
    private CommentsService commentsService;
    /**
     * 购物车
     */
    @Autowired
    private ShoppingCartService shoppingCartService;
    /**
     * 用户地址
     */
    @Autowired
    private UserAddressService userAddressService;


    /**
     * 查询用户信息
     *
     * @return
     */
    @LoginRequired
    @GetMapping("/information")
    public R<?> information() {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        UserInformationVO userInformationVO;
        // 获取当前用户的信息
        Long userId = currentUser.getId();
        if (currentUser.isAdmin()) {
            Admin admin = adminService.getById(userId);
            userInformationVO = ConvertUtil.entityToVo(admin, UserInformationVO.class);
            userInformationVO.setIsAdmin(1);
        } else {
            Users users = usersService.getById(userId);
            userInformationVO = ConvertUtil.entityToVo(users, UserInformationVO.class);
            userInformationVO.setIsAdmin(0);
        }
        return R.success(userInformationVO);
    }

    /**
     * 修改用户信息
     *
     * @return
     */
    @LoginRequired
    @PutMapping("/update")
    public R<?> update(@Valid @RequestBody UserInformationDTO userDTO) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        boolean result;
        if (currentUser.isAdmin()) {
            result = adminService.updateUserInfo(userDTO);
        } else {
            result = usersService.updateUserInfo(userDTO);
        }
        if (result)
            return R.success();
        else
            return R.error(ErrorCodeEnum.UPDATE_FAILED);
    }

    /**
     * 注销账号
     *
     * @return
     */
    @LoginRequired
    @PostMapping("/cancellation")
    public R<Users> cancellation() {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Long userId = currentUser.getId();
        //判断用户是否存在
        Users user = usersService.getById(userId);
        if (user == null) {
            log.error("用户不存在，用户id：{}", userId);
            return R.error(ErrorCodeEnum.USER_NOT_EXIST);
        }
        boolean comm = commentsService.removeByUserId(userId);
        if (!comm) {
            log.error("删除评论信息，用户id：{}失败", userId);
        }
        boolean article = articleService.removeByUserId(userId);
        if (!article) {
            log.error("删除文章信息，用户id：{}失败", userId);
        }
        boolean cart = shoppingCartService.removeByUserId(userId);
        if (!cart) {
            log.error("删除购物车数据，用户id：{}失败", userId);
        }
        boolean address = userAddressService.removeByUserId(userId);
        if (!address) {
            log.error("删除地址信息，用户id：{}失败", userId);
        }
        boolean gigJobHunting = gigJobHuntingService.removeByUserId(userId);
        if (!gigJobHunting) {
            log.error("零工求职信息删除，用户id：{}失败", userId);
        }
        boolean gigHires = gigHiresService.removeByUserId(userId);
        if (!gigHires) {
            log.error("零工招聘信息删除，用户id：{}失败", userId);
        }
        boolean enterpriseHires = enterpriseHiresService.removeByUserId(userId);
        if (!enterpriseHires) {
            log.error("企业招聘信息删除，用户id：{}失败", userId);
        }
        boolean enterpriseInformation = enterpriseInformationService.removeByUserId(userId);
        if (!enterpriseInformation) {
            log.error("企业信息删除，用户id：{}失败", userId);
        }
        boolean ret = usersService.removeById(userId);
        if (!ret) {
            log.error("注销账号：{}失败", userId);
        }
        return R.success();
    }

    /**
     * 退出登陆
     *
     * @return
     */
    @LoginRequired
    @PostMapping("/logout")
    @Operation(summary = "退出登陆")
    public R<String> logout(@RequestParam(value = HttpHeaders.AUTHORIZATION) String userToken) {

        return R.success();
    }


    /**
     * 判断token是否到期
     *
     * @return
     */
    @GetMapping("/expiration")
    public R<?> checkToken() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
            String token = request.getHeader(jwtProperties.getTokenName());
            if (!JwtUtil.isExpiration(jwtProperties.getSecretKey(), token)) {
                // token未到期，返回个新的token
                String newToken = loginService.refreshToken(token);
                Map<String, String> map = new HashMap<>();
                map.put("user_token", newToken);
                return R.success(map);
            } else
                return R.error(ErrorCodeEnum.TOKEN_IS_EXPIRATION);
        } catch (Exception e) {
            return R.error(ErrorCodeEnum.TOKEN_INVALID);
        }
    }

    /**
     * 普通用户注册
     *
     * @return 返回jwt生成的token，用于前后端交互
     */
    @PostMapping("/register")
    public R<?> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        Users user = ConvertUtil.dtoToEntity(userRegisterDTO, Users.class);
        // 获取openid
        WxOpenIdResponse wxOpenIdResponse = loginService.getWxOpenId(userRegisterDTO.getCode());
        if (wxOpenIdResponse.getErrCode() != 0) {
            // 获取openid失败
            return R.error(ErrorCodeEnum.OPENID_FAILED);
        }
        String openId = wxOpenIdResponse.getOpenid();
        if (usersService.getUserByOpenId(openId) != null) {
            // 用户已存在
            return R.error(ErrorCodeEnum.USER_HAS_EXISTED);
        }
        if (usersService.getUserByPhone(userRegisterDTO.getPhone()) != null) {
            return R.error(ErrorCodeEnum.PHONE_HAS_EXISTED);
        }
        user.setCreateTime(LocalDateTime.now());
        user.setOpenId(wxOpenIdResponse.getOpenid());
        ossService.imgReviewAsync(user.getAvatarUrl());
        boolean result = usersService.save(user);
        if (result) {
            String token = loginService.createUserLoginToken(user.getId());
            return R.success(new LoginVO(token));
        }
        log.error("register方法，保存用户到数据库失败");
        return R.error(ErrorCodeEnum.RESISTER_ERROR);
    }

    /**
     * 普通用户登录
     * 前端先调用登录接口，发送code给后端，获取openid，后端判断是否存在openid，
     * 如果不存在，返回错误信息，如果存在，则返回token给前端
     * 如果不存在，返回错误码，前端收到错误码后，调用注册接口，注册用户
     */
    @PostMapping("/login")
    public R<?> login(@RequestBody UserLoginDTO userLoginDTO) {
        String openid = userLoginDTO.getOpenid();
        String code = userLoginDTO.getCode();
        if (StringUtil.isNullOrEmpty(openid)) {
            // 如果openid为空，则通过code获取openid
            WxOpenIdResponse wxOpenIdResponse = loginService.getWxOpenId(code);
            if (wxOpenIdResponse.getErrCode() != 0) {
                return R.error(ErrorCodeEnum.OPENID_FAILED);
            }
            openid = wxOpenIdResponse.getOpenid();
        }
        // 通过openId查询用户
        Users user = usersService.getUserByOpenId(openid);
        if (user == null) {
            return R.error(ErrorCodeEnum.USER_NOT_EXIST);
        }
        String token = loginService.createUserLoginToken(user.getId());
        log.info("登陆成功...");
        return R.success(new LoginVO(token));
    }

}
