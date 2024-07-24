package com.ces.Village.service.factory;

import com.ces.Village.constant.ErrorCodeEnum;
import com.ces.Village.constant.JobTypeConstant;
import com.ces.Village.exception.CustomException;
import com.ces.Village.service.BaseJobService;
import com.ces.Village.service.EnterpriseHiresJobService;
import com.ces.Village.service.GigHiresJobService;
import com.ces.Village.service.GigHuntingJobService;
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
