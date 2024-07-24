<template>
	<view>
		<view class="uni-padding-wrap uni-common-mt">
			<uni-segmented-control :current="current" :values="items" style-type="text" @clickItem="onClickItem" />
		</view>
		<view class="content">
			<!-- 订单 列表 -->
			<view v-if="current != 5">
				<view class="list" v-for="i in data" :key="i.order_no" @longpress="remove(i.status,i.order_no)">
					<navigator :url="'/pages/order/detail?id='+i.order_no+'&status='+i.status+'&isadmin='+isadmin">
						<view class="children flex" v-for="item in i.goods" :key="item.goods">
							<image :src="item.thumbnail_url" mode="scaleToFill"></image>
							<view class="right">
								<view class="top flex">
									<text>{{item.name}}</text>
									<view v-if="item.status==2">
										<text style="color: red;">退款中</text>
									</view>
									<view v-else-if="item.status==3">
										<text style="color: red;">退款完成</text>
									</view>
									<view v-else>
										<text v-if='i.status==5' style="color: red;">已关闭</text>
										<text v-else style="color: red;">{{items[i.status]}}</text>
									</view>
								</view>
								<view>
									{{item.price}}
								</view>
								<view>
									x{{item.quantity}}
								</view>
							</view>
						</view>
					</navigator>
					<view class="money flex">
						<view>
							<view class="data">下单时间:{{i.order_time}}</view>
							<view class="expire" v-if='i.status==1'>
								截止付款:{{i.expire_time}} </view>
						</view>
						<text>实际金额: ￥{{i.actual_amount}}</text>
					</view>
					<!-- 用户显示 -->
					<view class="btn" v-if="isadmin!=1">
						<!-- 待付款 -->
						<view v-if='i.status==1'>
							<button type="warn" @click="cancel(i.order_no)">取消</button>
							<button class="green" @click="repay(i)">去支付</button>
						</view>
						<!-- 待发货 -->
						<view v-if='i.status==2'>
							<navigator :url="'/pages/order/detail?id='+i.order_no+'&status='+i.status"><button
									type="warn">退款</button></navigator>
							<button @click="prompt()">催发货</button>
						</view>
						<!-- 待收货 -->
						<view v-if='i.status==3'>
							<navigator :url="'/pages/order/detail?id='+i.order_no+'&status='+i.status"><button
									type="warn">退款</button></navigator>
							<button type="primary" @click="change(4,i.order_no)">确认收货</button>
						</view>
					</view>

					<!-- 管理员显示 -->
					<view class="btn" v-if="isadmin==1">
						<view v-if='i.status==2'>
							<navigator
								:url="'/pages/order/detail?id='+i.order_no+'&status='+i.status+'&isadmin='+isadmin">
								<button>去发货</button>
							</navigator>
						</view>
					</view>
				</view>
			</view>
			<!-- 售后订单 列表 -->
			<view v-else>
				<view class="list" v-for="i in data" :key="i.refund_no">
					<navigator :url="'/pages/order/refundDetail?id='+i.refund_no+'&status='+i.status">
						<view class="children flex">
							<image :src="i.thumbnail_url" mode="scaleToFill"></image>
							<view class="right">
								<view class="top flex">
									<text>{{i.name}}</text>
									<view v-if="i.status==4 || i.status==1">
										<text style="color: red;">拒绝退款</text>
									</view>
									<view v-else-if="i.status==2">
										<text style="color: red;">退款中</text>
									</view>
									<view v-else>
										<text style="color: red;">退款完成</text>
									</view>
								</view>
								<view>{{i.price}}</view>
								<view>x{{i.quantity}}</view>
							</view>
						</view>
					</navigator>
					<view class="money flex">
						<view>
							<view class="data">申请时间:{{i.putTime}}</view>
							<view style="color: red;" v-if="i.err_reason">{{i.err_reason}}</view>
						</view>
						<text>退款金额: ￥{{i.refund_amount}}</text>
					</view>
					<!-- 用户显示 -->
					<view class="btn" v-if="isadmin!=1">
						<!-- 退款中 -->
						<view v-if='i.status==2'>
							<button type="warn" @click="cancelRefund(i.refund_no)">取消退款</button>
						</view>
					</view>
					<!-- 管理员显示 -->
					<view class="btn" v-if="isadmin==1">
						<!-- 退款中 -->
						<view v-if='i.status==2'>
							<button type="warn" @click="agreeRefundAd(i.refund_no)">同意</button>
							<button type="primary" @click="rejectRefundAd(i.refund_no)">拒绝</button>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>

	</view>
