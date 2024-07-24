<template>
	<navigator :url="url">
		<view class="list">
			<view class="list1">
				<!-- 左侧图标 -->
				<slot name="image"></slot>
				<view>
					<!-- 标题 -->
					<text class="title">
						<slot name="title"></slot>
					</text>
					<!-- 小标题 -->
					<text class="subtitle">
						<slot name="subtitle"></slot>
					</text>
				</view>
			</view>
			<!-- 右侧箭头 -->
			<uni-icons type="forward" class="icon"></uni-icons>
		</view>
	</navigator>
</template>

<script>
	export default {
		name: 'MyLink',
		props: {
			// 导航到目标页面的链接
			url: ''
		},
		data() {
			return {
				image: '',
				title: '',
				subtitle: ''
			}
		},
		methods: {

		}

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
			view {
				margin-left: 2vw;
			}

			display: flex;

			text {
				display: block;
			}

			.title {
				font-weight: 600;
				font-size: 16px;
			}

			.subtitle {
				padding-left: 1vw;
				margin-top: 1vw;
				font-size: 10px;
				color: gray;
			}
		}

	}
</style>