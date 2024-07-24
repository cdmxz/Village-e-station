package com.ces.Village.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.annotation.LoginRequired;
import com.ces.Village.common.BaseContext;
import com.ces.Village.common.CurrentUser;
import com.ces.Village.common.R;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.constant.UserTypeConstant;
import com.ces.Village.pojo.dto.AdminLoginDTO;
import com.ces.Village.pojo.dto.AdminRegisterDTO;
import com.ces.Village.pojo.dto.ResetPwdDTO;
import com.ces.Village.pojo.dto.SMSDTO;
import com.ces.Village.pojo.entity.Admin;
import com.ces.Village.pojo.entity.Users;
import com.ces.Village.pojo.vo.LoginVO;
import com.ces.Village.pojo.vo.UserInformationVO;
import com.ces.Village.redis.SMSObject;
import com.ces.Village.service.AdminService;
import com.ces.Village.service.LoginService;
import com.ces.Village.service.UsersService;
import com.ces.Village.utils.AliSMSUtil;
import com.ces.Village.utils.ConvertUtil;
import com.ces.Village.utils.ValidateCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 管理员接口
 */
@Log4j2
@Api(tags = "管理员接口")
@RestController
@SessionAttributes("code")
@RequestMapping("/api/user/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private LoginService loginService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private AliSMSUtil aliSMSUtil;


    /**
     * 查询用户列表
     *
     * @param currentPage
     * @param type        用户类型
     * @return
     */
    @LoginRequired(requireAdmin = true)
    @GetMapping("/users")
    public R<?> getUsers(@RequestParam("page") Integer currentPage,
                         @RequestParam(value = "type", required = false) Integer type,
                         @RequestParam(value = "keyword", required = false) String keyword) {
        if (!UserTypeConstant.isValid(type)) {
            return R.error(ErrorCodeEnum.PARAM_IS_INVALID);
        }
        List<UserInformationVO> voList = new ArrayList<>();
        long pages = 0;// 查询总页数
        long total = 0;// 查询总记录条数
        // 查询用户表
        if (type == UserTypeConstant.USER) {
            Page<Users> page = usersService.getUserList(currentPage, keyword);
            pages = page.getPages();
            total = page.getTotal();
            for (Users users : page.getRecords()) {
                UserInformationVO vo = new UserInformationVO();
                vo.setAvatarUrl(users.getAvatarUrl());
                vo.setNickName(users.getNickName());
                vo.setName(users.getName());
                vo.setPhone(users.getPhone());
                voList.add(vo);
            }
        }
        // 查询管理员列表
        if (type == UserTypeConstant.ADMIN) {
            Page<Admin> page = adminService.getUserList(currentPage, keyword);
            pages = page.getPages();
            total = page.getTotal();
            for (Admin admin : page.getRecords()) {
                UserInformationVO vo = new UserInformationVO();
                vo.setAvatarUrl(admin.getAvatarUrl());
                vo.setNickName(admin.getNickName());
                vo.setName(admin.getName());
                vo.setPhone(admin.getPhone());
                voList.add(vo);
            }
        }
        // 构建响应数据
        Map<String, Object> info = new HashMap<>();
        info.put("list", voList);
        info.put("total", total);
        info.put("pagecount", pages);

        return R.success(info);
    }

    @PostMapping("/login")
    @ApiOperation("管理员用户端登陆")
    public R<LoginVO> login(HttpServletRequest request,
                            @RequestBody AdminLoginDTO adminLoginDTO) {
        log.info("管理员用户端登陆：{}", adminLoginDTO);
        // 校验验证码
        String captcha = adminLoginDTO.getCaptcha();
        String key = request.getRemoteHost();
        String codeInRedis = (String) redisTemplate.opsForValue().get(key);
        log.info("校验验证码，rediskey=" + key);
        if (codeInRedis == null || !codeInRedis.equalsIgnoreCase(captcha)) {
            return R.error(ErrorCodeEnum.CAPTCHA_ERROR);
        }
        return adminService.login(adminLoginDTO);
    }

    /**
     * 生成验证码
     */
    @GetMapping(value = "/captcha")
    @ApiOperation("生成验证码")
    public void getCaptchaImg(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("image/png");//设置相应类型,告诉浏览器输出的内容为图片
            response.setHeader("Pragma", "No-cache");//设置响应头信息，告诉浏览器不要缓存此内容
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expire", 0);
            ValidateCodeUtils randomValidateCode = new ValidateCodeUtils();
            // 生成验证码 并 输出验证码图片到客户端
            String code = randomValidateCode.getRandomCodeImage(response);
            // 将生成的随机字符串保存到redis中
            String key = request.getRemoteHost();
            int expireTime = 120; // 设置验证码过期时间，单位为秒
            redisTemplate.opsForValue().set(key, code, expireTime, TimeUnit.SECONDS);
            log.info("生成验证码，rediskey=" + key);
        } catch (Exception e) {
            log.error("获取图片验证码失败", e);
        }
    }

    /**
     * 退出登陆
     *
     * @return
     */
    @LoginRequired
    @PostMapping("/logout")
    @ApiOperation("退出登陆")
    public R<String> logout(@RequestParam(value = HttpHeaders.AUTHORIZATION) String userToken) {
        return R.success();
    }

    /**
     * 管理员注册
     *
     * @return 返回jwt生成的token，用于前后端交互
     */
    @PostMapping("/register")
    public R<?> register(@Valid @RequestBody AdminRegisterDTO adminRegisterDTO) {
        Admin admin = adminService.getByPhone(adminRegisterDTO.getPhone());
        if (admin != null) {
            return R.error(ErrorCodeEnum.USER_HAS_EXISTED);
        }
        admin = ConvertUtil.dtoToEntity(adminRegisterDTO, Admin.class);
        boolean result = adminService.Register(admin);
        if (result) {
            String token = loginService.createAdminLoginToken(admin.getId());
            return R.success(new LoginVO(token));
        }
        log.error("register方法，保存用户到数据库失败");
        return R.error(ErrorCodeEnum.RESISTER_ERROR);
    }


    /**
     * 发送短信验证码
     */
    @PostMapping("/sms")
    public R<?> sms(@Valid @RequestBody SMSDTO smsdto) {
        long interval = 120000;// 两次短信发送间隔120秒（120000毫秒）
        String key = smsdto.getPhone() + "SMS";
        String phone = smsdto.getPhone();
        Admin admin = adminService.getByPhone(phone);
        if (admin == null) {
            return R.error(ErrorCodeEnum.USER_NOT_EXIST);
        }
        if (Boolean.TRUE.equals(redisTemplate.hasKey(key))) {
            // 判断短信发送时间间隔是否太短
            SMSObject smsObject = (SMSObject) redisTemplate.opsForValue().get(key);
            long current = System.currentTimeMillis();
            if (smsObject != null) {
                long limitation = smsObject.sendingTimeMillis + interval;
                if (current < limitation) {
                    return R.error(ErrorCodeEnum.SMS_INTERVAL_TOO_SHORT);
                }
            }
        }
        // 生成验证码
        String code = ValidateCodeUtils.generateValidateCode();
        int expireTime = 600; // 验证码过期时间，单位为秒
        if (!aliSMSUtil.sendSMS(phone, code)) {
            return R.error(ErrorCodeEnum.SMS_FAILED);
        }
        // 验证码存到redis
        SMSObject smsObject = SMSObject.builder()
                .code(code)
                .sendingTimeMillis(System.currentTimeMillis())
                .build();
        redisTemplate.opsForValue().set(key, smsObject, expireTime, TimeUnit.SECONDS);
        return R.success();
    }


    /**
     * 重置密码
     */
    @PostMapping("/resetpwd")
    public R<?> resetPwd(@Valid @RequestBody ResetPwdDTO resetPwdDTO) {
        String key = resetPwdDTO.getPhone() + "SMS";
        // 判断验证码是否存在redis中，不存在表示验证码过期
        if (Boolean.FALSE.equals(redisTemplate.hasKey(key))) {
            return R.error(ErrorCodeEnum.CAPTCHA_EXPIRED);
        }
        SMSObject smsObject = (SMSObject) redisTemplate.opsForValue().get(key);
        if (smsObject == null||
                !smsObject.getCode().equals(resetPwdDTO.getCode())) {
            return R.error(ErrorCodeEnum.CAPTCHA_ERROR);
        }
        // 从redis移除验证码
        redisTemplate.delete(key);
        Admin admin = adminService.getByPhone(resetPwdDTO.getPhone());
        if (admin == null) {
            return R.error(ErrorCodeEnum.USER_NOT_EXIST);
        }
        // 对传过来的密码生成md5
        String newPwd = DigestUtils.md5DigestAsHex(resetPwdDTO.getPassword().getBytes());
        admin.setPassword(newPwd);
        boolean result = adminService.updateById(admin);
        if (result) {
            return R.success();
        } else {
            return R.error(ErrorCodeEnum.UPDATE_FAILED);
        }
    }

}
