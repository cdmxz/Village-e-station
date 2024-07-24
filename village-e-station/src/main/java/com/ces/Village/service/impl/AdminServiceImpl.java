package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.Village.common.BaseContext;
import com.ces.Village.common.CurrentUser;
import com.ces.Village.exception.CustomException;
import com.ces.Village.common.R;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.mapper.AdminMapper;
import com.ces.Village.pojo.dto.AdminLoginDTO;
import com.ces.Village.pojo.dto.UserInformationDTO;
import com.ces.Village.pojo.entity.Admin;
import com.ces.Village.pojo.vo.LoginVO;
import com.ces.Village.service.AdminService;
import com.ces.Village.service.OssService;
import com.ces.Village.utils.WxApiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import com.ces.Village.utils.StringUtils;

import java.time.LocalDateTime;

/**
 * <p>
 * 管理员信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-11
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private LoginServiceImpl loginService;

    @Autowired
    private OssService ossService;

    @Autowired
    private WxApiUtil wxApiUtil;

    /**
     * 管理员登陆
     *
     * @param adminLoginDTO
     * @return
     */
    public R<LoginVO> login(AdminLoginDTO adminLoginDTO) {
        String phone = adminLoginDTO.getPhone();
        String password = adminLoginDTO.getPassword();
        //1、根据 电话号码 查询数据库中
        Admin admin = this.getByPhone(phone);
        if (admin == null) {
            return R.error(ErrorCodeEnum.USER_LOGIN_ERROR);
        }
        // 密码比对
        // 对前端传过来的明文密码进行md5加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(admin.getPassword())) {
            // 密码错误
            return R.error(ErrorCodeEnum.USER_LOGIN_ERROR);
        }
        if (adminLoginDTO.getCode() != null && admin.getOpenId() == null) {
            // 写入openid
            try {
                String openId = wxApiUtil.code2OpenId(adminLoginDTO.getCode()).toString();
                admin.setOpenId(openId);
                adminMapper.updateById(admin);
            } catch (Exception e) {
                log.error("写入openid失败：" + e.getMessage());
            }
        }
        String token = loginService.createAdminLoginToken(admin.getId());
        return R.success(new LoginVO(token));
    }

    /**
     * 修改管理员信息
     */
    public boolean updateUserInfo(UserInformationDTO userDTO) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Admin users = adminMapper.selectById(currentUser.getId());
        // 如果手机号码被修改，判断手机号码是否存在重复
        if (!users.getPhone().equals(userDTO.getPhone())) {
            if (this.getByPhone(userDTO.getPhone()) != null) {
                throw new CustomException(ErrorCodeEnum.PHONE_HAS_EXISTED);
            }
        }
        users.setName(userDTO.getName());
        users.setNickName(userDTO.getNickName());
        users.setPhone(userDTO.getPhone());
        String password = userDTO.getPassword();
        // 生成密码的md5编码存到数据库
        if (!password.isEmpty()) {
            String md5 = DigestUtils.md5DigestAsHex(userDTO.getPassword().getBytes());
            users.setPassword(md5);
        }
        users.setAvatarUrl(userDTO.getAvatarUrl());
        users.setVillage(userDTO.getVillage());
        ossService.imgReviewAsync(userDTO.getAvatarUrl());
        // 更新用户信息
        return adminMapper.updateById(users) > 0;
    }

    /**
     * 分页查询管理员列表
     *
     * @param currentPage
     * @return
     */
    @Override
    public Page<Admin> getUserList(Integer currentPage, String keyword) {
        Page<Admin> page = new Page<>();
        page.setCurrent(currentPage);
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNotNull(Admin::getPassword);
        if (!StringUtils.isEmptyOrNull(keyword)) {
            // 查询关键字不为空，使用 OR 连接两个 LIKE 查询
            queryWrapper.and(w -> {
                w.or().like(Admin::getName, "%" + keyword + "%")// 匹配name字段
                        .or()
                        .like(Admin::getPhone, keyword + "%");// 匹配Phone字段
            });
        }
        return adminMapper.selectPage(page, queryWrapper);
    }

    /**
     * 通过电话查询管理员账号
     *
     * @return
     */
    @Override
    public Admin getByPhone(String phone) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getPhone, phone);
        return adminMapper.selectOne(queryWrapper);
    }

    /**
     * 管理员注册
     *
     * @param admin
     * @return
     */
    @Override
    public boolean Register(Admin admin) {
        admin.setCreateTime(LocalDateTime.now());
        // 对明文密码进行md5加密处理
        String password = DigestUtils.md5DigestAsHex(admin.getPassword().getBytes());
        admin.setPassword(password);
        ossService.imgReviewAsync(admin.getAvatarUrl());
        boolean result = adminMapper.insert(admin) > 0;
        return result;
    }

}
