package com.ces.village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 文章分类
 * 1、乡村振兴
 * 2、特产介绍
 * 3、农业技术
 * 4、问题答疑
 */
@Log4j2
public class ArticleTypeConstant {

    /**
     * 1、乡村振兴
     */
    public static int RURAL_REVITALIZATION = 1;

    /**
     * 2、特产介绍
     */
    public static int SPECIALTY_INTRODUCTION = 2;

    /**
     * 3、农业技术
     */
    public static int AGRICULTURAL_TECHNOLOGY = 3;

    /**
     * 4、问题答疑
     */
    public static final int QUESTION_AND_ANSWER = 4;

    /**
     * 判断值是否合法
     *
     * @return
     */
    public static boolean isValid(int value) {
        Field[] fields = ArticleTypeConstant.class.getDeclaredFields();
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
                    log.error(ArticleTypeConstant.class.getName() + " 获取字段值失败", e);
                }
            }
        }
        return false;
    }
}
