<template>
	<view class="main">
		<view class="title">
			<slot name="title"></slot>
		</view>
		<view class="name">
			<slot name="name"></slot>
		</view>
		<slot name="number"></slot>
		<view class="money">
			<slot name="money"></slot>
		</view>
		<view class="address">
			<slot name="address"></slot>
		</view>
		<view class="start">
			<slot name="start"></slot>
		</view>
		<view class="out">
			<slot name="out"></slot>
		</view>
		<view>
			<slot name="username"></slot>
		</view>
		<view>
			<slot name="tel"></slot>
		</view>
		<view>
			<slot name="bottom"></slot>
		</view>
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
	.title {
		font-weight: bold;
		text-align: center;
		font-size: 18px;
		margin-bottom: 1vw;
	}

	view {
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.main {
		background-color: #fff;
		margin: 4vw 2vw;
		border-radius: 5vw;
		font-size: 16px;
		box-shadow: 0 1px 5px #9d9d9d;
		padding-top: 2vw;

		view {
			padding: 2vw 4vw;
		}

		.money {
			color: #FFD700;
			font-weight: bolder;
			font-size: 16px;
		}
	}
</style>