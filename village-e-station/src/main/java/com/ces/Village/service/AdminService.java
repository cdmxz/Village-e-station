package com.ces.Village.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ces.Village.common.R;
import com.ces.Village.pojo.dto.AdminLoginDTO;
import com.ces.Village.pojo.dto.UserInformationDTO;
import com.ces.Village.pojo.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.Village.pojo.vo.LoginVO;

/**
 * <p>
 * 管理员信息 服务类
 * </p>
 *
 * @author author
 * @since 2023-11-11
 */
public interface AdminService extends IService<Admin> {

    /**
     * 管理员登陆
     *
     * @param adminLoginDTO
     * @return
     */
    R<LoginVO> login(AdminLoginDTO adminLoginDTO);

    /**
     * 修改管理员信息
     */
    boolean updateUserInfo(UserInformationDTO userDTO);

    /**
     * 分页查询管理员列表
     *
     * @param currentPage
     * @return
     */
    Page<Admin> getUserList(Integer currentPage, String keyword);

    /**
     * 通过电话查询管理员账号
     *
     * @return
     */
    Admin getByPhone(String phone);

    /**
     * 管理员注册
     * @param admin
     * @return
     */
    boolean Register(Admin admin);
}
