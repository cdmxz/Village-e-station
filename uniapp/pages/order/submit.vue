<template>
	<view class="">
		<view class="title">
			配送
		</view>
		<view class="address" @click="toggle('bottom')">
			<view class="" v-if="address!=''">
				<view class="top">
					<view class="flex">
						<view class="left">
							<text style="margin-right: 5vw;">{{address.consignee}}</text>
							<text>{{address.phone}}</text>
						</view>
					</view>
					<view class="">
						{{address.province}}{{address.city}}{{address.district}}{{address.detail}}
					</view>
				</view>
			</view>
			<view class="" v-else>
				请选择收货地址
			</view>
		</view>
		<view class="main">
			<view class="list flex" v-for="(item, index) in data" :key="item.name">
				<image :src="item.thumbnail_url" mode=""></image>
				<view class="flex price">
					<view>{{item.name}}</view>
					<view>￥{{(item.price*item.number).toFixed(2)}}</view>
				</view>
				<view class="btn flex">

					<text class="num">
						<text class="x">x</text>{{item.number}}
					</text>

				</view>
			</view>
		</view>

		<view class="fixed flex">
			<view class="">
				实付金额：
				<text>￥{{total}}</text>
			</view>
			<view class="btn" @click="submit()">
				提交订单
			</view>
		</view>

		<uni-popup ref="popup" background-color="#fff" class="popup">
			<view class="popup-content" :class="{ 'popup-height': type === 'left' || type === 'right' }">

				<view>
					<radio-group @change="change">
						<view class="list" v-for="i in list" :key="i.address_id" @click="choose(i)">
							<view class="top">
								<view class="flex">
									<view class="left">
										<text>{{i.consignee}}</text>
										<text>{{i.phone}}</text>
									</view>
								</view>
								<view class="">
									{{i.province}}{{i.city}}{{i.district}}{{i.detail}}
								</view>
							</view>
							<view class="">
								<view class="botm flex">
									<label>
										<radio :checked="i.is_default==1" :value="i.address_id" /><text>默认</text>
									</label>
								</view>

							</view>
						</view>
					</radio-group>
					<view class="btn">
						<navigator url="/pages/address/add">
							<button type='warn'>新建地址</button>
						</navigator>
					</view>
				</view>

			</view>
		</uni-popup>
	</view>
</template>

<script>
	import {
		defaultaddress,
		updatedefault,
		addressList
	} from '@/api/address.js'
	import {
		add
	} from '@/api/order.js'
	import {
		ShopDetails
	} from '@/api/shopping.js'
	import {
		gooddelete
	} from "@/api/cart.js"
	import {
		requestSubscribe
	} from '@/utils/wxPush.js'

	export default {
		data() {
			return {
				data: [],
				total: 0,
				type: 'bottom',
				list: [],
				address: '',
				Parameters: '', //页面传参
				tmplIds: [
					'lO1qboDVHoKdBWRvdSEVjU5IstfXHZeViaIpqNZM_U8', // 支付成功通知
					'7BY8jDy0P4tww9CJ5nEp4QCmCbdpYHMLmoCPW-A_EEg', // 订单发货提醒
				]
			};
		},
		methods: {
			toggle(type) {
				this.$refs.popup.open(type)
			},
			choose(e) {
				// 用户切换地址
				console.log(e)
				this.address = e
				this.$refs.popup.close()
			},
			// 切换默认地址
			change(e) {
				updatedefault({
					address_id: e.detail.value
				}).then(e => {
					if (e.code != 200) {
						this.showError('切换地址失败')
					}
					console.log(e)
				})
			},
			showSuccess(title) {
				uni.hideLoading();
				uni.showToast({
					title: title
				})
			},
			showError(title) {
				uni.hideLoading();
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
			// 提交订单
			async submit() {
				// 向用户申请订阅消息
				await requestSubscribe(this.tmplIds)
				//显示加载框
				uni.showLoading({
					title: '下单中...',
					mask: true,
					success() {
						// 30秒后自动关闭，避免后端无响应
						setTimeout(() => {
							uni.hideLoading();
						}, 30000)
					}
				});
				let data = {
					"address_id": this.address.address_id,
					'goods': []
				}
				this.data.forEach(e => {
					let obj = {
						'good_id': e.good_id,
						'number': e.number
					}
					data.goods.push(obj)
				})
				let res = await add(data)
				if (res.code != 200) {
					this.showError(res.msg)
					return
				}
				// 微信支付api 的请求参数（后端返回）
				let param = res.data
				// 调用微信api请求支付
				uni.requestPayment({
					provider: "wxpay",
					timeStamp: param.timeStamp,
					nonceStr: param.nonceStr,
					package: param.package,
					signType: param.signType,
					paySign: param.paySign,
					success: (res) => {
						this.showSuccess('支付成功')
						console.log('支付成功', res)
						setTimeout(e => {
							uni.navigateBack()
						}, 1000)
					},
					fail: (err) => {
						this.showError('支付失败')
						console.error('支付失败', err)
						setTimeout(e => {
							uni.navigateBack()
						}, 1500)
					}
				})
			}
		},
		onLoad(e) {
			this.Parameters = e
		},
		onShow() {
			let e = this.Parameters
			this.data = JSON.parse(e.id)
			this.total = e.total
			// 默认地址
			defaultaddress().then(e => {
				if (e.code != 200) {
					this.showError(e.msg)
				} else {
					this.address = e.data
				}
			})
			// 地址列表
			addressList().then(e => {
				if (e.code != 200) {
					this.showError(e.msg)
				} else {
					this.list = e.data
				}
			})
		}
	}
</script>

<style lang="scss" scoped>
	.title {
		font-size: 20px;
	}

	.main {
		padding: 2vh 0;
		background-color: #efefef;
		min-height: 80vh;
		padding-bottom: 5vh;

		.list {
			position: relative;
			padding: 2vw;
			background-color: #fff;
			margin: 2vh 0;

			.radio {
				align-self: center;
			}

			image {
				margin-left: 10vw;
				width: 20vw;
				height: 12vh;
			}

			.price {
				flex: 1;
				margin: 1vw 2vw;
				justify-content: space-between;
			}

			.btn {
				position: absolute;
				bottom: 1vh;
				right: 3vw;

				.num {
					margin: 0 2vw;
				}

				view {
					--wid: 7vw;
					background-color: #09bb07;
					height: var(--wid);
					width: var(--wid);
					color: white;
					text-align: center;
					line-height: var(--wid);
					border-radius: var(--wid);
				}
			}
		}
	}

	.popup {
		.list {
			background-color: #fff;
			padding: 2vw;
			font-size: 14px;

			.top {
				padding-bottom: 2vw;
				border-bottom: 1px solid #ddd;
				margin-bottom: 2vw;

				.flex {
					justify-content: space-between;
					padding: 2vw 0;

					text {
						margin-right: 5vw;
					}
				}

			}

			.botm {
				justify-content: space-between;

				.bor {
					border: 1px solid #a8a8a8;
					padding: 1vw;
					border-radius: 20%;
				}
			}
		}
	}

	.fixed {
		position: fixed;
		bottom: 0;
		width: 100%;
		justify-content: space-between;
		background-color: #fff;

		view {
			padding: 2vw;
		}

		.btn {
			background-color: #07c160;
			font-size: 20px;
			padding: 3vw 5vw;
			color: white;

		}
	}

	.address {
		margin-left: 15vw;
		padding: 1vh 1vh 2vh;
	}
</style>