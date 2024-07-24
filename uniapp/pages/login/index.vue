<template>
	<view>
		<loginstyle>
			<view class="box">
				<button style="background-color: #07c160;" @click="userlogin()">一键登录</button>

				<navigator url="/pages/login/admin">
					<button style="background-color: #379fd1;">干部(管理员)登录</button>
				</navigator>

				<button style="background-color: #ffa73b;" @click="goHome()">返回</button>
			</view>
		</loginstyle>
	</view>
</template>

<script>
	import loginstyle from "@/components/loginstyle.vue"
	import {
		login,
		tokenIsExpiration
	} from "@/api/user.js"
	import * as errorCode from "@/constant/errorCode.js"


	export default {
		components: {
			loginstyle
		},
		async mounted() {
			// 仅在h5编译
			//#ifdef H5
			uni.setStorageSync('token',
				'eyJhbGciOiJIUzI1NiJ9.eyJpZGVudGl0eSI6ImFkbWluIiwiZXhwIjoxNzA4NDA3ODA2LCJ1c2VySWQiOjE3NDIxNjkxMTA4MDkwOTIwOTl9.NGc6eYmzB9KvO4N-hmI-k7IAklIXqtpi0HcdmM0JAKM'
			)

			let token = uni.getStorageSync('token')
			let isExpiration = await tokenIsExpiration(token)
			if (isExpiration == false) {
				// token未过期
				console.log('token未过期')
				// 跳转到首页
				uni.switchTab({
					url: '/pages/home/index'
				})
				return
			}
			//#endif
		},
		data() {
			return {

			}
		},
		methods: {
			goHome() {
				uni.reLaunch({
					url: '/pages/home/index'
				})
			},
			showError(title) {
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
			//用户一键登录
			userlogin() {
				uni.showLoading({
					mask: true,
					title: '登陆中...'
				})
				let that = this
				// 一键登录
				uni.login({
					provider: 'weixin',
					async success(el) {
						// 拿到code
						let code = el.code
						console.log('用户code:' + code)
						// 登录
						let res = await login({
							code
						})
						// 隐藏加载框
						uni.hideLoading()
						if (res.code == errorCode.SUCCESS) {
							uni.setStorageSync('token', res.data.user_token)
							// 跳转到首页
							uni.switchTab({
								url: '/pages/home/index'
							})
						} else if (res.code == errorCode.USER_NOT_EXIST) {
							// 用户不存在
							console.log('用户不存在')
							that.showError(res.msg)
							// 跳转到注册页面
							setTimeout(e => {
								uni.navigateTo({
									url: '/pages/login/register'
								})
							}, 1000)
						} else {
							// 其他错误
							console.log("登录失败：" + res.msg)
							uni.removeStorageSync('token')
							that.showError(res.msg)
						}
					}
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	.box {
		margin-top: 10vh;
		background-color: #fff;
		padding: 1vh 3vw;

		box-shadow: 0 0 10px #9a9a9a;
		border-radius: 5vw;

		.register {
			border: 3px solid #07c160;
			color: #07c160;
			background-color: #fff;

		}

		button {
			border-radius: 20vw;
			margin: 5vh 0;
			color: white;
		}
	}

	.main {
		background-image: linear-gradient(#07c162 0%, #fff 50%);
		padding: 10vw;


	}
</style>