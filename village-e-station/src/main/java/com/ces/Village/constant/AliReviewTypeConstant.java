package com.ces.village.constant;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Field;

/**
 * 阿里云 内容审核类型常量
 */
@Log4j2
public class AliReviewTypeConstant {
    /**
     * 用户昵称检测常量
     */
    public static final String NICKNAME_DETECTION = "nickname_detection";

    /**
     * 私聊互动内容检测常量
     */
    public static final String CHAT_DETECTION = "chat_detection";

    /**
     * 公聊评论内容检测常量
     */
    public static final String COMMENT_DETECTION = "comment_detection";

    /**
     * AIGC文字检测常量
     */
    public static final String AI_ART_DETECTION = "ai_art_detection";

    /**
     * 广告法合规检测常量
     */
    public static final String AD_COMPLIANCE_DETECTION = "ad_compliance_detection";

    /**
     * PGC教学物料检测常量
     */
    public static final String PGC_DETECTION = "pgc_detection";

    /**
     * 判断值是否合法
     *
     * @return
     */
    public static boolean isValid(String value) {
        Field[] fields = AliReviewTypeConstant.class.getDeclaredFields();
        for (Field field : fields) {
            // 检查字段类型是否为String
            if (field.getType() == String.class) {
                // 设置访问权限
                field.setAccessible(true);
                try {
                    // 获取字段的值，静态字段，传入null
                    String fieldValue = (String)field.get(null);
                    if (fieldValue.equals(value))
                        return true;
                } catch (IllegalAccessException e) {
                    log.error("AliReviewTypeConstant 获取字段值失败", e);
                }
            }
        }
        return false;
    }
}
