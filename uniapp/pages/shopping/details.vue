<template>
	<view class="main" v-if='data'>
		<view>
			<swiper class="mainImg" :indicator-dots="true" :autoplay="true" circular="true" :interval="3000"
				:duration="1000">
				<swiper-item v-for="i in data.rotation_urls" :key="i">
					<view class="swiper-item">
						<image :src="i" mode="aspectFit" @click="previewImg(i)"></image>
					</view>
				</swiper-item>

			</swiper>

			<view class="price">
				<text>
					￥{{data.price}}
				</text>
			</view>
			<view class="name">
				<text>
					{{ data.name}}
				</text>
				<view class="right">
					<text>免运费</text>
					<text>销量{{data.sales_quantity}}份</text>
				</view>
			</view>
		</view>
		<view class="distribution">
			<view>
				<text>配送</text>
				<text>商家配送</text>

			</view>
			<view class="gt">
				&gt;
			</view>
		</view>
		<view class="description">
			<u-parse :content="data.description"></u-parse>
		</view>
		<uni-popup ref="popup" background-color="#fff" @change="change">
			<view class="popup">
				<image :src="data.thumbnail_url" mode=""></image>
				<view class="">
					<view class="price">
						<text>￥{{data.price}}</text>
					</view>
					<view class="">
						库存:{{data.surplus}} 件
					</view>
					<view class="btn flex">
						数量
						<view class="remove" @click="this.number!=1?--this.number:this.number">
							-
						</view>
						<text class="num">
							{{number}}
						</text>
						<view class="add" @click="this.number<data.surplus?++this.number:this.number">
							+
						</view>

					</view>
					<view class="" @click="">
						<button @click="addcart()">加入购物车</button>
					</view>
				</view>
			</view>
		</uni-popup>
		<view class="bottom">
			<button @click="change('bottom')">加入购物车</button>
			<button type="" @click="submit()">立即购买</button>
		</view>
	</view>
</template>

<script>
	import {
		ShopDetails
	} from '@/api/shopping.js'
	import {
		goodadd,
		goodnumber,
		UpdataCart
	} from '@/api/cart.js'
	import {
		islogin
	} from "@/api/user.js"
	export default {
		data() {
			return {
				data: {},
				number: 1,
				good_id: ''
			}
		},
		methods: {
			previewImg(url) {
				uni.previewImage({
					urls: [url]
				})
			},
			change(e) {
				this.$refs.popup.open(e)
			},
			//加入购物车
			addcart() {
				if (!islogin())
					return
				let that = this

				goodnumber({
					good_id: that.good_id
				}).then(e => {
					if (e.code != 3008) {
						// 购物车里有此商品
						UpdataCart({
							good_id: that.good_id,
							number: e.data.number + that.number
						}).then(e => {
							if (e.code != 200) {
								that.showError(e.msg)
							}
						})
					} else {
						// 只添加一个数量
						goodadd({
							good_id: that.good_id
						}).then(e => {
							if (e.code != 200) {
								that.showError(e.msg)
								return
							}
							if (that.number > 1) {
								UpdataCart({
									good_id: that.good_id,
									number: that.number
								}).then(e => {
									if (e.code != 200) {
										that.showError(e.msg)
										return
									}
								})
							}
						})
					}
					uni.showToast({
						title: '加入购物车',
						success() {
							that.$refs.popup.close()
						}
					})
				})
			},
			submit() {
				if (!islogin())
					return
				let data = this
				console.log(this.data)
				let obj = [{
					thumbnail_url: data.data.thumbnail_url,
					name: data.data.name,
					price: data.data.price,
					number: 1,
					good_id: data.data.good_id

				}]
				uni.navigateTo({
					url: '/pages/order/submit?id=' + JSON.stringify(obj) + '&total=' + obj[0].price
				})
			},
			showError(title) {
				uni.showToast({
					icon: 'error',
					mask: true,
					title: title,
				})
			},
		},
		async onLoad(obj) {
			this.good_id = obj.id
			try {
				const res = await ShopDetails({
					good_id: obj.id
				})
				if (res.code != 200) {
					this.showError('加载商品详情失败')
				} else {
					this.data = res.data
				}
			} catch (e) {
				console.error('加载商品详情失败')
				console.error(e)
				this.showError('加载商品详情失败')
			}
		}
	}
</script>

<style scoped lang="scss">
	.main {
		padding-bottom: 10vh;

		.description {
			margin: 0 2vw 0 0;
		}

		.gt {
			color: #cacaca;
			font-weight: 600;
			font-size: 20px;
		}

		view {
			background-color: #fff;

			text {
				margin: 0 3vw;
			}
		}

		.mainImg {
			height: 70vh;
			width: 100%;

			image {
				height: 70vh;
				width: 100%;
			}
		}

		.box1 {
			color: white;
		}

		margin: 2vw;

		.price {
			color: #3cb371;
			padding-bottom: 1vh;
			border-bottom: 1px solid #cacaca;
		}

		.name {
			display: flex;
			justify-content: space-between;
			padding: 2vh 0;

			.right {
				color: #cacaca;

			}
		}

		.distribution {
			display: flex;
			justify-content: space-between;
			padding: 1vw 1vw 1vw 0;
			margin: 1vh 0;

		}

		.bottom {
			position: fixed;
			bottom: 0;
			background-color: #fff;
			display: flex;
			justify-content: flex-end;
			width: 100%;
			z-index: 2;

			button {
				margin: 0;
				color: white;
				margin: 1vw 2vw;
				font-size: 16px;
			}

			button:nth-child(1) {
				background-color: #ffa63b;
			}

			button:nth-child(2) {
				background-color: #07c160;
			}
		}

		.popup {
			image {
				width: 40vw;
				height: 40vw;
			}

			view {
				margin: 2vh 0;
			}

			.price {
				border: none;
				font-weight: 600;

			}

			.btn {
				view {
					background-color: #07c160;
					padding: 0 1vw;
					color: white;
					margin: 0 2vw;
				}


			}

			display: flex;
			justify-content: space-around;
			padding: 10vw 8vw;
		}
	}
</style>