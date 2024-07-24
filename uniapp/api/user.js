import api from "./api.js"
import * as errorCode from "@/constant/errorCode.js"

// 用户注册
const register = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/user/register',
		data: e,
	}).then(res => {

		return res
	})
}

// 用户登录
const login = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/user/login',
		data: e,
	}).then(res => {
		return res
	})

}

//管理员登录
const adminLogin = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/user/admin/login',
		data: e,
	}).then(res => {
		return res
	})
}

// token是否到期
const tokenIsExpiration = (token) => {
	return api({
		ur1: '/api/user/expiration',
		header: {
			'User-Token': token
		}
	}).then(res => {
		if (res.code != errorCode.SUCCESS) {
			console.log('token是否到期检查失败，原因：')
			console.log(res)
			return true
		} else {
			// 未到期，则会更新token
			uni.setStorageSync('token', res.data.user_token)
			return false
		}
	})
}

//查询用户信息
const information = () => {
	return api({
		ur1: '/api/user/information',
		data: '',
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}

//修改用户信息
const update = (e) => {
	return api({
		method: 'PUT',
		ur1: '/api/user/update',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 注销账号
const cancellation = () => {
	return api({
		method: 'POST',
		ur1: '/api/user/cancellation',
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}


// 加载用户列表
const userList = (e) => {
	return api({
		ur1: '/api/user/admin/users',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}

// 管理员注册
const adminRegister = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/user/admin/register',
		data: e,
	}).then(res => {
		return res
	})
}

// 请求发送短信验证码
const sms = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/user/admin/sms',
		data: e,
	}).then(res => {
		return res
	})
}

// 重置密码
const resetpwd = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/user/admin/resetpwd',
		data: e,
	}).then(res => {
		return res
	})
}


//判断用户是否登陆
function islogin() {
	let token = uni.getStorageSync('token')
	if (!token) {
		uni.showToast({
			icon: 'error',
			title: '请先登录',
			mask: true,
			success(e) {
				setTimeout(e => {
					uni.navigateTo({
						url: '/pages/login/index'
					})
				}, 1500)

			}
		})
		return false
	} else {
		return true
	}
}



export {
	islogin,
	cancellation,
	update,
	information,
	adminLogin,
	register,
	login,
	tokenIsExpiration,
	userList,
	adminRegister,
	sms,
	resetpwd
}