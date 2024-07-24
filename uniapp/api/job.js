import api from "./api.js"

// 用户查询工作 根据分类
const selectJob = (e) => {
	return api({
		ur1: '/api/job/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

//管理员查询工作
const AdminJob = (e) => {
	return api({
		ur1: '/api/job/admin/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

//我发布的招聘
const MyJob = (e) => {
	return api({
		ur1: '/api/job/mypublish/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
//根据id查询详情
const details = (e) => {
	return api({
		ur1: '/api/job/details',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
//发布招聘信息
const publish = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/job/publish',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 审核招聘信息
const review = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/job/admin/review',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

//删除招聘信息
const deleteInfo = (e) => {
	return api({
		method: 'DELETE',
		ur1: '/api/job/delete?information_id=' + e.information_id + '&type=' + e.type,
		data: '',
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

//修改招聘信息
const update = (e) => {
	return api({
		method: 'PUT',
		ur1: '/api/job/update',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}


export {
	review,
	publish,
	selectJob,
	AdminJob,
	MyJob,
	details,
	deleteInfo,
	update
}