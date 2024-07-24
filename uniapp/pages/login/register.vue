<template>
	<login>
		<view class="box">
			<view class="list">
				<view class="left">
					<text>头像</text>
				</view>
				<!-- #ifdef H5 -->
				<uni-file-picker limit="1" @select="success" class="img"></uni-file-picker>
				<!-- #endif -->
				<!-- #ifdef MP -->
				<button class="button img" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">

					<image class="avatar" :src='imgFile' mode="scaleToFill"></image>

					<!-- <image class="avatar" :src="imgFile == '' ? '@/static/avatar/default.jpg' : imgFile"></image> -->
				</button>
				<!-- #endif -->
			</view>
			<!-- 用户注册 -->
			<view v-if="!isAdmin" class="list" v-for="i in userData" :key="i.value">
				<view class="left">
					<text style="color: red;" v-if='i.value!="用户名"'>*</text>
					<text>{{i.value}}</text>
				</view>

				<!-- 输入微信用户名  -->
				<input v-if="i.value.startsWith('用户名')" type="nickname" v-model="i.value1" :placeholder="i.placeholder"
					maxlength="18" />
				<!-- 输入证件号码 用身份证键盘 -->
				<input v-else-if="i.value.startsWith('证')" type="idcard" v-model="i.value1" :placeholder="i.placeholder"
					maxlength="18" />
				<!-- 输入电话号码 用数字键盘 -->
				<input v-else-if="i.value.startsWith('联')" type="number" v-model="i.value1" :placeholder="i.placeholder"
					maxlength="11" />
				<view v-else-if="i.value.startsWith('乡村')" @click="onClickRegion">
					<input type="text" v-model="i.value1" :placeholder="i.placeholder" disabled />
				</view>
				<input v-else type="text" v-model="i.value1" :placeholder="i.placeholder" maxlength="30" />
			</view>

			<!-- 管理员注册 -->
			<view v-if="isAdmin" class="list" v-for="i in adminData" :key="i.value">
				<view class="left">
					<text style="color: red;">*</text>
					<text>{{i.value}}</text>
				</view>
				<!-- 输入电话号码 用数字键盘 -->
				<input v-if="i.value.startsWith('联')" type="digit" v-model="i.value1" :placeholder="i.placeholder"
					maxlength="11" />
				<view v-else-if="i.value.startsWith('乡村')" @click="onClickRegion">
					<input type="text" v-model="i.value1" :placeholder="i.placeholder" disabled="true" />
				</view>
				<input v-else type="text" v-model="i.value1" :placeholder="i.placeholder" maxlength="30" />
			</view>
		</view>
		</view>

		<label class="radio">
			<checkbox-group @change="change">
				<checkbox class="check" value="false" />
			</checkbox-group>
			<view class="int">请阅读并同意
				<navigator url="/pages/protocol/service" style="color: #07c160;">用户协议</navigator>
				和 <navigator url="/pages/protocol/policy" style="color: #07c160;">隐私政策</navigator>
			</view>
		</label>
		<button class="button btn" @click="save()">注册</button>
		<regionalComponents v-show="regionaStatus" ref="region" @cancel="cancel" @sure="sure" />
		<uni-popup ref="inputDialog" type="dialog">
			<uni-popup-dialog ref="inputClose" mode="input" title="请输入村庄" value="" placeholder="例如: 连樟村"
				@confirm="dialogInputConfirm">
			</uni-popup-dialog>
		</uni-popup>
	</login>
</template>