</template>


<script>
	import {
		listbystatus,
		del,
		update,
		AllOrder,
		adminList,
		adminType,
		wxRepay,
		cancelPay,
		refundList,
		myRefundList,
		refundDetails,
		agreeRefund,
		cancelRefund
	} from '@/api/order.js'
	import {
		islogin
	} from '@/api/user.js'
	import * as orderStatus from '@/constant/orderStatus.js'
	import {
		data
	} from '../../uni_modules/uview-ui/libs/mixin/mixin'

	export default {
		data() {
			return {
				items: ['全部', '待付款', '待发货', '待收货', '已完成', '售后'],
				data: [],
				current: 0,
				isadmin: 0,
				page: 1,
				maxpage: 0,
			}
		},
		methods: {
			relunch() {
				uni.redirectTo({
					url: '/pages/order/index'
				})
			},
			//订单分类
			async onload(page = 1, bottom) {
				//参数2 触底加载
				let res
				let data = this
				this.data = []
				let paramCurrent = data.current
				if (this.isadmin == 1) {
					//管理员
					if (data.current == 5) {
						res = await refundList({
							page: page
						})
					} else if (data.current == 0) {
						res = await adminList({
							page: page
						})
					} else {
						res = await adminType({
							order_status: paramCurrent,
							page: page
						})
					}
				} else {
					//不为管理员
					if (data.current == 5) {
						res = await myRefundList({
							page: page
						})
					} else if (data.current == 0) {
						res = await AllOrder({
							page: page
						})
					} else {
						res = await listbystatus({
							order_status: paramCurrent,
							page: page
						})
					}
				}
				if (res.code != 200) {
					this.showError(e.msg)
					return
				}
				//是否触底加载
				if (!bottom) {
					this.data = res.data.list
					this.maxpage = res.data.pagecount
				} else {
					this.data = this.data.concat(res.data.list)
				}
			},
			onClickItem(e) {
				if (this.current !== e.currentIndex) {
					this.current = e.currentIndex
					this.page = 1
					this.onload()
				}
			},
			wxpay(data) {
				uni.hideLoading()
				// 拼接微信支付api 的请求参数
				let param = data
				param.provider = 'wxpay'
				param.success = (res) => {
					this.showSuccess('支付成功')
					console.log('支付成功', res)
					this.relunch()
				}
				param.fail = (err) => {
					this.showError('支付失败')
					console.error('支付失败', err)
					this.relunch()
				}
				// 调用微信api请求支付
				uni.requestPayment(param)
			},
			// 管理员 点击退款按钮
			async refund(order) {
				this.showLoading()
				let res = await wxRefund({
					order_no: order.order_no
				})
				if (res.code != 200) {
					this.showError(res.msg)
					return
				}
				this.showSuccess('申请中，请等候')
			},
			// 用户 点击去支付按钮，重新支付
			async repay(order) {
				const orderTime = Date.parse(order.order_time);
				const now = Date.now();
				const diff = now - orderTime
				const interval = 10 * 1000
				// 距离下单时间，间隔30秒后，才允许继续支付
				// 避免后端出错
				if (diff <= interval) {
					this.showError(`请等${parseInt((interval-diff)/1000)}秒再付款`)
					return
				}
				this.showLoading()
				let res = await wxRepay({
					order_no: order.order_no
				})
				if (res.code != 200) {
					this.showError(res.msg)
				} else {
					this.wxpay(res.data)
					this.relunch()
				}
			},
			// 点击取消按钮 取消订单
			async cancel(order_no) {
				this.showLoading()
				let res = await cancelPay({
					order_no: order_no
				})
				if (res.code != 200) {
					this.showError(res.msg)
					return
				}
				this.relunch()
			},
			// 改变订单状态
			async change(status, order_no) {
				this.showLoading()
				let res = await update({
					order_no: order_no,
					status: status
				})
				if (res.code != 200) {
					this.showError(res.msg)
					return
				}
				this.relunch()
			},
			// 删除订单
			remove(status, order_no) {
				// 状态为已关闭和已完成 才显示删除
				if (status != 4 && status != 5)
					return
				// 管理员不显示删除订单
				if (this.isadmin == 1) {
					return
				}
				let data = this
				uni.showModal({
					title: '确定要删除该订单吗？',
					success: function(res) {
						if (res.confirm) {
							data.showLoading()
							del(order_no).then(e => {
								if (e.code == 200) {
									data.relunch()
								} else {
									data.showError(e.msg)
								}
							})
						}
					}
				});
			},
			// 用户取消退款
			cancelRefund(refund_no) {
				let data = this
				uni.showModal({
					title: '确定要取消退款吗？',
					success: function(res) {
						if (res.confirm) {
							data.showLoading()
							cancelRefund({
								refund_no: refund_no
							}).then(e => {
								if (e.code == 200) {
									data.showSuccess("已取消退款")
									setTimeout(() => {
										data.relunch()
									}, 500)
								} else {
									data.showError(e.msg)
								}
							})
						}
					}
				});
			},
			// 管理员 同意退款
			agreeRefundAd(refund_no) {
				this.refundAd(refund_no, 1)
			},
			// 管理员 拒绝退款
			rejectRefundAd(refund_no) {
				let data = this
				uni.showModal({
					title: '提示',
					editable: true,
					placeholderText: '请输入拒绝退款理由',
					success: (res) => {
						if (res.confirm) {
							if (res.content.trim() == '') {
								data.showError('请输入理由')
								return
							}
							if (res.content.length > 30) {
								data.showError('理由字数过多')
								return
							}
							data.refundAd(refund_no, 0, res.content)
						}
					}
				})
			},
			// 管理员面向后端发送请求 同意/拒绝 退款 
			async refundAd(refund_no, isAgree, reason) {
				this.showLoading()
				let req = await agreeRefund({
					refund_no: refund_no,
					is_agree: isAgree,
					reason: reason
				})
				if (req.code == 200) {
					this.relunch()
				} else {
					this.showError(e.msg)
				}
			},
			showLoading() {
				uni.showLoading({
					mask: true,
					title: '请稍等...'
				})
			},
			// 催发货
			prompt() {
				uni.showLoading({
					title: '请稍等...',
					mask: true
				})
				setTimeout(() => {
					uni.hideLoading()
					this.showSuccess('催促成功')
				}, 1000)
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
					duration: 500
				})
			},
		},
		onLoad(e) {
			this.isadmin = e.isadmin
		},
		onShow() {
			if (!islogin())
				return
			this.onload()
		},
		//触底加载
		async onReachBottom() {
			if (this.page < this.maxpage) {
				this.onload(++this.page, 1)
			}
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

		.money {
			justify-content: space-between;
			padding: 1vw;
			font-size: 12px;

			.data {
				color: #b8b8b8;
			}

			.expire {
				color: #ff0000;
				// font-weight: bold;
			}
		}

		.btn {
			view {
				display: flex;
				justify-content: flex-end;

				button {
					padding: 0;
					margin: 0 1vw;
					width: 20vw;

					font-size: 12px;
				}

				.green {
					background-color: #4ca14c;
					color: white;
				}
			}
		}

		.children {
			border-bottom: 1px solid #ddd;
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
				height: 20VW;
			}
		}
	}
</style>