package com.ces.Village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 用户类型 常量
 */
@Log4j2
public class UserTypeConstant {
    /**
     * 0 全部
     */
  //  public static final int ALL = 0;

    /**
     *  1 用户
     */
    public static final int USER = 1;
    /**
     * 2 管理员
     */
    public static final int ADMIN = 2;

    /**
     * 判断值是否合法
     *
     * @return
     */
    public static boolean isValid(int value) {
        Field[] fields = UserTypeConstant.class.getDeclaredFields();
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
                    log.error(UserTypeConstant.class.getName() + " 获取字段值失败", e);
                }
            }
        }
        return false;
    }
}
