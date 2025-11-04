package com.ces.village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 招聘信息状态常量
 */
@Log4j2
public class JobStatusConstant {
    /**
     * 表示全部状态
     * 查询招聘信息列表接口 使用
     */
    public static final int ALL = 0;
    /**
     * 1、待审核
     */
    public static final int REVIEW_PENDING = 1;
    /**
     * 2、审核成功，展示中
     */
    public static final int REVIEW_SUCCESS = 2;
    /**
     * 3、审核失败，已打回
     */
    public static final int REVIEW_FAILURE = 3;
    /**
     * 4、到达截止时间，已下架
     */
    public static final int DEADLINE = 4;

    /**
     * 判断状态是否合法
     *
     * @return
     */
    public static boolean isValid(Integer value) {
        Field[] fields = JobStatusConstant.class.getDeclaredFields();
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
                    log.error("JobStatusConstant 获取字段值失败", e);
                }
            }
        }
        return false;
    }

    public static String toString(Integer status)
    {
        switch (status){
            case ALL -> {
                return "全部";
            }
            case REVIEW_PENDING -> {
                return "待审核";
            }
            case  REVIEW_SUCCESS-> {
                return "审核成功，展示中";
            }
            case  REVIEW_FAILURE-> {
                return "审核失败，已打回";
            }
            case  DEADLINE-> {
                return "到达截止时间，已下架";
            }
            default -> {
                return "unknown";
            }
        }
    }
}
