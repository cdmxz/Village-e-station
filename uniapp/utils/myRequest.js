const myRequest = (options) => {
	return new Promise((resolve, reject) => {
		// 封装主体：网络请求
		uni.request({
			url: options.url,
			data: options.data || {},
			method: options.method || 'GET', // 默认值GET
			success: (res) => {
				resolve(res)
			},
			fail: (err) => {
				console.log('请求接口失败' + err);
				reject(err);
			}
		})
	})
}

export {
	myRequest
}