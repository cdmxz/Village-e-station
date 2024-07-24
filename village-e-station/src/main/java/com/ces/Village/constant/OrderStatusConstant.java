package com.ces.Village.constant;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.After;

import java.lang.reflect.Field;

/**
 * 订单状态常量
 */
@Log4j2
public class OrderStatusConstant {

    /**
     * 0 所有状态
     */
    public static final int ALL = 0;

    /**
     * 1 待付款
     */
    public static final int UNPAID = 1;
    /**
     * 2 待发货
     */
    public static final int UNSEND = 2;
    /**
     * 3 待收货
     */
    public static final int UNRECEIVE = 3;
    /**
     * 4 已完成
     */
    public static final int COMPLETE = 4;
    /**
     * 5 已关闭
     */
    public static final int CLOSE = 5;


    /**
     * 判断值是否合法
     *
     * @return
     */
    public static boolean isValid(int value) {
        Field[] fields = OrderStatusConstant.class.getDeclaredFields();
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
                    log.error(OrderStatusConstant.class.getName() + " 获取字段值失败", e);
                }
            }
        }
        return false;
    }
}
