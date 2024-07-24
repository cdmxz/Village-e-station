// 向用户申请订阅消息
export function requestSubscribe(tmplIds) {
	return new Promise((resolve, reject) => {
		uni.requestSubscribeMessage({
			tmplIds: tmplIds,
			success: res => {
				for (let item in tmplIds) {
					let id = tmplIds[item]
					console.log('订阅结果：' + id + ', ' + res[id])
					// if (res[id] === 'accept') {
					// 	console.log(res[id] + '，允许')
					// } else if (res[id] === 'reject') {
					// 	console.log(res[id] + '，拒绝')
					// }
				}
				resolve(res)
			},
			fail: err => {
				if (err.errCode == 20004) {
					console.log('关闭小程序主开关')
				} else {
					console.log('调起订阅失败')
				}
			}
		});
	})
}