<template>
	<view class="main" v-html="content">

	</view>
</template>

<script>
	import {
		service
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
			let res = await service()   
			this.content = res.data.service
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