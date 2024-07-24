<template>
	<checkbox-group name="" @change="change">
		<label class="radio">
			<checkbox value="" class="check" :checked="status" />
			<view class="int">请阅读并同意
				<navigator url="/pages/protocol/service" style="color: #07c160;">用户协议</navigator>
				和 <navigator url="/pages/protocol/policy" style="color: #07c160;">隐私政策</navigator>
			</view>
		</label>
	</checkbox-group>
</template>

<script>
	export default {
		name: "check",
		data() {
			return {
				status: false
			};
		},
		methods: {
			change(e) {
				this.status = !this.status
				this.$emit('change', this.status)
			}
		}
	}
</script>

<style scoped lang="scss">
	.radio {
		margin: 2vh;
	}

	.int,
	.radio {
		display: flex;
		font-size: 14px;

		.check {
			transform: scale(0.6);

		}

	}
</style>