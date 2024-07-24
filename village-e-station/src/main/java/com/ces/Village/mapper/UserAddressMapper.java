package com.ces.Village.mapper;

import com.ces.Village.pojo.entity.UserAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ces.Village.pojo.vo.AddressVO;
import com.ces.Village.pojo.vo.UserAddressVO;
import com.ces.Village.pojo.vo.UserDefaultVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <p>
 * 地址管理 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-10-30
 */
@Mapper
public interface UserAddressMapper extends BaseMapper<UserAddress> {
    /**
     * 修改地址
     * @param userAddress
     */
    void update(UserAddress userAddress);

    /**
     * 查询默认地址
     * @param userAddress
     * @return
     */
    @Select("select id,consignee,phone,province,city,district,detail from user_address where is_default = 1 and user_id = #{userId}")
    List<UserDefaultVO> getDefault(UserAddress userAddress);

    /**
     * 获取所有地址
     * @param userAddress
     * @return
     */
    @Select("select id,consignee,phone,province,city,district,detail,is_default from user_address where user_id = #{userId}")
    List<UserAddressVO> getList(UserAddress userAddress);

    /**
     * 修改默认地址
     * @param userAddress
     * @return
     */
    @Update("update user_address set is_default = 1 where id = #{id}")
    void updateDefault(UserAddress userAddress);

    /**
     * 获取修改后的默认地址信息
     * @param userAddress
     * @return
     */
    @Select("select consignee,phone,province,city,district,detail,is_default from user_address where id = #{id}")
    List<AddressVO> getUpdateDefault(UserAddress userAddress);

    /**
     * 删除地址
     * @param userAddress
     */
    @Delete("delete from user_address where id = #{id};")
    void delete(UserAddress userAddress);

    /**
     * 根据用户id删除地址
     * @param id
     * @return
     */
    @Delete("delete from user_address where user_id = #{UserId}")
    boolean deleteByUserId(Long id);
}
