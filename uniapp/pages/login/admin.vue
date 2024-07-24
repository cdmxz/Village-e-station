<template>
	<loginstyle>
		<view class="box">
			<view class="list" v-for="i in data" :key="i.value">
				<view class="left">
					<text style="color: red;">*</text>
					<text>{{i.value}}</text>
				</view>
				<input v-if="i.value==='电话'" maxlength="11" type="number" v-model="i.value1"
					:placeholder="i.placeholder" />
				<input v-else maxlength="13" type="text" v-model="i.value1" :placeholder="i.placeholder" />
			</view>
			<text class="forget" @click="onForget">忘记密码</text>
			<image :src="CAPTCHA_URL" mode="aspectFit" @click="onCapthaClick"></image>
		</view>
		<check @change='change'></check>
		<button @click="login()">登录</button>
	</loginstyle>

</template>

<script>
	import * as URL from '@/constant/host.js'
	import * as errorCode from "@/constant/errorCode.js"
	import loginstyle from "@/components/loginstyle.vue"
	import check from "@/components/check.vue"
	import {
		adminLogin
	} from "@/api/user.js"

	import {
		isPhoneNumber
	} from "@/utils/verifyUtils.js";
	export default {
		components: {
			loginstyle,
			check
		},
		data() {
			return {
				data: [{
						value: '电话',
						placeholder: '请输入电话',
						value1: ''
					},
					{
						value: '密码',
						placeholder: '请输入密码',
						value1: ''
					},
					{
						value: '验证码',
						placeholder: '请输入',
						value1: ''
					}
				],
				check: false,
				intervalId: null,
				CAPTCHA_URL: URL.HOST + '/api/user/admin/captcha?v='
			}
		},
		methods: {
			// 点击忘记密码
			onForget() {
				// 跳转到发送验证码界面
				uni.navigateTo({
					url: '/pages/login/resetPwd'
				})
			},
			onCapthaClick() {
				this.updateCaptcha()
			},
			change(e) {
				this.check = e
			},
			// 刷新验证码
			updateCaptcha() {
				// url中的参数v=xx没有任何意义
				// +'1'目的是为了改变原来的url，让image重新请求图片
				this.CAPTCHA_URL += '1'
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
			async login() {
				let arr = []
				for (let a in this.data) {
					if (this.data[a].value1.trim() == '') {
						this.showError('请输入' + this.data[a].value)
						return
					}
					arr.push(this.data[a].value1.trim())
				}
				// 校验电话号码
				let val = isPhoneNumber(arr[0])
				if (!val) {
					this.showError('电话格式错误')
					return
				}
				if (!this.check) {
					this.showModal('请阅读并同意用户协议和隐私政策')
					return
				}
				let code = null
				// #ifdef MP
				code = await this.getLoginCode()
				// #endif
				try {
					var resp = await adminLogin({
						"username": arr[0],
						"password": arr[1],
						"captcha": arr[2],
						"code": code
					})

					if (resp.code != errorCode.SUCCESS) {
						this.showError(resp.msg)
						if (resp.code == errorCode.CAPTCHA_ERROR)
							this.updateCaptcha()
					} else {
						uni.setStorageSync('token', resp.data.user_token);
						this.showSuccess('登录成功')
						setTimeout(() => {
							// 跳转到首页
							uni.switchTab({
								url: '/pages/home/index'
							})
						}, 500)
					}
				} catch (e) {
					console.error(e)
					this.showError('登录失败')
				}
			},
			showSuccess(title) {
				uni.showToast({
					title: title,
					mask: true,
					duration: 500
				})
			},
			showError(title) {
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
			showModal(content) {
				uni.showModal({
					title: '提示',
					content: content,
					showCancel: false
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	.box {
		position: relative;
		margin-top: 10vh;
		font-size: 14px;
		background-color: #fff;
		padding: 2vh 8vw;
		border-radius: 5vw;
		border: 1px solid #c5c5c5;

		.list {
			display: flex;
			padding: 4vw 0;
			font-size: 18px;
			border-bottom: 1px solid #bababa;

			// 左边的宽度
			.left {
				width: 20vw;
			}

			input {
				font-size: 16px;
				width: 50%;
			}
		}
	}

	button {
		background-color: #07c160;
		border-radius: 10vw;
		color: white;
		margin: 0 5vw;
	}

	image {
		position: absolute;
		right: 3vh;
		bottom: 2vh;
		width: 25vw;
		height: 7vh;
		border: #bababa solid 1px;
		border-radius: 10rpx;
	}

	.forget {
		position: absolute;
		right: 9vw;
		bottom: 20vw;
		font-size: 14px;
		color: #7E7F83;
	}
</style>