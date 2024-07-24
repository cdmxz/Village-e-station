<template>
	<view>
		<top></top>
		<view class="top">
			<uni-icons type="back" @click="back" size="26" color="white" class="icon"></uni-icons>
			<view class="search">
				<u-search placeholder="搜索商品" v-model="keyword" :animation="true" @search="onSearch" @custom="onSearch">
				</u-search>
			</view>
		</view>
		<swiper :indicator-dots="true" :autoplay="true" :interval="3000" :circular="true">

			<swiper-item v-for="(i,index) in banner" :key="index">
				<view class="swiper-item">
					<image :src="i" mode=""></image>
				</view>
			</swiper-item>

		</swiper>
		<view class="nav bor">
			<navigator url="/pages/shopping/cart">
				<view>
					<text class="icon" style="color: #07c160;">&#x344b;</text>
					<view class="text">
						购物车
					</view>
				</view>
			</navigator>

			<navigator url="/pages/order/index?isadmin=0">
				<view>
					<text class="icon" style="color: #228b22;">&#xe897;</text>
					<view class="text">
						我的订单
					</view>
				</view>
			</navigator>

			<navigator url="/pages/address/index">
				<view>
					<text class="icon" style="color: #ff8c00;">&#xebbb;</text>
					<view class="text">
						收货地址
					</view>
				</view>
			</navigator>
			<view>
				<button class="open-contact" open-type="contact" bindcontact="handleContact">
					<u-icon name="server-man" color="#2979ff" size="32"></u-icon>
					客服
				</button>
				<!-- <text class="icon" style="color: #ff8c00;">&#xebbb;</text> -->
			</view>
		</view>
		<view class="tabbar">
			<view class="shopClass bor">
				<view class="text">
					———— <text>商品分类</text> ————
				</view>

				<view class="btn">
					<navigator v-for="item in category" :url="'/pages/shopping/shop?id='+item.category_id"
						:key="item.category_id">
						<view>
							<!-- <text class="icon">&#xe625;</text> -->
							<text>{{item.name}}</text>
						</view>
					</navigator>
				</view>
			</view>
			<view class="text bor">
				———— <text>商品推荐</text> ————
			</view>
		</view>

		<view class="list">
			<view class="bor" v-for="(i,index) in data" :key='index'>
				<navigator :url="'/pages/shopping/details?id='+i.good_id">
					<image :src="i.thumbnail_url" mode=""></image>
					<view class="">
						{{i.name}}
					</view>

				</navigator>
				<view class="bottom">
					<text>￥{{i.price}}</text>
					<button @click="addCart(i)">加入购物车</button>
				</view>
			</view>

		</view>
		<bottom></bottom>
	</view>
</template>

<script>
	import top from "@/components/top.vue"
	import bottom from "@/components/bottom.vue"
	import {
		GetShopping,
		adminList,
		getCategory
	} from '@/api/shopping.js'
	import {
		goodadd
	} from '@/api/cart.js'
	import {
		getbanner
	} from '@/api/index.js'
	import {
		islogin
	} from '@/api/user.js'

	export default {
		components: {
			bottom,
			top
		},
		data() {
			return {
				data: [],
				banner: [],
				page: 1,
				maxpage: 0,
				isadmin: 0,
				keyword: '',
				category: [] // 商品分类列表
			}
		},
		methods: {
			back() {
				uni.navigateBack()
			},
			addCart(i) {
				if (!islogin())
					return
				goodadd({
					good_id: i.good_id
				}).then(e => {
					if (e.code != 200) {
						this.showError(e.msg)
					} else {
						this.showSuccess('加入购物车成功')
					}
				})
			},
			showError(title) {
				uni.showToast({
					icon: 'error',
					mask: true,
					title: title,
				})
			},
			showSuccess(title) {
				uni.showToast({
					icon: 'success',
					title: title,
					duration: 500
				})
			},
			// 搜索
			onSearch(keyword) {
				console.log('搜索：' + keyword)
				this.keyword = ''
				uni.navigateTo({
					url: '/pages/shopping/shop?keyword=' + keyword
				})
			}
		},
		async mounted() {
			const res = await GetShopping({
				page: 1
			})
			this.data = res.data.list
			this.maxpage = res.data.pagecount
			let banner = await getbanner({
				type: 2
			})
			this.banner = banner.data.img_url
			// 查询商品分类
			let category = await getCategory()
			this.category = category.data
			console.log(this.category)
		},
		async onReachBottom() {
			if (this.page < this.maxpage) {
				let data = this
				if (this.isadmin == 0) {
					const res = await GetShopping({
						page: ++data.page
					})
					this.data = this.data.concat(res.data.list)
				} else {
					const res = await adminList({
						page: ++data.page
					})
					this.data = this.data.concat(res.data.list)
				}

			}
		}
	}
</script>

<style scoped lang="scss">
	.top {
		background-color: #3cb371;
		padding: 0 0 10px 1vh;
		display: flex;
		align-items: center;
		height: 5vh;

		.search {
			margin: 15rpx 0 0 15rpx;
			width: 60%;
		}

		.icon {
			margin-top: 12rpx;
		}
	}

	swiper {
		height: 20vh;

		image {
			width: 100vw;
		}
	}

	.bor {
		box-shadow: 0 0 10px #c8c8c8;
		border: solid #c8c8c8 1px;
		border-radius: 2vw;
		padding: 2vw;
		background-color: #fff;
		margin: 1vh 2vw;
		box-sizing: border-box;
	}

	.nav {
		display: flex;
		justify-content: space-around;

		font-size: 14px;

		view {
			text-align: center;
		}

		.icon {
			font-size: 35px;
			font-weight: 600;
		}

		.text {
			margin-top: 13rpx;
		}
	}

	.open-contact {
		font-size: 14px;
		background-color: #ffffff;
	}

	.open-contact::after {
		border: none; // 去除边框
	}

	.tabbar {
		.text {
			text-align: center;
			color: #c8c8c8;
			margin: 0 2vw;
			padding: 1vh 0;
			font-size: 12px;

			text {
				color: #000;
			}
		}

		.btn {
			font-size: 12px;
			display: flex;
			margin: 1vh 1vw;
			justify-content: space-around;

			view {
				border: 1px solid #c8c8c8;
				padding: 1vw 2vw;
				border-radius: 2vw;
				flex: 1;
				text-align: center;
			}
		}

		.icon {
			font-size: 14px;
		}

	}

	.list {
		text-align: center;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-evenly;
		padding-bottom: 10vh;

		.bor {
			width: 46vw;
			padding: 3vw;
			font-size: 12px;

			image {
				width: 100%;
				height: 150px;
			}

			view {
				margin: 1vh 0;
			}

			.bottom {
				display: flex;
				color: #23b86a;
				justify-content: space-between;
				padding: 0 2vw;
			}

			button {
				font-size: 12px;
				margin: 0;
				padding: 0 1vw;
				background-color: #06b057;
				color: white;
				line-height: 20px;
			}
		}
	}
</style>