import api from "./api.js"


// 查询我的所有订单
const AllOrder = (e) => {
	return api({
		ur1: '/api/order/my/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 提交订单
const add = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/order/admin/add',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 提交订单
const listbystatus = (e) => {
	return api({
		ur1: '/api/order/my/listbystatus',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 订单详情
const details = (e) => {
	return api({
		ur1: '/api/order/details',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 修改订单状态
const update = (e) => {
	return api({
		method: 'PUT',
		ur1: '/api/order/update/status',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 删除订单
const del = (e) => {
	return api({
		method: 'DELETE',
		ur1: '/api/order/del?order_no=' + e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 订单发货
const send = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/order/admin/send',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 查询所有订单列表
const adminList = (e) => {
	return api({
		ur1: '/api/order/admin/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 查询订单列表根据状态（管理员接口）
const adminType = (e) => {
	return api({
		ur1: '/api/order/admin/listbystatus',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 重新支付订单
const wxRepay = (e) => {
	return api({
		ur1: '/api/order/repay',
		method: 'POST',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// // 退款
// const wxRefund = (e) => {
// 	return api({
// 		ur1: '/api/order/admin/refund',
// 		method: 'POST',
// 		data: e,
// 		header: {
// 			'User-Token': uni.getStorageSync('token')
// 		}
// 	}).then(res => {
// 		return res
// 	})

// }

// 取消支付
const cancelPay = (e) => {
	return api({
		ur1: '/api/order/cancel/pay',
		method: 'POST',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}


// 申请退款
const requestRefund = (e) => {
	return api({
		ur1: '/api/order/request/refund',
		method: 'POST',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 查询退款列表（管理员）
const refundList = (e) => {
	return api({
		ur1: '/api/order/admin/refund/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 查看 我的 退款列表
const myRefundList = (e) => {
	return api({
		ur1: '/api/order/my/refund/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 查看 退款详情
const refundDetails = (e) => {
	return api({
		ur1: '/api/order/refund',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 是否同意退款
const agreeRefund = (e) => {
	return api({
		ur1: '/api/order/admin/agreerefund',
		method: 'POST',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 取消退款 用户
const cancelRefund = (e) => {
	return api({
		ur1: '/api/order/cancel/refund',
		method: 'POST',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

export {
	adminType,
	adminList,
	send,
	del,
	update,
	details,
	listbystatus,
	AllOrder,
	add,
	wxRepay,
	cancelPay,
	requestRefund,
	refundList,
	myRefundList,
	refundDetails,
	agreeRefund,
	cancelRefund
}