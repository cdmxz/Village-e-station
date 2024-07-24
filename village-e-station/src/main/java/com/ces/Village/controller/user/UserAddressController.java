package com.ces.Village.controller.user;


import com.ces.Village.annotation.LoginRequired;
import com.ces.Village.common.BaseContext;
import com.ces.Village.common.CurrentUser;
import com.ces.Village.common.R;
import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.pojo.entity.UserAddress;
import com.ces.Village.pojo.vo.UserAddressVO;
import com.ces.Village.service.UserAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 地址管理
 * </p>
 *
 * @author author
 * @since 2023-10-30
 */
@Log4j2
@RestController
@RequestMapping("/api/address")
@Api(tags = "移动端地址簿接口")
public class UserAddressController {
    @Autowired
    private UserAddressService userAddressService;

    /**
     * 新增地址
     */
    @LoginRequired
    @PostMapping("/add")
    @ApiOperation("新增地址")
    public R<?> save(@RequestBody UserAddress userAddress) {
        log.info("新增地址");
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Long userId = currentUser.getId();
        userAddress.setUserId(userId);
        userAddressService.save(userAddress);
        Map<String, Object> vo = new HashMap<>();
        vo.put("address_id", userAddress.getId());
        return R.success(vo);
    }

    /**
     * 修改默认地址
     */
    @LoginRequired
    @PutMapping("/updatedefault")
    @ApiOperation("修改默认地址")
    public R<?> setDefault(@RequestBody UserAddress userAddress) {
        userAddressService.setDefault(userAddress);
        return R.success();
    }

    /**
     * 修改收货地址
     *
     * @param userAddress
     * @return
     */
    @LoginRequired
    @PutMapping("/updateaddress")
    @ApiOperation("修改收货地址")
    public R<?> update(@RequestBody UserAddress userAddress) {
        ////SQL:update user_address set  where id = ?
        userAddressService.updateById(userAddress);
        return R.success("地址修改成功");
    }

    /**
     * 查询默认地址
     */
    @LoginRequired
    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public R<?> getDefault() {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Long userId = currentUser.getId();
        UserAddress userAddress = userAddressService.getDefault(userId);
        log.info("查询默认地址:");
        if (null == userAddress) {
            return R.error(ErrorCodeEnum.ADDRESS_NOT_EXIST);
        } else {
            return R.success(userAddress);
        }
    }

    /**
     * 查询收货地址列表
     */
    @LoginRequired
    @GetMapping("/list")
    @ApiOperation("查询收货地址列表")
    public R<?> list(UserAddress userAddress) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        Long userId = currentUser.getId();
        userAddress.setUserId(userId);
        log.info("查询收货地址列表");
        List<UserAddressVO> userAddressVOList = userAddressService.getList(userAddress);

        return R.success(userAddressVOList);
    }

    /**
     * 删除地址
     *
     * @return
     */
    @LoginRequired
    @DeleteMapping("/delete")
    @ApiOperation("根据id删除地址")
    public R<UserAddress> deleteById(@RequestParam(value = "address_id") String addressId) {
        log.info("删除地址...");
        UserAddress address = userAddressService.getById(addressId);
        if (address == null) {
            return R.error(ErrorCodeEnum.ADDRESS_NOT_EXIST);
        }
        userAddressService.removeById(addressId);
        return R.success();
    }
}
