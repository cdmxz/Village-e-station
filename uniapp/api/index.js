import api from "./api.js"
// 查询轮播图
const getbanner = (e) => {
	return api({
		ur1: '/api/chart',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}
const privacy = (e) => {
	return api({
		ur1: '/api/protocol/privacy',
		data: e,
	}).then(res => {
		return res
	})
}
const service = (e) => {
	return api({
		ur1: '/api/protocol/service',
		data: e,
	}).then(res => {
		return res
	})
}

export {
	service,
	privacy,
	getbanner
}