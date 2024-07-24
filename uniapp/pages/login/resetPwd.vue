<template>
	<view>
		<xiaokeai-humanCheck title="身份校验" closeBtnText="关闭" actionTips="向右滑动验证码" checkSuccessText="校验成功"
			:show="validShow" @checkResult='onCheckResult' @close='onClose'></xiaokeai-humanCheck>
		<view class="outline">
			<view class="item">
				<text class="left">电话：</text>
				<input maxlength="11" type="number" v-model.trim="phone" placeholder="请输入电话" />
			</view>
			<view class="item">
				<text class="left">验证码：</text>
				<input maxlength="13" type="text" v-model.trim="code" placeholder="请输入验证码" />
				<view class="btn">
					<u-button type="success" size="small" @click="onBtnClick()" :text="btnTxt"
						:disabled='btnDisabled'></u-button>
				</view>
			</view>
			<view class="item">
				<text class="left">新密码：</text>
				<input maxlength="13" type="text" v-model.trim="password" placeholder="请输入新密码" />
			</view>
			<view class="item">
				<text class="left">新密码：</text>
				<input maxlength="13" type="text" v-model.trim="password_1" placeholder="请再输入一遍新密码" />
			</view>
		</view>
		<view class="bottom">
			<u-button type="success" :plain="true" @click="onSubmit" text="提交"></u-button>
		</view>
	</view>
</template>

<script>
	import {
		isPhoneNumber
	} from '@/utils/verifyUtils';
	import {
		isEmpty
	} from '@/utils/stringUtils';
	import {
		sms,
		resetpwd
	} from '@/api/user.js'
	export default {
		data() {
			return {
				phone: "",
				code: "",
				password: "",
				password_1: "",
				countdown: 0,
				btnTxt: "发送验证码",
				intervalId: null,
				btnDisabled: false,
				validShow: false
			};
		},
		methods: {
			onBtnClick() {
				this.validShow = true
			},
			// 发送短信验证码
			async getCode() {
				// 校验手机号码
				if (!isPhoneNumber(this.phone)) {
					this.showError('电话输入错误')
					return
				}
				let resp = await sms({
					phone: this.phone
				})
				if (resp.code != 200) {
					this.showError(resp.msg)
					return
				}
				var that = this
				this.countdown = 120
				this.btnDisabled = true
				// 倒计时
				this.intervalId = setInterval(() => {
					if (that.countdown <= 1) {
						that.btnDisabled = false
						clearInterval(that.intervalId)
						return
					}
					that.countdown -= 1
					that.btnTxt = `${that.countdown}s后重试`
				}, 1000)
			},
			onCheckResult(e) {
				if (e.detail === 'success') {
					this.validShow = false
					this.getCode()
				}
			},
			onClose() {
				this.validShow = false
			},
			showSuccess(title) {
				uni.showToast({
					title: title,
					duration: 500
				})
			},
			showError(title) {
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true,
				})
			},
			async onSubmit() {
				// 拼接请求参数
				let param = {
					code: this.code,
					phone: this.phone,
					password: this.password
				}
				// 校验数据是否有空值
				for (var item in param) {
					if (isEmpty(param[item])) {
						this.showError('信息不完整')
						return
					}
				}
				if (this.password !== this.password_1) {
					this.showError('密码输入不一致')
					return
				}
				// 向后端发送请求
				let resp = await resetpwd({
					phone: this.phone,
					password: this.password,
					code: this.code
				})
				if (resp.code != 200) {
					this.showError(resp.msg)
					return
				}

				this.showSuccess('修改成功')
				clearInterval(this.intervalId)
				setTimeout(() => {
					uni.navigateBack()
				}, 500)
			}
		},
		onLoad(e) {
			if (typeof e.phone !== 'undefined') {
				if (isPhoneNumber()) {
					this.phone = e.phone
				}
			}
		}
	}
</script>

<style lang="scss">
	.outline {
		margin: 5vw;
		border-radius: 2vw;
		border: 1px solid darkgrey;
		padding: 2vw;

		.item {
			border-bottom: 1px solid darkgrey;
			display: flex;
			margin-top: 5vw;
			align-items: center;

			.left {
				margin-left: 2vw;
				width: 20%;
				font-size: 16px;
			}

			input {
				width: 45%;
			}

			.btn {
				font-size: 12px;
				width: 20vw;
			}
		}

		.verify {
			margin-top: 5vw;
		}
	}

	.bottom {
		margin: 5vw;

		u-button {
			font-size: 20px;
		}
	}
</style>