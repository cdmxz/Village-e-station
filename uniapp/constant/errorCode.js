/**
 * 全局错误码
 */

//  "(.*?)"\)

/**
 * 返回成功
 */
export const SUCCESS = 200
/**
 * 全局错误：1001-1099
 */

/**
 * 删除失败
 */
export const DELETE_FAILED = 1001


/**
 * 修改失败
 */
export const UPDATE_FAILED = 1002


/**
 * 插入失败
 */
export const INSERT_FAILED = 1002


/*
 * 接口参数错误：1101-1199
 */

/**
 * 参数内容不合法
 */
export const PARAM_IS_INVALID = 1101


/**
 * 参数内容为空
 */
export const PARAM_IS_NULL = 1102


/**
 * 参数类型错误
 */
export const PARAM_TYPE_ERROR = 1103


/**
 * 轮播图类型不存在
 */
export const CHART_TYPE_ERROR = 1104


/*
 * 用户错误：2001-2099
 */

/**
 * 用户未登录访问的路径需要验证请登录
 */
export const USER_NOT_LOGGED_IN = 2001


/**
 * 账号不存在或密码错误
 */
export const USER_LOGIN_ERROR = 2002


/**
 * 用户不存在
 */
export const USER_NOT_EXIST = 2003


/**
 * 用户已存在
 */
export const USER_HAS_EXISTED = 2004


/**
 * 该手机号已存在
 */
export const PHONE_HAS_EXISTED = 2005


/**
 * 该用户无权限
 */
export const USER_NOT_HAVE_PERMISSION = 2007


/**
 * 密码修改失败
 */
export const PASSWORD_EDIT_FAILED = 2008


/**
 * 登录失败
 */
export const LOGIN_FAILED = 2009


/**
 * 验证码错误
 */
export const CAPTCHA_ERROR = 2010


/**
 * 注册失败
 */
export const RESISTER_ERROR = 2012


/**
 * token无效
 */
export const TOKEN_INVALID = 2013


/**
 * 地址不存在
 */
export const ADDRESS_NOT_EXIST = 2014


/*
 * 商城模块异常：3000～3099
 */

/**
 * 库存不足
 */
export const OUT_OF_STOCK = 3001


/**
 * 购物车数据为空，不能下单
 */
export const SHOPPING_CART_IS_NULL = 3002


/**
 * 购物车商品数量不能为0
 */
export const SHOPPING_NUMBER_NOT_NULL = 3003


/**
 * 用户地址为空，不能下单
 */
export const ADDRESS_BOOK_IS_NULL = 3004


/**
 * 用户地址为空
 */
export const ADDRESS_BOOK_NULL = 3005


/**
 * 订单状态错误
 */
export const ORDER_STATUS_ERROR = 3006


/**
 * 订单不存在
 */
export const ORDER_NOT_FOUND = 3007


/**
 * 商品不存在
 */
export const GOODS_NOT_FOUND = 3008


/*
 * 文章模块异常：3100~3199
 */

/**
 * 文章不存在
 */
export const ARTICLE_NOT_FOUND = 3101


/**
 * 评论不存在
 */
export const COMMENT_NOT_FOUND = 3102


/*
 * 就业模块异常：3200~3299
 */

/**
 * 就业信息分类不存在
 */
export const JOB_TYPE_NOT_FOUND = 3201


/**
 * 未找到对应的企业招聘信息
 */
export const ENTERPRISE_HIRES_NOT_FOUND = 3202


/**
 * 未找到对应的零工招聘信息
 */
export const GIG_HIRES_NOT_FOUND = 3203


/**
 * 未找到对应的零工求职信息
 */
export const GIG_JOB_HUNTING_NOT_FOUND = 3204


/**
 * 就业信息状态不存在
 */
export const JOB_STATUS_NOT_FOUND = 3205

/**
 * 未找到该用户的企业信息
 */
export const ENTERPRISE_INFORMATION_NOT_FOUND = 3206

//    ID_NOT_FOUND(3102 "评论不存在");

/*
 * OSS异常：3300~3399
 */

/**
 * 文件上传失败
 */
export const UPLOAD_FAILED = 3300


/**
 * STS获取失败
 */
export const STS_FAILED = 3301


/**
 * 文件删除失败
 */
export const OSS_DELETE_FAILED = 3302


/*
 * 微信接口异常：3400~3499
 */

/**
 * 通过code获取OpenID失败
 */
export const OPENID_FAILED = 3400


/*
 * 其他异常：4000~4099
 */

/**
 * 未知错误
 */
export const UNKNOWN_ERROR = 4001


/**
 * 服务器错误
 */
export const SERVER_ERROR = 4001