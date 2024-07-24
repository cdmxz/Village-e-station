/**
 * describe: 校验助手
 */

/**
 * 是否为正确的手机号码
 * @param {Object} value
 */
export function isPhoneNumber(value) {
	const regex = /^1[3|4|5|6|7|8|9][0-9]\d{8}$/
	return regex.test(value)
}


/**
 * 一个浮点数 是否 最多为2位小数
 */
export function isUpToTwoDecimalPlaces(value) {
	return /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/.test(value)
}