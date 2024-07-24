<template>
	<view class="main">
		<uni-section class="title" title="企业信息" titleFontSize="20px">
		</uni-section>
		<view>
			<view>企业名称</view>
			<uni-easyinput v-model="data.name" :styles="styles" :placeholderStyle="placeholderStyle"
				placeholder="请输入"></uni-easyinput>
		</view>
		<view>
			<view>信用代码</view>
			<uni-easyinput v-model="data.credit_code" :styles="styles" :placeholderStyle="placeholderStyle"
				placeholder="请输入"></uni-easyinput>
		</view>
		<view>
			<view>成立时间</view>
			<uni-datetime-picker type="date" v-model="data.established_time" :clear-icon='false' />

		</view>
		<view>
			<view>企业地址</view>
			<uni-easyinput v-model="data.address" :styles="styles" :placeholderStyle="placeholderStyle"
				placeholder="请输入"></uni-easyinput>
		</view>
		<view>
			<view>营业执照<text class="noteImage">必填</text></view>
			<u-upload :fileList="imgList_business_license_url" @afterRead="addImg" @delete="deleteImg"
				name="business_license_url" :previewFullImage="true" :maxCount="1"></u-upload>
			<!-- <uni-file-picker limit="1" @select='business' file-mediatype="image"
				@delete='dele(1)'></uni-file-picker> -->
		</view>
		<view>
			<view>企业招聘授权委托书<text class="noteImage">必填，需要盖企业公章</text></view>
			<u-upload :fileList="imgList_authorization_letter_url" @afterRead="addImg" @delete="deleteImg"
				name="authorization_letter_url" :previewFullImage="true" :maxCount="1"></u-upload>
			<!-- 				<uni-file-picker limit="1" @select='authorization' file-mediatype="image"
				@delete='dele(2)'></uni-file-picker> -->
		</view>
		<view>
			<view>企业实景<text class="noteImage">必填</text></view>
			<u-upload :fileList="imgList_photo_url" @afterRead="addImg" @delete="deleteImg" name="photo_url"
				:previewFullImage="true" :maxCount="1"></u-upload>
			<!-- 				<uni-file-picker limit="1" @select='photo' file-mediatype="image" @delete='dele(3)'></uni-file-picker> -->
		</view>
		<view class="flex">
			<button type="primary" @click="submit()">提交</button>
			<button type="warn" @click="del()">删除</button>
		</view>
	</view>
</template>

