package com.ces.Village.constant;

import lombok.Getter;

/**
 * 错误码枚举
 */
@Getter
public enum ErrorCodeEnum {
    SUCCESS(200, "success"),

    /**
     * 全局错误：1001-1099
     */
    DELETE_FAILED(1001, "删除失败"),
    UPDATE_FAILED(1002, "修改失败"),
    INSERT_FAILED(1002, "插入失败"),

    /*
     * 接口参数错误：1101-1199
     */
    PARAM_IS_INVALID(1101, "参数内容不合法"),
    PARAM_IS_NULL(1102, "参数内容为空"),
    PARAM_TYPE_ERROR(1103, "参数类型错误"),
    CHART_TYPE_ERROR(1104, "轮播图类型不存在"),

    /*
    用户错误：2001-2099
     */
    USER_NOT_LOGGED_IN(2001, "用户未登录"),
    USER_LOGIN_ERROR(2002, "账号不存在或密码错误"),
    USER_NOT_EXIST(2003, "用户不存在"),
    USER_HAS_EXISTED(2004, "用户已存在"),
    PHONE_HAS_EXISTED(2005, "该手机号已存在"),
    USER_NOT_HAVE_PERMISSION(2007, "该用户无权限"),
    PASSWORD_EDIT_FAILED(2008, "密码修改失败"),
    LOGIN_FAILED(2009, "登录失败"),
    CAPTCHA_ERROR(2010, "验证码错误"),
    RESISTER_ERROR(2012, "注册失败"),
    TOKEN_INVALID(2013, "token无效"),
    ADDRESS_NOT_EXIST(2014, "地址不存在"),
    TOKEN_IS_EXPIRATION(2015, "token过期"),
    SMS_INTERVAL_TOO_SHORT(2016, "短信验证码间隔太短"),
    SMS_FAILED(2016, "发送短信验证码失败"),
    CAPTCHA_EXPIRED(2017, "验证码过期"),
    /*
     商城模块异常：3000～3099
     */
    OUT_OF_STOCK(3001, "库存不足"),
    SHOPPING_CART_IS_NULL(3002, "购物车数据为空，不能下单"),
    SHOPPING_NUMBER_NOT_NULL(3003, "购物车商品数量不能为0"),
    ADDRESS_BOOK_IS_NULL(3004, "用户地址为空，不能下单"),
    ADDRESS_BOOK_NULL(3005, "用户地址为空"),
    ORDER_STATUS_ERROR(3006, "订单状态错误"),
    ORDER_NOT_FOUND(3007, "订单不存在"),
    GOODS_NOT_FOUND(3008, "商品不存在"),
    SHOPPING_CART_CLEAR_FAILURE(3009, "清空购物车失败"),
    GOODS_CATEGORY_NOT_FOUND(3010, "商品分类不存在"),
    GOODS_CATEGORY_HAS_EXISTED(3011, "商品分类已存在"),
    ORDER_CREATE_FAILED(3012, "订单创建失败"),
    OVER_LIMIT(3013, "数量超限"),
    PAYMENT_FAILED(3015, "支付失败"),
    REFUND_ERROR(3015, "退款出错"),
    NOT_FOUND_REFUND(3016, "找不到退款记录"),
    REFUND_GREATER_THAN_TOTAL(3017, "退款金额大于总金额"),
    PAYMENT_INTERVAL_TOO_SHORT(3019, "支付间隔太短"),
    HAS_EXISTED_REFUND(3020, "该商品已存在退款"),
    ORDER_NOT_EXISTED_GOODS(3021, "订单中不存在该商品"),
    REFUND_NOT_FOUND(3022, "退款记录不存在"),
    /*
    文章模块异常：3100~3199
     */
    ARTICLE_NOT_FOUND(3101, "文章不存在"),
    COMMENT_NOT_FOUND(3102, "评论不存在"),
    /*
    就业模块异常：3200~3299
     */
    JOB_TYPE_NOT_FOUND(3201, "就业信息分类不存在"),
    ENTERPRISE_HIRES_NOT_FOUND(3202, "未找到对应的企业招聘信息"),
    GIG_HIRES_NOT_FOUND(3203, "未找到对应的零工招聘信息"),
    GIG_JOB_HUNTING_NOT_FOUND(3204, "未找到对应的零工求职信息"),
    JOB_STATUS_NOT_FOUND(3205, "就业信息状态不存在"),
    ENTERPRISE_INFORMATION_NOT_FOUND(3206, "未找到该用户的企业信息"),
    ENTERPRISE_INFORMATION_HAS_EXISTED(3207, "该用户企业信息已存在"),
    /*
    OSS异常：3300~3399
     */
    UPLOAD_FAILED(3300, "文件上传失败"),
    STS_FAILED(3301, "STS获取失败"),
    OSS_DELETE_FAILED(3302, "文件删除失败"),

    /*
    微信接口异常：3400~3499
    */
    OPENID_FAILED(3400, "通过code获取OpenID失败"),
    CONTENT_REVIEW_FAILED(3401, "内容审核出错"),
    USER_PROHIBITED(3402, "该用户禁止调用"),

    /*
     * 内容审核异常：3500-3599
     */
    IS_ABNORMAL_TEXT(3500, "文本内容违规"),
    IS_ABNORMAL_IMAGE(3501, "图片内容违规"),
    IS_ABNORMAL_TITLE(3502, "标题违规"),

    /*
     其他异常：4000~4099
     */
    UNKNOWN_ERROR(4001, "未知错误"),

    SERVER_ERROR(4002, "服务器错误"),
    REQ_EXCEPTION(4003, "请求异常");

    private final Integer code;
    private final String msg;

    ErrorCodeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ErrorCodeEnum{" + "code='" + code + '\'' + ", msg='" + msg + '\'' + '}';
    }
}
