<template>
	<view>
		<!-- 其它审核状态 才显示删除 -->
		<view class="btnDel" v-if="showDel">
			<u-button @click.native.stop="onDelete" type="error" text="删除"></u-button>
		</view>
		<!-- 待审核状态时，显示 -->
		<view class="bottomFlex" v-if="showPassReject">
			<u-button type="success" text="通过" @click.native.stop="onPass"></u-button>
			<u-button type="error" text="拒绝" @click.native.stop="onReject"></u-button>
		</view>
	</view>
</template>

<script>
	export default {
		name: "JobBottomAd",
		// data是招聘信息数据
		props: {
			showDel: Boolean,
			showPassReject: Boolean,
			data: Object
		},
		data() {
			return {};
		},
		methods: {
			onDelete() {
				this.$emit('delete', this.data)
			},
			onPass() {
				this.$emit('pass', this.data)
			},
			onReject() {
				this.$emit('reject', this.data)
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

	.btnDel {
		margin: 0 auto;
		padding: 0 35vw;
	}
</style>