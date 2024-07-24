function getBarHeight() {
	const res = uni.getSystemInfoSync()
	if (res.platform === 'ios') {
		return 44 + res.statusBarHeight
	} else if (res.platform === 'android') {
		return 48 + res.statusBarHeight
	} else {
		return res.statusBarHeight;
	}
}

//获取可视区域高度
export function getClineHeight() {
	let {
		windowHeight
	} = uni.getSystemInfoSync();
	windowHeight = windowHeight - uni.upx2px(80) - getBarHeight();
	// console.log(windowHeight + 'px')
	return windowHeight
}