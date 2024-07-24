import api from "./api.js"

// 查询购物车列表
const GetCart=(e)=>{
		return api({
			ur1:'/api/cart/goodlist',
			data:e,
			header:{'User-Token':uni.getStorageSync('token')}
		}).then(res=>{
			return res
		})

}
// 修改购物车商品数量
const UpdataCart=(e)=>{
		return api({
			method:'PUT',
			ur1:'/api/cart/update/goodnumber',
			data:e,
			header:{'User-Token':uni.getStorageSync('token')}
		}).then(res=>{
			return res
		})

}

// 添加购物车商品
const goodadd=(e)=>{
		return api({
			method:'POST',
			ur1:'/api/cart/goodadd',
			data:e,
			header:{'User-Token':uni.getStorageSync('token')}
		}).then(res=>{
			return res
		})

}
// 查询购物车一种商品数量
const goodnumber=(e)=>{
		return api({
			ur1:'/api/cart/goodnumber',
			data:e,
			header:{'User-Token':uni.getStorageSync('token')}
		}).then(res=>{
			return res
		})

}
// 删除购物车商品
const gooddelete=(e)=>{
		return api({
			method:'DELETE',
			ur1:'/api/cart/gooddelete?good_id='+e,
			header:{'User-Token':uni.getStorageSync('token')}
		}).then(res=>{
			return res
		})

}
//清空购物车
const clean=(e)=>{
		return api({
			method:'DELETE',
			ur1:'/api/cart/clean',
			data:e,
			header:{'User-Token':uni.getStorageSync('token')}
		}).then(res=>{
			return res
		})

}

export{
	goodnumber,
	gooddelete,
	clean,
	goodadd,
	UpdataCart,
	GetCart,
	
}