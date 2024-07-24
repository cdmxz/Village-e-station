<template>
	<view class="bottomFlex">
		<u-button type="success" text="修改" @click.native.stop="onUpdate"></u-button>
		<u-button type="error" text="删除" @click.native.stop="onDelete"></u-button>
	</view>
</template>

<script>
	export default {
		name: "JobBottom",
		// data是招聘信息数据
		props: ['data'],
		data() {
			return {

			};
		},
		methods: {
			onUpdate() {
				this.$emit('update', this.data)
			},
			onDelete() {
				this.$emit('delete', this.data)
			}
		}
	}
</script>

<style lang="scss">
	.bottomFlex {
		margin-top: 1vh;
		display: flex;
		padding: 0 20vw;
		gap: 2vw;
	}
</style>