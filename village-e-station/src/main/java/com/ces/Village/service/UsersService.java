package com.ces.village.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.village.pojo.dto.UserInformationDTO;
import com.ces.village.pojo.entity.Users;

import java.io.Serializable;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
public interface UsersService extends IService<Users> {

    /**
     * 修改用户信息
     */
    boolean updateUserInfo(UserInformationDTO userDTO);


    /**
     * 通过openid查询用户
     *
     * @param openId
     * @return
     */
    Users getUserByOpenId(String openId);


    /**
     * 通过手机号查询用户
     *
     * @return
     */
    Users getUserByPhone(String phone);

    /**
     * 分页查询用户列表
     *
     * @param currentPage
     * @return
     */
    Page<Users> getUserList(Integer currentPage, String keyword);


    /**
     * 查询该用户的openid，查不到抛出异常
     * @param userId
     * @return
     */
    String userId2OpenId(Serializable userId) throws Exception;
}
