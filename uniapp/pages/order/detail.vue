<template>
	<view>

		<view class="list">
			<view class="children flex" v-for="(item,index) in data.goods" :key="index">
				<image :src="item.thumbnail_url" mode="scaleToFill" @click="previewImg(item.thumbnail_url)"></image>
				<view class="right">
					<view v-if="item.status==2">
						<text style="color: red;">退款中</text>
					</view>
					<view v-else-if="item.status==3">
						<text style="color: red;">退款完成</text>
					</view>
					<view>
						<text>{{item.name}}</text>
					</view>
					<view>
						{{item.price.toFixed(2)}}
					</view>
					<view>
						x{{item.quantity}}
					</view>
					<!-- 订单状态为：待发货，待收货，已完成 才显示退款 -->
					<view v-if="(status == 2 || status == 3 || status == 4) && item.status == 1 && !admin">
						<button class="refundBtn" type="warn" @click="refund(index)">退款</button>
					</view>
					{{admin}}
					<!-- {{item.good_id}} -->
				</view>
			</view>

		</view>
		<view class="list botm">
			<view class="top">
				<view>
					<text>商品总价</text>
					<text>￥{{data.total_amount}}</text>
				</view>
				<view>
					<text>订单运费</text>
					<text>￥{{data.postage}}</text>
				</view>
				<view>
					<text>优惠</text>
					<text>￥{{data.actual_amount}}</text>
				</view>
				<view>
					<text>实付款</text>
					<text>￥{{data.discount_amount}}</text>
				</view>
			</view>
			<view class="botm">
				<view>
					<text>订单编号</text>
					<text>{{data.order_no}}</text>
					<text class="btn" @click="copy(data.order_no)">复制</text>
				</view>
				<view v-if="!admin">
					<text>收货人信息</text>
					<text>{{data.receive_information}}</text>
				</view>
				<view v-if="!admin">
					<text>物流单号</text>
					<text>{{data.track_number}}</text>
					<text class="btn" @click="copy(data.track_number)">复制</text>
				</view>
				<view>
					<text>下单时间</text>
					<text>{{data.order_time}}</text>
				</view>
				<view>
					<text>付款时间</text>
					<text>{{data.pay_time}}</text>
				</view>
				<view v-if="!admin">
					<text>发货时间</text>
					<text>{{data.send_time}}</text>
				</view>
				<view v-if="!admin">
					<text>收货时间</text>
					<text>{{data.receive_time}}</text>
				</view>
				<view style="display: block;" v-if="admin">
					<view>
						<text>收货人姓名</text>
						<text>{{user[0]}}</text>
						<text class="btn" @click="copy(data.order_no)">复制</text>
					</view>
					<view>
						<text>收货人手机</text>
						<text>{{user[1]}}</text>
						<text class="btn" @click="copy(data.order_no)">复制</text>
					</view>
					<view>
						<text>收货人地址</text>
						<text>{{user[2]}}</text>
						<text class="btn" @click="copy(data.order_no)">复制</text>
					</view>
					<view v-if="data.status==2">
						<text>物流单号</text>
						<uni-easyinput maxlength="20" v-model="track_number" placeholder="请输入物流单号"></uni-easyinput>
					</view>
				</view>
				<view v-if="admin && data.status==2">
					<button type="primary" @click="submit(track_number)" v-if="admin">确定发货</button>
				</view>
			</view>
		</view>



	</view>
</template>

<script>
	import {
		details,
		send,
		requestRefund
	} from '@/api/order.js'
	import {
		requestSubscribe
	} from '@/utils/wxPush.js'
	import * as orderStatus from '@/constant/orderStatus.js'
	import {
		isEmpty
	} from '../../utils/stringUtils'

	export default {
		data() {
			return {
				data: {},
				admin: false, // 是否为管理员
				track_number: '',
				user: '',
				order_no: '', // 订单号
				status: 0, // 订单状态
				tmplIds: [
					'UnTYWOzPAZN8Wkt7U9GPRspI5LTMPpoJi2jh4bKBPsk', // 退款申请通知
				]
			}
		},
		methods: {
			relunch() {
				uni.redirectTo({
					url: `/pages/order/detail?id=${this.order_no}&status=${this.status}`
				})
			},
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
			refund(index) {
				let data = this
				let good_id = data.data.goods[index].good_id
				console.log(good_id)
				uni.showModal({
					title: '提示',
					editable: true,
					placeholderText: '请输入退款理由',
					success: (res) => {
						if (!res.confirm) {
							return
						}
						if (res.content.trim() == '') {
							data.showError('请输入理由')
							return
						}
						if (res.content.length > 30) {
							data.showError('理由字数过多')
							return
						}
						// 改变订单状态
						data.showLoading()
						requestRefund({
							order_no: data.order_no,
							good_id: good_id,
							reason: res.content
						}).then((res) => {
							if (res.code != 200) {
								data.showError(res.msg)
							} else {
								data.showSuccess("请耐心等候")
								requestSubscribe(data.tmplIds).then(() => {
									setTimeout(e => {
										data.relunch();
									}, 500)
								})
							}
						})
					}
				})
			},
			submit(e) {
				let data = this
				data.showLoading()
				if (isEmpty(e.trim())) {
					this.showError('请输入单号')
					return
				}
				send({
					order_no: data.data.order_no,
					track_number: e
				}).then(e => {
					if (e.code == 200) {
						data.showSuccess('发货成功')
						setTimeout(e => {
							uni.navigateBack();
						}, 500)
					} else {
						data.showError(e.msg)
					}
				})
			},
			showError(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
			showSuccess(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					icon: 'success'
				})
			},
			showLoading() {
				uni.showLoading({
					mask: true,
					title: '请稍等...'
				})
			},
		},
		onLoad(e) {
			this.admin = Boolean(e.isadmin)
			this.status = e.status
			this.order_no = e.id
		},
		mounted() {
			details({
				order_no: this.order_no
			}).then(e => {
				if (e.code != 200) {
					this.showError(e.msg)
					return
				}
				e.data.actual_amount = e.data.actual_amount.toFixed(2)
				e.data.discount_amount = e.data.discount_amount.toFixed(2)
				e.data.postage = e.data.postage.toFixed(2)
				e.data.total_amount = e.data.total_amount.toFixed(2)
				this.user = e.data.receive_information.split(',')
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

		.refundBtn {
			padding: 0;
			margin: 2vw 0 0 52vw;
			width: 13vw;
			font-size: 12px;
		}
	}

	.botm {
		.top {
			view {
				display: flex;
				font-size: 16px;
				padding: 1vw 5vw 1vw 0;
				justify-content: space-between;

				text:nth-child(1) {
					width: 22vw;
					text-align: right;
				}
			}

			border-bottom: 1px solid #ddd;
			margin: 2vw 3vw;
			padding: 2vw 0;
		}

		.botm {
			view {
				display: flex;
				padding: 1vw 0;
				font-size: 14px;

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


	}
</style>