<template>
	<view class="main" :style="{paddingBottom:safeAreaInsets}">
		<view class="main1">
			<view @click="goHome">
				<slot name="index">
					<image src="../static/tabbar/fangzi.png" mode=""></image>
					<text>首页</text>
				</slot>
			</view>
			<view @click="goMy">
				<slot name="my">
					<image src="../static/tabbar/yonghu.png" mode=""></image>
					<text>我的</text>
				</slot>
			</view>
		</view>
	</view>

</template>

<script>
	export default {
		data() {
			return {
				safeAreaInsets: ''
			}
		},
		methods: {
			goHome() {
				uni.switchTab({
					url: '/pages/home/index'
				})
			},
			goMy() {
				uni.switchTab({
					url: '/pages/my/index'
				})
			}

		},
		mounted() {
			const {
				safeAreaInsets
			} = uni.getSystemInfoSync()
			this.safeAreaInsets = safeAreaInsets.bottom + 'px'
		}

	}
</script>

<style scoped lang="scss">
	.main {
		background-color: #3cb371;
		width: 100vw;
		color: white;
		position: fixed;
		bottom: 0;

		image {
			width: 21px;
			height: 21px;
			display: block;
			margin-left: 5rpx;
		}

		.main1 {
			display: flex;
			justify-content: space-between;
			padding: 1vh 165rpx 0vh 165rpx;
		}

		text {
			font-size: 12px;
		}
	}
</style>