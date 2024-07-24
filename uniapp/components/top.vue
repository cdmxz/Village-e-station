<template>
	<view class="main" :style="{paddingTop:top+'vh'}">

	</view>
</template>

<script>
	export default {
		name: 'top',
		props: {},
		data() {
			return {
				safeAreaInsets: {},
				top: 0
			}
		},
		methods: {

		},
		onReady() {
			// this.safeAreaInsets = uni.getSystemInfoSync().safeAreaInsets;
			// console.log(
			// 	`safeAreaInsets:左${this.safeAreaInsets.left},右${this.safeAreaInsets.right},上${this.safeAreaInsets.top},下${this.safeAreaInsets.bottom}`
			// )
			// this.top = this.safeAreaInsets.top
			// if (this.top === 0)
			this.top = 4
		}
	}
</script>

<style scoped lang="scss">
	.main {
		background-color: #3cb371;
	}
</style>