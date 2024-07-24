package com.ces.Village.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ces.Village.common.BaseContext;
import com.ces.Village.pojo.vo.UserAddressVO;
import com.ces.Village.service.UserAddressService;
import com.ces.Village.mapper.UserAddressMapper;
import com.ces.Village.pojo.entity.UserAddress;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 地址管理 服务实现类
 * </p>
 *
 * @author author
 * @since 2023-10-30
 */
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Autowired
    private UserAddressMapper userAddressMapper;

    /**
     * 查询默认地址
     *
     * @return
     */
    public UserAddress getDefault(Long userId) {
        LambdaQueryWrapper<UserAddress> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserAddress::getUserId, userId)
                .and(s -> s.eq(UserAddress::getIsDefault, 1));
        return userAddressMapper.selectOne(queryWrapper);
    }

    /**
     * 查询所有地址列表
     *
     * @param userAddress
     * @return
     */
    public List<UserAddressVO> getList(UserAddress userAddress) {
        return userAddressMapper.getList(userAddress);
    }

    /**
     * 设置默认地址
     *
     * @param userAddress
     */
    public void setDefault(UserAddress userAddress) {
        Long userId = BaseContext.getCurrentUser().getId();
        UserAddress oldAddress = getDefault(userId);
        if (oldAddress != null) {
            // 将原来默认地址的isDefault设为0
            oldAddress.setIsDefault(0);
            userAddressMapper.update(oldAddress);
        }
        userAddressMapper.updateDefault(userAddress);
    }

    /**
     * 删除地址
     *
     * @param userAddress
     */
    public void delete(UserAddress userAddress) {
        userAddressMapper.delete(userAddress);
    }

    /**
     * 根据用户id删除地址
     * @param userId
     * @return
     */
    @Override
    public boolean removeByUserId(Long userId) {
        return userAddressMapper.deleteByUserId(userId);
    }
}
