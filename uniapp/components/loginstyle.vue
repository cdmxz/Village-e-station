<template>
	<view class="main">
		<slot></slot>
	</view>
</template>

<script>
	import {
		getClineHeight
	} from "@/utils/screenUtils.js"
	export default {
		name: "loginstyle",
		data() {
			return {};
		},
		onReady() {
			// this.height = getClineHeight()
		}
	}
</script>

<style lang="scss" scoped>
	.main {
		background-image: linear-gradient(#07c162 0%, #fff 30%);
		padding: 8vw 5vw 0 5vw;
		height: 85vh;
	}
</style>