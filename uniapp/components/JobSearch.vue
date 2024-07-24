<template>
	<view class="main">
		<view class="top">
			<slot name="title"></slot>
			<navigator :url="'/pages/job/publish?type='+type">
				<button type="primary" class="btnTop">
					<slot name="btn"></slot>
				</button>
			</navigator>
		</view>
		<view class="botm">
			<uni-icons type="search" class="icon"></uni-icons>
			<input type="text" v-model="value" :placeholder="placeholder" @input="$emit('input', $event.target.value)">
			<uni-icons type="close" class="iconClear" v-if="value.length!=0" @click="onClear"></uni-icons>
			<button @click="onSearch">搜索</button>
		</view>
	</view>
</template>

<script>
	export default {
		props: {
			placeholder: '',
			type: '',
			informationId: ''
		},
		data() {
			return {
				value: ''
			}
		},
		methods: {
			onSearch() {
				// 触发自定义事件
				this.$emit('search', this.value);
			},
			onClear() {
				this.value = ''
				this.$emit('clear', this.value);
			}
		}

	}
</script>

<style scoped lang="scss">
	.main {
		background-color: #fff;
		margin: 4vw 2vw 0;
		padding: 5vw;
		border-radius: 5vw;
		box-shadow: 0 1px 5px #9d9d9d;

		.top {
			display: flex;
			font-weight: 600;
			font-size: 15px;
			margin-bottom: 4vw;
			align-items: center;
		}

		button {
			font-size: 12px;
			font-weight: 400;
		}

		.btnTop {
			margin-left: 4vw;
		}

		.btnCont {
			line-height: 100%;
		}

		.botm {
			display: flex;
			position: relative;

			.icon {
				position: absolute;
				left: 7%;
				top: 62%;
				transform: scale(1.5) translateY(-50%);
			}

			.iconClear {
				position: absolute;
				left: 71%;
				top: 7%;
			}

			input {
				background-color: #cecece;
				border-radius: 5vw;
				width: 50%;
				padding: 1vw 13vw;
				font-size: 14px;
			}

			button {
				background-color: #19c9fe;
				color: white;
			}
		}
	}
</style>