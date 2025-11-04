package com.ces.village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 商品目录id
 * 1,水果蔬菜
 * 2,油粮米面
 * 3,休闲零食
 * 4,禽畜肉蛋
 */
@Log4j2
public class CategoryIdConstant {

    public static final int ALL = 0;
    /**
     * 1,水果蔬菜
     */
    public static final int FRUITS_AND_VEGETABLES = 1;
    /**
     * 2,油粮米面
     */
    public static final int OIL_GRAIN_RICE_NOODLES = 2;
    /**
     * 3,休闲零食
     */
    public static final int CASUAL_SNACKS = 3;
    /**
     * 4,禽畜肉蛋
     */
    public static final int MEAT_AND_EGGS = 4;

    /**
     * 判断值是否合法
     *
     * @return
     */
    public static boolean isValid(int value) {
        Field[] fields = CategoryIdConstant.class.getDeclaredFields();
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
                    log.error(CategoryIdConstant.class.getName() + " 获取字段值失败", e);
                }
            }
        }
        return false;
    }
}
