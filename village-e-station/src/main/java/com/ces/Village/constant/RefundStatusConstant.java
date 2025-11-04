package com.ces.village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 退款状态
 */
@Log4j2
public class RefundStatusConstant {

    /**
     * 1 未退款、取消退款、退款失败
     * 只用于 订单详情表中商品的退款状态
     * 不用于 退款表的退款状态
     */
    public static final int NO_REFUND = 1;

    /**
     * 2 待退款
     */
    public static final int PENDING_REFUND = 2;

    /**
     * 3 退款成功
     */
    public static final int SUCCESS_REFUND = 3;

    /**
     * 4 拒绝退款
     */
    public static final int REJECT_REFUND = 4;

    /**
     * 判断值是否合法
     *
     * @return
     */
    public static boolean isValid(int value) {
        Field[] fields = RefundStatusConstant.class.getDeclaredFields();
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
                    log.error(RefundStatusConstant.class.getName() + " 获取字段值失败", e);
                }
            }
        }
        return false;
    }


    public static String toString(int value) {
        return switch (value) {
            case PENDING_REFUND -> "待退款";
            case SUCCESS_REFUND -> "退款成功";
            case REJECT_REFUND -> "拒绝退款";
            default -> "unknown";
        };
    }
}
