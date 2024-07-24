<template>
	<button class="open-contact" open-type="contact" bindcontact="handleContact">
		<view class="list">
			<view class="list1">
				<!-- 左侧图标 -->
				<u-icon name="server-man" color="#2979ff" size="32"></u-icon>
				<view>
					<!-- 标题 -->
					<text class="title">联系客服</text>
					<!-- 小标题 -->
					<text class="subtitle">打开客服页面</text>
				</view>
			</view>
			<!-- 右侧箭头 -->
			<uni-icons type="forward" class="icon"></uni-icons>
		</view>
	</button>
</template>

<script>
	export default {
		name: 'open-contact',
		data() {
			return {

			}
		},
	}
</script>

<style scoped lang="scss">
	.list {
		height: 18vw;
		border-bottom: #e7e7e7 1px solid;
		margin: 0 2vw;
		padding-right: 5vw;
		display: flex;
		justify-content: space-between;
		align-items: center;

		.list1 {
			display: flex;

			view {
				margin-left: 2vw;
			}

			text {
				display: block;
			}

			.title {
				font-weight: 600;
				font-size: 16px;
			}

			.subtitle {
				padding-left: 1vw;
				font-size: 10px;
				color: gray;
			}
		}

	}

	.open-contact {
		background-color: #ffffff;
		padding: 0;
	}

	.open-contact::after {
		border: none; // 去除边框
	}
</style>