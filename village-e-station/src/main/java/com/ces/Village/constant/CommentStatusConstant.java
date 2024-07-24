package com.ces.Village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 评论状态常量
 */
@Log4j2
public class CommentStatusConstant {
    /**
     * 待审核
     */
    public static final int REVIEW_PENDING = 1;

    /**
     * 审核成功
     */
    public static final int REVIEW_SUCCESS = 2;

    /**
     * 审核失败
     */
    public static final int REVIEW_FAILED = 3;

    /**
     * 判断状态是否合法
     *
     * @return
     */
    public static boolean isValid(Integer value) {
        Field[] fields = CommentStatusConstant.class.getDeclaredFields();
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
                    log.error("CommentStatusConstant 获取字段值失败", e);
                }
            }
        }
        return false;
    }
}
