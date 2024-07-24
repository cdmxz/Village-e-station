import {
	uuid
} from '@/utils/orange-util/orange-util.js'

/**
 * 获得文件扩展名 仅适用于h5环境
 * @param {Object} file
 */
function getExtendNameH5(file) {
	return new Promise((resolve, reject) => {
		uni.request({
			url: file,
			responseType: 'arraybuffer',
			success(res) {
				var mime = res.header['content-type'];
				var pos = mime.lastIndexOf('/');
				var extendName = mime.substring(pos + 1); // 获得扩展名
				resolve(extendName)
			},
			fail(err) {
				console.log('h5获取扩展名失败，原因：')
				console.log(err)
				resolve(null)
			}
		});
	})
}

const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';

/**
 * describe: 文件工具类
 */

/**
 * 生成一个32位的随机字符串文件名
 * @param {Object} file 文件的路径
 */
async function generateRandomFileName(file) {
	let extendName
	let pos
	// #ifdef H5
	extendName = await getExtendNameH5(file)
	if (extendName == null) {
		return null;
	}
	// #endif

	// #ifdef APP-PLUS || MP
	pos = file.lastIndexOf('.');
	extendName = file.substring(pos + 1); // 获得扩展名
	// #endif
	// 生成一个36位的uuid
	var randomStr = uuid()
	// 拼接成32位的随机文件名
	var randomFileName = randomStr + '.' + extendName;
	return randomFileName;
}

export default generateRandomFileName