/**
 * describe: 字符串助手
 */

/**
 * 遍历对象里面的字符串，删除每个字符串两端的空白字符
 * @param {Object} obj
 */
export function trimObjectStrings(obj) {
	for (let key in obj) {
		if (obj.hasOwnProperty(key) && typeof obj[key] === 'string') {
			obj[key] = obj[key].trim();
		}
	}
	return obj;
}

export function isEmpty(str) {
	return (!str || str.length == 0);
}