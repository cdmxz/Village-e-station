package com.ces.village.controller.user;


import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.BaseContext;
import com.ces.village.common.CurrentUser;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.entity.UserAddress;
import com.ces.village.pojo.vo.UserAddressVO;
import com.ces.village.service.UserAddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
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
@Tag(name = "移动端地址簿接口")
public class UserAddressController {
    @Autowired
    private UserAddressService userAddressService;

    /**
     * 新增地址
     */
    @LoginRequired
    @PostMapping("/add")
    @Operation(summary = "新增地址")
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
    @Operation(summary = "修改默认地址")
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
    @Operation(summary = "修改收货地址")
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
    @Operation(summary = "查询默认地址")
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
    @Operation(summary = "查询收货地址列表")
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
    @Operation(summary = "根据id删除地址")
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
