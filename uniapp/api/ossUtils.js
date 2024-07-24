import crypto from 'crypto-js';
import {
	Base64
} from 'js-base64';

import api from "./api.js"

/**
 * 获取临时凭证
 */
async function getToken() {
	const url = '/api/oss/sts';
	var res = await api({
		ur1: url
	});
	var obj = res.data;
	console.log('返回token：' + obj.AccessKeyId)
	return obj;
}

// 计算签名。
function computeSignature(accessKeySecret, canonicalString) {
	return crypto.enc.Base64.stringify(crypto.HmacSHA1(canonicalString, accessKeySecret));
}

/**
 * 获取上传文件时所需的签名 
 */
async function getSign() {
	// 设置policy过期时间
	const date = new Date();
	date.setHours(date.getHours() + 1);
	const policyText = {
		expiration: date.toISOString(), // 设置policy过期时间。
		conditions: [
			// 限制上传大小。
			["content-length-range", 0, 1024 * 1024 * 1024],
		],
	};
	const credentials = await getToken()
	const policy = Base64.encode(JSON.stringify(policyText)) // policy必须为base64的string。
	const signature = computeSignature(credentials.AccessKeySecret, policy)
	const formData = {
		OSSAccessKeyId: credentials.AccessKeyId,
		signature,
		policy,
		'SecurityToken': credentials.SecurityToken
	}
	return formData
}

export default {
	getToken,
	getSign
}