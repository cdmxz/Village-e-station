package com.ces.village.common;

import com.ces.village.pojo.entity.BaseUser;
import com.ces.village.service.AdminService;
import com.ces.village.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户工厂类
 */
@Component
public class UserFactory {
    @Autowired
    private AdminService adminService;
    @Autowired
    private UsersService usersService;

    /**
     * 根据不同的CurrentUser对象创建用户或管理员
     *
     * @param currentUser
     * @return
     */
    public BaseUser create(CurrentUser currentUser) {
        if (currentUser.isAdmin()) {
            return adminService.getById(currentUser.getId());
        } else {
            return usersService.getById(currentUser.getId());
        }
    }
}