<script>
	import {
		get,
		update,
		remove,
		add
	} from "@/api/enterprise.js"
	import * as errorCode from "@/constant/errorCode.js"
	import oss from '@/api/oss.js'
	import * as ossUploadPath from '@/constant/ossUploadPath.js'
	import generateRandomFileName from '@/utils/fileUtils.js'
	export default {
		data() {
			return {
				placeholderStyle: "font-size:14px",
				styles: {},
				imgList_business_license_url: [],
				imgList_authorization_letter_url: [],
				imgList_photo_url: [],
				imgName: ['business_license_url', 'authorization_letter_url', 'photo_url'],
				data: {
					"name": "",
					"credit_code": "",
					"established_time": "",
					"address": "",
					// 先注释掉，避免校验信息出现信息不完整
					// "business_license_url": '',
					// "authorization_letter_url": '',
					// "photo_url": ''
				},
				isAdd: true
			};
		},
		methods: {
			// 删除图片
			deleteImg(event) {
				this[`imgList_${event.name}`].splice(event.index, 1)
			},
			// 新增图片
			addImg(event) {
				let lists = [].concat(event.file)
				lists.map((item) => {
					console.log(this[`imgList_${event.name}`])
					this[`imgList_${event.name}`].push({
						...item,
						status: 'success',
						message: ''
					})
				})
			},
			showError(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
			showSuccess(title) {
				uni.hideLoading()
				let callthis = this
				uni.showToast({
					title: title,
					icon: 'success'
				})
			},
			// 提交
			async submit() {
				let callthis = this
				// 校验数据是否填写完整
				for (let key in this.imgName) {
					let e = this.imgName[key]
					let imgList = this[`imgList_${e}`]
					if (imgList.length == 0) {
						this.showError('请上传必填图片')
						return
					}
				}
				for (let key in this.data) {
					if (this.data[key] == '') {
						this.showError('信息不完整')
						return
					}
				}
				uni.showLoading({
					mask: true,
					title: '提交中...'
				})
				// 上传imgList的图片到oss
				if (!await this.uploadToOss()) {
					return
				}
				// 上传图片后，把imgList的图片复制到data
				for (let key in this.imgName) {
					let e = this.imgName[key]
					let imgList = this[`imgList_${e}`]
					this.data[`${e}`] = imgList[0].url
				}
				var resp;
				if (this.isAdd) {
					resp = await add(callthis.data)
				} else {
					resp = await update(callthis.data)
				}
				if (resp.code != errorCode.SUCCESS) {
					this.showError(resp.msg)
				} else {
					this.showSuccess('成功')
					setTimeout(() => {
						uni.navigateBack()
					}, 1000)
				}
			},
			// 上传全部图片到oss
			async uploadToOss() {
				let that = this
				// 上传企业招聘3张图片到oss
				for (let key in that.imgName) {
					let e = that.imgName[key]
					// 从3个imgList_xxx数组拿到图片本地路径
					let imgpath = that[`imgList_${e}`][0].url
					// 如果这个链接是oss上的url，而不是本地路径，跳过
					if (imgpath.startsWith("https")) {
						continue
					}
					let random = ossUploadPath.JOB + await generateRandomFileName(imgpath)
					let res = await oss.uploadImageWithPromise({
						url: random, //  图片上传的目标地址
						imgPath: imgpath, // 图片本地路径
					})
					if (!res.isSuccess) {
						that.showError('上传图片失败')
						console.error('上传图片失败：' + e.msg)
						return false
					}
					// 将上传之后的图片路径复制imgList_$
					that[`imgList_${e}`][0].url = res.url
				}
				return true
			},
			del() {
				let callthis = this
				uni.showModal({
					title: '确定要删除企业信息吗？',
					success: function(res) {
						if (res.confirm) {
							remove().then(e => {
								if (e.code != errorCode.SUCCESS) {
									callthis.showSuccess(e.msg)
								} else {
									callthis.showSuccess('删除成功')
									setTimeout(() => {
										uni.navigateBack()
									}, 1500)
								}
							})
						}
					}
				});
			},
			// 获取当前用户的企业信息
			getdata() {
				get().then(e => {
					if (e.code != errorCode.SUCCESS) {
						// 获取不到该用户的企业信息
						this.isAdd = true
						// this.showError('未填写企业信息')
						return
					}
					this.isAdd = false
					this.data = e.data
					// 把data里的图片url复制到imgList
					for (let key in this.imgName) {
						let e = this.imgName[key]
						this[`imgList_${e}`].push({
							url: this.data[`${e}`]
						})
					}
					// console.log(callthis.data)
				})
			}
		},
		mounted() {
			this.getdata()
		}
	}
</script>

<style lang="scss" scoped>
	.main {
		padding: 3vw 3vw 3vw 2vw;

		view {
			padding-left: 2vw;
			margin-bottom: 2vh;
			// padding-right: 3vw;
			font-size: 16px;
		}

		.title {
			padding-left: 0;
			font-weight: bold;
		}

		.noteImage {
			margin-left: 2vw;
			font-size: 14px;
			color: red;
		}

		.flex text {
			margin-right: 50vw;
			font-size: 16px;
			line-height: 30px;
		}

		// 底部温馨提示
		.noteTitle {
			color: red;
		}

		.note {
			font-weight: bold;
		}
	}
</style>