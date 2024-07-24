package com.ces.Village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 商品状态常量
 */
@Log4j2
public class GoodStatusConstant {
    /**
     * 在售
     */
    public static final int FOR_SALE = 1;

    /**
     * 下架
     */
    public static final int OFF_SHELF = 2;

    /**
     * 库存不足
     */
    public static final int OUT_OF_STOCK = 3;

    /**
     * 判断状态是否合法
     *
     * @return
     */
    public static boolean isValid(Integer value) {
        Field[] fields = GoodStatusConstant.class.getDeclaredFields();
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
                    log.error("GoodStatusConstant 获取字段值失败", e);
                }
            }
        }
        return false;
    }
}
