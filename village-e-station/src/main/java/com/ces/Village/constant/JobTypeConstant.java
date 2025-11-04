package com.ces.village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 工作类型常量
 */
@Log4j2
public class JobTypeConstant {
    /**
     * 企业招聘
     */
    public static final int ENTERPRISE_HIRES = 1;
    /**
     * 零工招聘
     */
    public static final int GIG_HIRES = 2;
    /**
     * 零工求职
     */
    public static final int GIG_JOB_HUNTING = 3;

    public static boolean isValid(Integer value) {
        Field[] fields = JobTypeConstant.class.getDeclaredFields();
        for (Field field : fields) {
            // 检查字段类型是否为int
            if (field.getType() == int.class) {
                // 设置访问权限
                field.setAccessible(true);
                try {
                    // 获取字段的值，静态字段，传入null
                    int fieldValue = field.getInt(null);
                    if (fieldValue == value)
                        return true;
                } catch (IllegalAccessException e) {
                    log.error("JobTypeConstant 获取字段值失败", e);
                }
            }
        }
        return false;
    }
}
