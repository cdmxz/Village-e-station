import api from "./api.js"
// 查询我的企业信息
const get = (e) => {
	return api({
		ur1: '/api/job/enterprise/',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}

// 添加我的企业信息
const add = (e) => {
	return api({
		method:'POST',
		ur1: '/api/job/enterprise/add',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}
// 修改我的企业信息
const update = (e) => {
	return api({
		method:'PUT',
		ur1: '/api/job/enterprise/update',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}

// 添加我的企业信息
const remove = (e) => {
	return api({
		method:'DELETE',
		ur1: '/api/job/enterprise/delete',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}
export {
	get,
	add,
	remove,
	update
}