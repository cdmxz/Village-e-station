<template>
	<uni-popup ref="privacyPopup" type="center" :is-mask-click="true">
		<view class="privacyPopup">
			<view class="title">
				<view class="title_circle"></view>
				<view>乡村e站</view>
			</view>
			<view class="content_pri">
				<text>在你使用【乡村e站】服务之前，请仔细阅读</text>
				<text style="color: #1793ee;" @click="goToPrivacy">《乡村e站小程序隐私保护指引》</text>。
				<text>如你同意乡村e站小程序隐私保护指引，请点击“同意”开始使用【乡村e站】。</text>
			</view>
			<view class="pri_btn">
				<button class="confuse_btn" @click="confusePrivacy">拒绝</button>
				<button class="confirm_btn" id="agree-btn" open-type="agreePrivacyAuthorization"
					@agreeprivacyauthorization="handleAgreePrivacyAuthorization">同意</button>
			</view>
		</view>
	</uni-popup>
</template>


<script>
	export default {
		name: "showPrivacyAgreement",
		data() {
			return {

			}
		},
		methods: {
			init(resolve) {
				this.$refs.privacyPopup.open()
				this.resolvePrivacyAuthorization = resolve
			},
			// 打开隐私协议
			goToPrivacy() {
				wx.openPrivacyContract({
					success: () => {
						console.log('打开成功');
					}, // 打开成功
					fail: () => {
						uni.showToast({
							title: '打开失败，稍后重试',
							icon: 'none'
						})
					} // 打开失败
				})
			},
			// 拒绝
			confusePrivacy() {
				this.$refs.privacyPopup.close()
				this.resolvePrivacyAuthorization({
					event: 'disagree'
				})
			},
			// 同意
			handleAgreePrivacyAuthorization() {
				// 告知平台用户已经同意，参数传同意按钮的id
				this.resolvePrivacyAuthorization({
					buttonId: 'agree-btn',
					event: 'agree'
				})
				this.$refs.privacyPopup.close()
			}
		}
	}
</script>



<style>
	* {
		box-sizing: border-box;
	}

	.privacyPopup {
		width: 520rpx;
		/* height: 500rpx; */
		background-color: #fff;
		border-radius: 50rpx;
		padding: 20rpx 40rpx;
	}

	.title {
		display: flex;
		align-items: center;
		justify-content: start;
		margin: 20rpx 0;
		font-size: 38rpx;
		font-weight: 600;
	}

	.title .title_circle {
		width: 60rpx;
		height: 60rpx;
		background-color: #efefef;
		border-radius: 50%;
		margin-right: 20rpx;
	}

	.content_pri {
		width: 480rpx;
		margin: 0 auto;
		font-size: 34rpx;
		line-height: 1.5;
	}

	.pri_btn {
		width: 100%;
		height: 158rpx;
		display: flex;
		align-items: center;
		justify-content: space-evenly;
	}

	.pri_btn .confuse_btn,
	.pri_btn .confirm_btn {
		width: 200rpx;
		height: 90rpx;
		border-radius: 20rpx;
		font-size: 34rpx;
	}

	.pri_btn .confuse_btn {
		background-color: #eee;
		color: #52bf6b;
	}

	.pri_btn .confirm_btn {
		background-color: #52bf6b;
		color: #fff;
	}
</style>