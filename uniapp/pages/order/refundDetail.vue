<template>
	<view>
		<view class="list">
			<view class="children flex">
				<image :src="data.thumbnail_url" mode="scaleToFill"></image>
				<view class="right">
					<view class="top flex">
						<text>{{data.name}}</text>
						<view v-if="data.status==1">
							<text class="red-font">拒绝退款</text>
						</view>
						<view v-else-if="data.status==2">
							<text class="red-font">退款中</text>
						</view>
						<view v-else>
							<text class="red-font">退款完成</text>
						</view>
					</view>
					<view>{{data.price}}</view>
					<view>x{{data.quantity}}</view>
				</view>
			</view>
		</view>
		<view class="list">
			<view class="botm">
				<view>
					<text>订单编号</text>
					<text>{{data.order_no}}</text>
					<text class="btn" @click="copy(data.order_no)">复制</text>
				</view>
				<view>
					<text>退款金额</text>
					<text>{{data.refund_amount}}</text>
				</view>
				<view>
					<text>退款编号</text>
					<text>{{data.refund_no}}</text>
					<text class="btn" @click="copy(data.refund_no)">复制</text>
				</view>
				<view>
					<text>申请时间</text>
					<text>{{data.putTime}}</text>
				</view>
				<view>
					<text>退款理由</text>
					<text>{{data.reason}}</text>
				</view>
				<view class="red-font" v-if="data.reject_reason">
					<text>拒绝理由</text>
					<text>{{data.reject_reason}}</text>
				</view>
				<view class="red-font" v-if="data.err_reason">
					<text>退款错误</text>
					<text>{{data.err_reason}}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		refundDetails
	} from '@/api/order.js'
	import * as orderStatus from '@/constant/orderStatus.js'

	export default {
		data() {
			return {
				data: {},
				refund_no: '', // 退款单号
				status: 0 // 订单状态
			}
		},
		methods: {
			previewImg(url) {
				uni.previewImage({
					urls: [url]
				})
			},
			copy(e) {
				uni.setClipboardData({
					data: e
				})
			},
			showError(title) {
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
		},
		onLoad(e) {
			this.status = e.status
			this.refund_no = e.id
		},
		mounted() {
			refundDetails({
				refund_no: this.refund_no
			}).then(e => {
				if (e.code != 200) {
					this.showError(e.msg)
					return
				}
				e.data.price = e.data.price.toFixed(2)
				e.data.refund_amount = e.data.refund_amount.toFixed(2)
				this.data = e.data
			})
		}
	}
</script>

<style lang="scss" scoped>
	.list {
		background-color: #fff;
		margin: 2vw;
		padding: 2vw;
		border-radius: 3%;
		border: 1px solid #ddd;

		.children {
			border-top: 1px solid #ddd;
			padding: 1vw 0;

			.right {
				flex: 1;
				text-align: end;
				padding: 0 3vw;
			}

			.top {
				flex: 1;
				justify-content: space-between;
			}

			image {
				width: 20vw;
				height: 15vh;
			}
		}

		.botm {

			view {
				display: flex;
				font-size: 14px;
				padding: 1vw 0;

				text:nth-child(1) {
					width: 25vw;
					text-align: right;
					padding-right: 3vw;
				}

				text:nth-child(2) {
					width: 50vw;
				}

				.btn {
					border: 1px solid #ddd;
					padding: 1px;
				}
			}
		}

		.red-font {
			color: red;
		}
	}
</style>