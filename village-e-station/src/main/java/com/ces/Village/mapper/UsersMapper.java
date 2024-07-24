package com.ces.Village.mapper;

import com.ces.Village.pojo.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author author
 * @since 2023-11-03
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    /**
     * 通过openid查询用户
     * @param openId
     * @return
     */
    @Select("SELECT * FROM users WHERE openid = #{openId}")
    Users getByOpenId(String openId);

    /**
     * 修改用户信息
     * @param users
     */
    int updateUserInfo(Users users);

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    @Select("select name,nick_name,phone,id_number,avatar_url,village from users")
    List<Users> getUsersById(Long userId);


}
