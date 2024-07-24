package com.ces.Village.service;

import com.ces.Village.pojo.entity.UserAddress;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ces.Village.pojo.vo.UserAddressVO;

import java.util.List;

/**
 * <p>
 * 地址管理 服务类
 * </p>
 *
 * @author author
 * @since 2023-10-30
 */
public interface UserAddressService extends IService<UserAddress> {


    /**
     * 查询默认地址
     * @return
     */
    UserAddress getDefault(Long userId);

    /**
     * 查询地址列表
     * @param userAddress
     * @return
     */
    List<UserAddressVO> getList(UserAddress userAddress);

    /**
     * 修改默认地址
     * @param userAddress
     * @return
     */
    void setDefault(UserAddress userAddress);

    /**
     * 删除地址
     * @param userAddress
     */
    void delete(UserAddress userAddress);

    /**
     * 根据用户id删除地址
     * @param userId
     * @return
     */
    boolean removeByUserId(Long userId);
}
