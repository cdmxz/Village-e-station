/**
 * describe: 图片助手
 */


/**
 * 压缩图片
 * @param {Object} url
 */
function compressImage(url) {
	return new Promise((reslove, reject) => {
		const tempFilePath = url //url是选中图片的路径
		uni.compressImage({
			src: tempFilePath,
			quality: 60, //压缩的程度
			success: (res) => {
				reslove(res.tempFilePath) //压缩成功返回的路径
			},
			fail: (error) => {
				console.log('压缩失败', error)
			}
		})
	})
}

/** 获取图片大小（单位：字节）
 * @param {Object} path 图片本地路径
 */
function getImgSize(path) {
	return uni.getFileSystemManager().statSync(path).size
}

// 保存图片
function saveImage(imgbase64url) {
	let base64 = imgbase64url.replace(/^data:image\/\w+;base64,/, "");
	// 本地路径
	let filePath = `${wx.env.USER_DATA_PATH}/img_id_${new Date().getTime()}.jpg`;
	uni.getFileSystemManager().writeFileSync(filePath, base64, 'base64')
	return filePath
}

/**
 * 调整图片大小
 * @param {Object} path
 */
function resizeImage(imgPath, maxWidth, maxHeight) {
	return new Promise((reslove, reject) => {
		// 压缩图片
		uni.compressImage({
			src: imgPath,
			compressedWidth: maxWidth,
			compressedHeight: maxHeight,
			success: (res) => {
				reslove(res.tempFilePath) //压缩成功返回的路径
			},
			fail: (err) => {
				reject(err)
			}
		})
	})
}
export {
	compressImage,
	getImgSize,
	saveImage,
	resizeImage
}