<script>
	import oss from '@/api/oss.js'
	import * as errorCode from "@/constant/errorCode.js"
	import * as ossUploadPath from "@/constant/ossUploadPath.js"
	import * as reviewType from "@/constant/reviewType.js"
	import generateRandomFileName from '@/utils/fileUtils.js'
	import login from "@/components/loginstyle.vue"
	import {
		isEmpty
	} from '@/utils/stringUtils.js'
	import {
		register,
		adminRegister
	} from "@/api/user.js"
	import {
		saveImage
	} from '../../utils/imgUtils'
	import {
		textReview
	} from "@/api/review.js"
	import {
		isPhoneNumber
	} from "@/utils/verifyUtils.js"
	export default {
		components: {
			login
		},
		data() {
			return {
				userData: [{
						value: '用户名',
						placeholder: '请输入微信用户名',
						value1: ''
					},
					{
						value: '姓名',
						placeholder: '请输入姓名',
						value1: ''
					},
					{
						value: '联系电话',
						placeholder: '请输入电话',
						value1: ''
					},
					{
						value: '乡村',
						placeholder: '例如: 英德市 连江口镇 连樟村',
						value1: ''
					}
				],
				adminData: [{
						value: '用户名',
						placeholder: '请输入微信用户名',
						value1: ''
					},
					{
						value: '姓名',
						placeholder: '请输入姓名',
						value1: ''
					},
					{
						value: '联系电话',
						placeholder: '请输入电话',
						value1: ''
					},
					{
						value: '密码',
						placeholder: '请输入密码',
						value1: ''
					},
					{
						value: '乡村',
						placeholder: '例如: 英德市 连江口镇 连樟村',
						value1: ''
					}
				],
				imgFile: require('@/static/avatar/default.jpg'),
				check: false,
				isAdmin: false, // 是否为管理员注册
				regionaStatus: false,
				regiona: '',
			}
		},
		onLoad(e) {
			this.isAdmin = Boolean(e.isAdmin)
			if (this.isAdmin) {
				uni.setNavigationBarTitle({
					title: '管理员注册'
				})
			}
		},
		methods: {
			success(e) {
				this.imgFile = e.tempFilePaths[0]
			},
			onChooseAvatar(e) {
				this.imgFile = e.detail.avatarUrl
			},
			onClickRegion() {
				this.regionaStatus = true;
			},
			cancel() {
				this.regionaStatus = false;
			},
			sure(data) {
				// console.log(data);
				this.regionaStatus = false;
				if (!data.includes('请选择')) {
					this.regiona = data[2] + " " + data[3]
					// 输入村庄名
					this.openInputDialog();
				}

			},
			openInputDialog() {
				this.$refs.inputDialog.open()
			},
			// 提示输入村庄的对话框的确认按钮触发
			dialogInputConfirm(e) {
				if (!isEmpty(e)) {
					let data = this.userData
					if (this.isAdmin)
						data = this.adminData
					data[data.length - 1].value1 = this.regiona + " " + e;
				}
			},
			change(e) {
				e = e.detail.value[0]
				e ? this.check = e : this.check = false
			},
			async uploadImg(imgFile) {
				let randomFile = await generateRandomFileName(imgFile)
				let objName = ossUploadPath.COMMENT + randomFile
				let res = await oss.uploadImageWithPromise({
					url: objName,
					imgPath: imgFile
				})
				if (res.isSuccess) {
					return res.url;
				} else {
					throw new Error('上传图片失败' + res.msg)
				}
			},
			showSuccess(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					duration: 500
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
			getLoginCode(obj) {
				return new Promise(
					async (resolve, reject) => {
						await uni.login({
							provider: 'weixin',
							success: (e) => {
								console.log(e)
								resolve(e.code);
							},
							fail: () => {
								reject('获取code失败' + e.errMsg)
							}
						})
					})
			},
			async save() {
				// 数据校验
				let arr = []
				let data = this.userData
				if (this.isAdmin)
					data = this.adminData

				//设置用户名
				if (data[0].value1 == '') {
					let random = Math.random().toString(36)
					data[0].value1 = random.substring(2, 13)
				}
				for (let a in data) {
					let val = data[a].value1
					if (val.trim() === '') {
						this.showError('请输入' + data[a].value)
						return
					}
					arr.push(val)
				}
				if (isPhoneNumber(arr[2]) == false) {
					this.showError("电话输入错误")
					return
				}
				if (!this.check) {
					this.showError('请阅读用户协议')
					return
				}
				uni.showLoading({
					mask: true,
					title: '注册中...'
				})
				// 审核用户名
				let resp = await textReview({
					text: arr[0],
					type: reviewType.NICKNAME_DETECTION
				})
				if (resp.code == errorCode.SUCCESS) {
					let tips = resp.data.risk_tips
					if (tips != "") {
						this.showError("用户名包含：" + tips)
						return
					}
				}
				// 上传图片到oss
				try {
					// 如果imgFile存的是base64，而不是图片路径
					if (/^data:image\/\w+;base64,/.test(this.imgFile)) {
						this.imgFile = saveImage(this.imgFile)
						console.log(this.imgFile)
					}
					// 确保该图片未被上传过
					if (this.imgFile.startsWith('https') == false) {
						this.imgFile = await this.uploadImg(this.imgFile)
					}
				} catch (err) {
					console.error('图片上传失败', err)
					this.showError(err.message)
					return
				}
				// 拼接请求参数
				let paramObj = {
					nick_name: arr[0],
					name: arr[1],
					avatar_url: this.imgFile,
					phone: arr[2],
					village: arr[3],
				}
				let res
				if (this.isAdmin) {
					paramObj.password = arr[3]
					res = await adminRegister(paramObj)
				} else {
					paramObj.code = await this.getLoginCode()
					paramObj.id_number = arr[3]
					res = await register(paramObj)
				}
				console.log(paramObj)
				if (res.code == errorCode.SUCCESS) {
					this.showSuccess('注册成功')
					if (this.isAdmin) {
						// 跳转到上一页
						setTimeout(() => {
							uni.navigateBack()
						}, 500)
					} else {
						uni.setStorageSync('token', res.data.user_token)
						setTimeout(() => {
							// 跳转到首页
							uni.switchTab({
								url: '/pages/home/index'
							})
						}, 500)
					}
				} else {
					this.showError(res.msg)
				}
			}
		},
	}
</script>

<style scoped lang="scss">
	.img {
		padding: 0;
		margin-left: 2vw;
		width: 15vw;
		height: 15vw;

		.avatar {
			padding: 0;
			width: 100%;
			height: 100%;
		}
	}

	.box {
		margin-top: 10vh;
		font-size: 12px;
		background-color: #fff;
		padding: 2vh 8vw;
		border-radius: 5vw;
		border: 1px solid #c5c5c5;

		.list {
			display: flex;
			padding: 3vw 0;

			border-bottom: 1px solid #bababa;

			.left {
				width: 20vw;
			}

			input {
				// padding-left: 10vw;
				font-size: 12px;
				width: 50vw;

			}
		}
	}

	.radio {
		margin: 2vh;
	}

	.int,
	.radio {
		display: flex;
		font-size: 14px;

		.check {
			transform: scale(0.6);

		}

	}

	.btn {
		border: 3px solid #07c160;
		background-color: #fff;
		border-radius: 10vw;
		margin: 0 5vw;
	}
</style>