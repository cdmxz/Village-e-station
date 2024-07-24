/**
 * 订单状态常量
 */

/**
 * 0 所有状态
 */
export const ALL = 0;

/**
 *  1 待付款
 */
export const UNPAID = 1;
/**
 * 2 待发货
 */
export const UNSEND = 2;
/**
 * 3 待收货
 */
export const UNRECEIVE = 3;
/**
 * 4 已完成
 */
export const COMPLETE = 4;
/**
 * 5 已关闭
 */
export const CLOSE = 5;

/**
 * 6 待退款
 */
export const PENDING_REFUND = 6;

/**
 * 7 退款成功
 */
export const PENDING_SUCCESS = 7;

/**
 * 售后服务，表示 6 待退款 和 7 退款成功 两种状态
 */
export const AFTER_SALE_SERVICE = 8;