import api from "./api.js"

// 文本内容审核
const textReview = (e) => {
	return api({
		ur1: '/api/review/text',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		},
		method: 'POST'
	}).then(res => {
		return res
	})

}

export {
	textReview
}