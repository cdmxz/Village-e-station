// 依赖：npm install ali-oss --save
import api from "./api.js"
import ossUtils from './ossUtils.js';
import {
	compressImage,
	getImgSize
} from '@/utils/imgUtils.js'

const HOST = 'https://villageestation.oss-cn-guangzhou.aliyuncs.com';


// 获得文件名，比如：article/v2_s2q1rn.jpg
// 返回v2_s2q1rn.jpg
function getFilename(urlPath) {
	let pos = urlPath.lastIndexOf('/');
	return urlPath.substr(pos + 1);
}

// 获取文件下载链接
async function handleDownLoad(url) {
	var name = getFilename(url);

	if (typeof(client) === "undefined")
		await initClient();

	let newUrl = client.signatureUrl(url);
	return newUrl;
}


/**
 * 下载图片
 * @param {Object} obj : { 
 *     url: 'article/aaa.jpg',// 填写后端接口返回的url 
 *     callback: (e) => { // 回调函数 
 *		    e.isSuccess:boolean 表示是否成功
 *		    e.msg:String 返回错误信息
 * 			e.code:Integer 返回的状态码
 *          e.imgPath:String 下载成功后，图片的本地路径
 *      }
 *    }
 */
function getImage(obj) {
	// 传给回调函数的对象
	let e = {
		isSuccess: false,
		code: '',
		msg: '',
		imgPath: ''
	}
	uni.downloadFile({
		url: obj.url,
		complete: (res) => {
			console.log('下载文件到oss')
			console.log(res)
			if (res.statusCode === 200) {
				// 保存文件到本地
				uni.getFileSystemManager().saveFile({
					tempFilePath: res.tempFilePath,
					complete(res1) {
						e.isSuccess = true
						e.imgPath = res1.savedFilePath
						obj.callback(e);
					}
				});
			} else {
				e.code = res.statusCode
				e.msg = res.errMsg
				obj.callback(e);
			}
		}
	});
}


/**
 * 上传图片（使用Promise包装方便使用await）
 * @param {Object} obj
 */
function uploadImageWithPromise(obj) {
	return new Promise(async (resolve, reject) => {
		await uploadImage({
			url: obj.url,
			imgPath: obj.imgPath,
			callback(e) {
				console.log(e)
				resolve(e);
			}
		});
	});
}

/**
 * 上传图片 （此接口直连oss，不经过后端）
 * e.msg=409表示已经存在同名文件
 * @param {Object} obj : { 
 *     url: 'article/aaa.jpg',//  图片上传的目标地址
 *     imgPath: imgPath,  // 图片本地路径
 *     callback: (e) => { // 回调函数 
 *		    e.isSuccess:boolean 表示是否成功
 *		    e.msg:String 返回错误信息，成功返回null
 *          e.url:String 上传成功后返回的url
 * 			e.code:Integer 返回的状态码
 *      }
 *    }
 */
async function uploadImage(obj) {
	const signObj = await ossUtils.getSign();
	const signature = signObj.signature;
	const ossAccessKeyId = signObj.OSSAccessKeyId;
	const policy = signObj.policy;
	const key = obj.url;
	const securityToken = signObj.SecurityToken;
	let filePath = obj.imgPath; // 待上传文件的文件路径。
	// 传给回调函数的对象
	let e = {
		isSuccess: false,
		code: '',
		msg: '',
		url: ''
	}
	// 图片最大大小 1MB
	const maxSize = 1 * 1024 * 1024;
	// #ifdef MP
	try {
		if (getImgSize(filePath) > maxSize) {
			filePath = await compressImage(filePath)
			console.log('压缩后的图片路径：' + filePath)
			console.log(`压缩后的图片大小：${getImgSize(filePath)} byte`)
		}
	} catch (e) {
		console.error('压缩图片失败：' + e.message)
	}
	// #endif
	uni.uploadFile({
		url: HOST,
		filePath: filePath,
		name: 'file', // 必须填file。
		formData: {
			key,
			policy,
			OSSAccessKeyId: ossAccessKeyId,
			signature,
			'x-oss-security-token': securityToken, // 使用STS签名时必传。
			'x-oss-forbid-overwrite': 'true' // 禁止覆盖同名文件
		},
		complete: (res) => {
			console.log('上传文件到oss，返回结果：')
			console.log(res)
			if (res.statusCode == 204) {
				e.isSuccess = true
				e.url = HOST + '/' + key
			} else {
				e.code = res.statusCode
				e.msg = res.errMsg
			}
			obj.callback(e);
		}
	});
}


/**
 * 从oss删除文件
 * 此接口是调用后端api，并非直接连到oss
 * @param {Object} objectName
 */
function deleteImage(objectName) {
	return api({
		ur1: '/api/image/delete',
		data: {
			objectName: objectName
		},
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}

export default {
	getImage,
	uploadImage,
	handleDownLoad,
	deleteImage,
	uploadImageWithPromise
}