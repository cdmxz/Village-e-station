import api from "./api.js"

// 查询评论列表
const list = (e) => {
	return api({
		ur1: '/api/article/comment/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		},
	}).then(res => {
		return res
	})
}
// 发布评论
const publish = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/article/comment/publish',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		},
	}).then(res => {
		return res
	})
}
// 删除评论
const commentDel = (e) => {
	return api({
		method: 'DELETE',
		ur1: '/api/article/comment/delete?comment_id=' + e,
		header: {
			'User-Token': uni.getStorageSync('token')
		},
	}).then(res => {
		return res
	})
}

// 查询待审核的评论
const reviewList = (e) => {
	return api({
		ur1: '/api/article/comment/admin/review/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		},
	}).then(res => {
		return res
	})
}

// 审核评论
const review = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/article/comment/admin/review',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		},
	}).then(res => {
		return res
	})
}

export {
	list,
	publish,
	commentDel,
	reviewList,
	review
}