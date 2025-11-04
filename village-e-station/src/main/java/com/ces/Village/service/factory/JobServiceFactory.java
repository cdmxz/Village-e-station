package com.ces.village.service.factory;

import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.constant.JobTypeConstant;
import com.ces.village.exception.CustomException;
import com.ces.village.service.BaseJobService;
import com.ces.village.service.EnterpriseHiresJobService;
import com.ces.village.service.GigHiresJobService;
import com.ces.village.service.GigHuntingJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 就业服务 简单工厂
 */
@Component
public class JobServiceFactory {

    @Autowired
    private EnterpriseHiresJobService enterpriseHiresJobService;
    @Autowired
    private GigHiresJobService gigHiresJobService;
    @Autowired
    private GigHuntingJobService gigHuntingJobService;

    /**
     * 根据不同的就业类型，创建不同招聘类的实例
     *
     * @param jobType
     * @return
     */
    public BaseJobService<?> create(Integer jobType) {
        return switch (jobType) {
            case JobTypeConstant.ENTERPRISE_HIRES -> enterpriseHiresJobService;
            case JobTypeConstant.GIG_HIRES -> gigHiresJobService;
            case JobTypeConstant.GIG_JOB_HUNTING -> gigHuntingJobService;
            default -> throw new CustomException(ErrorCodeEnum.JOB_TYPE_NOT_FOUND);
        };
    }
}
