/**
 * 招聘信息状态常量
 */

/**
    * 表示全部状态
    * 查询招聘信息列表接口 使用
    */
export const ALL = 0;
/**
 * 1、待审核
 */
export const REVIEW_PENDING = 1;
/**
 * 2、审核成功，展示中
 */
export const REVIEW_SUCCESS = 2;
/**
 * 3、审核失败，已打回
 */
export const REVIEW_FAILURE = 3;
/**
 * 4、到达截止时间，已下架
 */
export const DEADLINE = 4;