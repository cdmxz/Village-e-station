package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.common.BaseContext;
import com.ces.Village.common.CurrentUser;
import com.ces.Village.exception.CustomException;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.pojo.dto.UserInformationDTO;
import com.ces.Village.pojo.entity.Users;
import com.ces.Village.mapper.UsersMapper;
import com.ces.Village.service.OssService;
import com.ces.Village.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ces.Village.utils.StringUtils;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
@Log4j2
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {
    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private OssService ossService;

    /**
     * 修改用户信息
     */
    public boolean updateUserInfo(UserInformationDTO userDTO) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Users users = usersMapper.selectById(currentUser.getId());
        // 如果手机号码被修改，判断手机号码是否存在重复
        if (!users.getPhone().equals(userDTO.getPhone())) {
            if (this.getUserByPhone(userDTO.getPhone()) != null) {
                throw new CustomException(ErrorCodeEnum.PHONE_HAS_EXISTED);
            }
        }
        users.setName(userDTO.getName());
        users.setNickName(userDTO.getNickName());
        users.setPhone(userDTO.getPhone());
        users.setIdNumber(userDTO.getIdNumber());
        users.setAvatarUrl(userDTO.getAvatarUrl());
        users.setVillage(userDTO.getVillage());
        ossService.imgReviewAsync(userDTO.getAvatarUrl());
        // 更新用户信息
        return usersMapper.updateById(users) > 0;
    }


    /**
     * 通过openid查询用户
     *
     * @param openId
     * @return
     */
    @Override
    public Users getUserByOpenId(String openId) {
        return usersMapper.getByOpenId(openId);
    }

    /**
     * 通过手机号查询用户
     *
     * @param phone
     * @return
     */
    @Override
    public Users getUserByPhone(String phone) {
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Users::getPhone, phone);
        return usersMapper.selectOne(queryWrapper);
    }

    /**
     * 分页查询用户列表
     *
     * @param currentPage
     * @return
     */
    @Override
    public Page<Users> getUserList(Integer currentPage, String keyword) {
        Page<Users> page = new Page<>();
        page.setCurrent(currentPage);
        LambdaQueryWrapper<Users> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNotNull(Users::getOpenId);
        if (!StringUtils.isEmptyOrNull(keyword)) {
            // 使用 OR 连接两个 LIKE 查询
            queryWrapper.and(w -> w.or()
                    .like(Users::getName, "%" + keyword + "%")
                    .or()
                    .like(Users::getPhone, keyword + "%")
            );
        }
        return usersMapper.selectPage(page, queryWrapper);
    }

    /**
     * 查询该用户的openid，查不到抛出异常
     *
     * @param userId
     * @return
     */
    @Override
    public String userId2OpenId(Serializable userId) throws Exception {
        Users users = usersMapper.selectById(userId);
        if (users == null) {
            throw new Exception("userId:" + userId + "用户不存在");
        }
        if (users.getOpenId() == null) {
            throw new Exception("userId:" + userId + "该用户openid不存在");
        }
        return users.getOpenId();
    }
}
