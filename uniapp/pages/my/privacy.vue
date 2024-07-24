<template>
	<view class="container">
		<uni-list>
			<uni-list-item showArrow title="用户服务协议" to='/pages/protocol/service' />
			<uni-list-item showArrow title="隐私政策" to='/pages/protocol/policy' />
		</uni-list>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			}
		},
		methods: {

		}
	}
</script>

<style scoped lang="scss">
	.container {
		font-size: 20px;
	}

	uni-list-item {}
</style>