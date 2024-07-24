import api from "./api.js"

//查询地址列表
const addressList = (e) => {
	return api({
		ur1: '/api/address/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 更改地址
const updateaddress = (e) => {
	return api({
		method: 'PUT',
		data: e,
		ur1: '/api/address/updateaddress',
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 更改默认地址
const updatedefault = (e) => {
	return api({
		method: 'PUT',
		ur1: '/api/address/updatedefault',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 查询默认地址
const defaultaddress = (e) => {
	return api({
		ur1: '/api/address/default',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 删除地址
const remove = (e) => {
	return api({
		method: 'DELETE',
		ur1: '/api/address/delete?address_id=' + e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 添加地址
const add = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/address/add',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

export {
	defaultaddress,
	remove,
	updatedefault,
	updateaddress,
	addressList,
	add

}