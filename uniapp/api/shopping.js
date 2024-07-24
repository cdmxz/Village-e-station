import api from "./api.js"

// 查询商品列表
const GetShopping = (e) => {
	return api({
		ur1: '/api/good/list',
		data: e,
	}).then(res => {
		return res
	})

}
// 根据商品分类查询商品列表
const listbytype = (e) => {
	return api({
		ur1: '/api/good/listbytype',
		data: e,
	}).then(res => {
		return res
	})

}

// 修改商品信息
const update = (e) => {
	return api({
		method: 'PUT',
		ur1: '/api/good/update',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 查询商品剩余库存
const surplust = (e) => {
	return api({
		ur1: '/api/good/surplust',
		data: e,
	}).then(res => {
		return res
	})

}
// 查询商品详情
const ShopDetails = (e) => {
	return api({
		ur1: '/api/good/details',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 查询商品详情
const ShopDelete = (e) => {
	return api({
		method: 'DELETE',
		ur1: '/api/good/delete?good_id=' + e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 添加商品
const ShopAdd = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/good/add',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 查询商品列表 管理员
const adminList = (e) => {
	return api({
		ur1: '/api/good/admin/list',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}
// 根据分类查询商品列表 管理员
const adminType = (e) => {
	return api({
		ur1: '/api/good/admin/listbytype',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})

}

// 删除商品分类
const deleteCategory = (category_id) => {
	return api({
		method: 'DELETE',
		ur1: '/api/good/category/delete?category_id=' + category_id,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}

// 增加商品分类
const addCategory = (e) => {
	return api({
		method: 'POST',
		ur1: '/api/good/category/add',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}

// 修改商品分类
const updateCategory = (e) => {
	return api({
		method: 'PUT',
		ur1: '/api/good/category/update',
		data: e,
		header: {
			'User-Token': uni.getStorageSync('token')
		}
	}).then(res => {
		return res
	})
}


// 查询商品分类
const getCategory = () => {
	return api({
		ur1: '/api/good/category/'
	}).then(res => {
		return res
	})
}



export {
	adminType,
	adminList,
	surplust,
	ShopDetails,
	GetShopping,
	listbytype,
	update,
	ShopDelete,
	ShopAdd,
	getCategory,
	addCategory,
	deleteCategory,
	updateCategory

}