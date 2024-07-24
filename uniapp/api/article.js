import api from "./api.js"

//查询列表 根据文章分类
const getArticle = (e) => {
	return api({
		ur1: '/api/article/listbytype',
		data: e,
	}).then(res => {
		return res
	})
}
//查询列表
const AllArticle = (e, header = null) => {
	return api({
		ur1: '/api/article/list',
		data: e,
		header: header
	}).then(res => {
		return res
	})
}

//查询根据文章详情
const ArtDetails = (e) => {
	return api({
		ur1: '/api/article/details',
		data: e,
	}).then(res => {
		return res
	})
}

//查询我发布的问题列表	
const MyPublish = (e) => {
	return api({
		ur1: '/api/article/mypublish',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}

//查询我发布的评论列表
const mycomment = (e) => {
	return api({
		ur1: '/api/article/comment/mycomment',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}

// 发布问题
const publish = (data) => {
	return api({
		ur1: '/api/article/publish',
		data: data,
		header: {
			'User-Token': uni.getStorageSync('token')
		},
		method: 'POST'
	}).then(res => {
		return res
	})
}

// 删除文章
const deleteArticle = (id) => {
	return api({
		ur1: '/api/article/delete?article_id=' + id,
		data: '',
		header: {
			'User-Token': uni.getStorageSync('token')
		},
		method: 'DELETE'
	}).then(res => {
		return res
	})
}

// 删除评论
const deleteComment = (id) => {
	return api({
		ur1: '/api/article/comment/delete?comment_id=' + id,
		data: '',
		header: {
			'User-Token': uni.getStorageSync('token')
		},
		method: 'DELETE'
	}).then(res => {
		return res
	})
}


// 更新文章
const updateArticle = (data) => {
	return api({
		ur1: '/api/article/update',
		data: data,
		header: {
			'User-Token': uni.getStorageSync('token')
		},
		method: 'PUT'
	}).then(res => {
		return res
	})
}

export {
	ArtDetails,
	AllArticle,
	MyPublish,
	mycomment,
	getArticle,
	publish,
	deleteArticle,
	deleteComment,
	updateArticle
}