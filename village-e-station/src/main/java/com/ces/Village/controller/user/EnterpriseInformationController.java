package com.ces.village.controller.user;

import com.ces.village.annotation.LoginRequired;
import com.ces.village.common.BaseContext;
import com.ces.village.common.CurrentUser;
import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.pojo.entity.EnterpriseInformation;
import com.ces.village.service.EnterpriseInformationService;
import com.ces.village.service.OssService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 当前用户的企业信息
 */
@Log4j2
@Tag(name = "企业信息接口")
@RestController
@RequestMapping("/api/job/enterprise")
@RequiredArgsConstructor
public class EnterpriseInformationController {
    @Autowired
    private EnterpriseInformationService enterpriseInformationService;
    @Autowired
    private OssService ossService;

    /**
     * 增加当前用户的企业信息
     */
    @LoginRequired
    @PostMapping("/add")
    @Operation(summary = "增加当前用户的企业信息")
    public R<?> add(@RequestBody EnterpriseInformation informationDTO) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        EnterpriseInformation information = enterpriseInformationService.getByUserId(currentUser.getId());
        if (information != null) {
            return R.error(ErrorCodeEnum.ENTERPRISE_INFORMATION_HAS_EXISTED);
        }
        informationDTO.setUserId(currentUser.getId());
        // 审核图片
        ossService.imgReviewAsync(informationDTO.getAuthorizationLetterUrl());
        ossService.imgReviewAsync(informationDTO.getPhotoUrl());
        ossService.imgReviewAsync(informationDTO.getBusinessLicenseUrl());
        boolean result = enterpriseInformationService.save(informationDTO);
        if (!result)
            return R.error(ErrorCodeEnum.INSERT_FAILED);
        else
            return R.success();
    }

    /**
     * 查询当前用户的企业信息
     */
    @LoginRequired
    @GetMapping("/")
    @Operation(summary = "查询当前用户的企业信息")
    public R<?> query() {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        EnterpriseInformation information = enterpriseInformationService.getByUserId(currentUser.getId());
        if (information == null) {
            return R.error(ErrorCodeEnum.ENTERPRISE_INFORMATION_NOT_FOUND);
        }
        return R.success(information);
    }


    /**
     * 修改当前用户的企业信息
     */
    @LoginRequired
    @PutMapping("/update")
    @Operation(summary = "修改当前用户的企业信息")
    public R<?> update(@RequestBody EnterpriseInformation informationDTO) {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        EnterpriseInformation information = enterpriseInformationService.getById(informationDTO.getId());
        if (information == null) {
            return R.error(ErrorCodeEnum.ENTERPRISE_INFORMATION_NOT_FOUND);
        }
        if (!Objects.equals(currentUser.getId(), information.getUserId())) {
            return R.error(ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
        }
        // 审核图片
        ossService.imgReviewAsync(informationDTO.getAuthorizationLetterUrl());
        ossService.imgReviewAsync(informationDTO.getPhotoUrl());
        ossService.imgReviewAsync(informationDTO.getBusinessLicenseUrl());
        boolean r = enterpriseInformationService.updateById(informationDTO);
        if (r) {
            return R.success();
        }
        return R.error(ErrorCodeEnum.UPDATE_FAILED);
    }

    /**
     * 删除当前用户的企业信息
     */
    @LoginRequired
    @DeleteMapping("/delete")
    @Operation(summary = "删除当前用户的企业信息")
    public R<?> delete() {
        CurrentUser currentUser = BaseContext.getCurrentUser();
        EnterpriseInformation enterpriseInformation = enterpriseInformationService.getByUserId(currentUser.getId());
        if (enterpriseInformation == null)
            return R.error(ErrorCodeEnum.ENTERPRISE_INFORMATION_NOT_FOUND);
        if (!Objects.equals(enterpriseInformation.getUserId(), currentUser.getId())) {
            // 不是该用户的企业信息
            return R.error(ErrorCodeEnum.USER_NOT_HAVE_PERMISSION);
        }
        boolean result = enterpriseInformationService.deleteInfo(enterpriseInformation.getId());
        if (result)
            return R.success();
        else
            return R.error(ErrorCodeEnum.DELETE_FAILED);
    }
}
