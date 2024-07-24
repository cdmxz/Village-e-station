<template>
	<view class="main" v-html="content">

	</view>
</template>

<script>
	import {
		privacy
	} from '@/api/index.js'
	export default {
		data() {
			return {
				content: ''
			}
		},
		methods: {

		},
		async mounted(e) {
			let res = await privacy()
			this.content = res.data.privacy
		}
	}
</script>

<style scoped lang="scss">
	.main {
		font-size: 14px;
		padding: 5vw;
		line-height: 26px;
	}
</style>