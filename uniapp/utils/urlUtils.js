/**
 * describe: url助手
 */

import {
	myRequest
} from '@/utils/myRequest.js'
/**
 * 获取页面标题
 * @param {Object} url
 * @@return {Object} e
 }
 */
export async function fetchPageTitle(url) {
	var e = {
		isValid: false,
		title: ''
	}
	try {
		const response = await myRequest({
			url: url,
			header: {
				'User-Agent': 'Mozilla/5.0 (Linux; Android 13; Pixel 7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Mobile Safari/537.36 Edg/120.0.0.0'
			}
		})
		if (response.statusCode != 200) {
			throw new Error(`无法访问url，原因：${response.statusCode}`);
		} else {
			e.isValid = true
			const html = response.data;
			const match = /<title>(.*?)<\/title>/i.exec(html);
			e.title = match ? match[1] : '';
			//console.log(title)
		}
	} catch (error) {
		console.error(error);
	}
	return e
}

/**
 * 测试url是否有效
 * @param {Object} url
 */
export function isValidUrl(url) {
	const regex = /^(?:(http|https|ftp):\/\/)?((|[\w-]+\.)+[a-z0-9]+)(?:(\/[^/?#]+)*)?(\?[^#]+)?(#.+)?$/i;
	return regex.test(url);
}