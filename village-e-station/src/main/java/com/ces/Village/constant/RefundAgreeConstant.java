package com.ces.village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 退款同意常量
 */
@Log4j2
public class RefundAgreeConstant {
    /**
     * 不同意
     */
    public static int DISAGREE = 0;
    /**
     * 同意
     */
    public static int AGREE = 1;

    /**
     * 判断值是否合法
     *
     * @return
     */
    public static boolean isValid(int value) {
        Field[] fields = RefundAgreeConstant.class.getDeclaredFields();
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
                    log.error(RefundAgreeConstant.class.getName() + " 获取字段值失败", e);
                }
            }
        }
        return false;
    }
}
