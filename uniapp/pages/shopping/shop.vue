<template>
	<view>
		<top></top>
		<!-- 头部 -->
		<view class="top">
			<uni-icons type="back" @click="back" size="26" color="white" class="icon"></uni-icons>
			<!-- <view  :class="{'search':!isadmin,'search-ad':isadmin}"> -->
			<view class="search" :style="{'width':isadmin?'45%':'50%'}">
				<u-search placeholder="搜索商品" v-model="keyword" :animation="true" @search="onSearch" @custom="onSearch"
					@clear="onClear">
				</u-search>
			</view>
			<!-- 管理员显示添加商品 -->
			<view class="topAdd" v-if="isadmin">
				<navigator :url="'/pages/shopping/shopUpdate?isAddType=true'">
					<button class="add" size='mini'>添加商品</button>
				</navigator>
			</view>
		</view>

		<view class="tabbar">
			<view class="select">
				<uni-data-select v-model="value" :localdata="dataTitle" @change="change" class="list" :clear="false">
				</uni-data-select>
			</view>
			<view :class="composite === 1 ? 'tabActive' : ''" @click="active(0)" class="tabItem">
				综合
			</view>
			<view :class="salesdesc === 1 ? 'tabActive' : ''" @click="active(1)" class="tabItem">
				销量
			</view>
			<view :class="pricedesc === 1 || pricedesc === 0? 'tabActive' : ''" @click="activePrice">
				<view class="flexTop">
					<view>价格</view>
					<u-icon :name="pricedesc === 0?'arrow-up':'arrow-down'" color="black" size="14"></u-icon>
				</view>
			</view>

		</view>
		<!-- 用户页面 -->
		<view class="data" v-if="!isadmin">
			<view class="bor" v-for="(i,index) in data" :key='index'>
				<navigator :url="'/pages/shopping/details?id='+i.good_id">
					<image :src="i.thumbnail_url" mode=""></image>
					<view>
						{{i.name}}
					</view>

				</navigator>
				<view class="bottom">
					<text>￥{{i.price}}</text>
					<button @click="addCart(i)">加入购物车</button>
				</view>
			</view>

		</view>
		<!-- 管理员页面 -->
		<view class="admin" v-else>
			<view class="bor" v-for="(i,index) in data" :key='index'>
				<view class="img">
					<navigator :url="'/pages/shopping/details?id='+i.good_id">
						<image :src="i.thumbnail_url" mode="scaleToFill"></image>
						<text>{{i.name}}</text>
					</navigator>
				</view>
				<view class="right">
					<view>
						<text>价格</text><text class='price'>￥{{i.price}}</text>
					</view>
					<view>
						<text>近30填销量</text><text>{{i.recent_sales}} 件</text>
					</view>
					<view>
						<text>总销量</text><text>{{i.sales_quantity}} 件</text>
					</view>
					<view>
						<text>剩余库存</text><text>{{i.surplus}} 件</text>
					</view>
					<view>
						<text>原始库存</text><text>{{i.stock}} 件</text>
					</view>
					<view>
						<text class="status" style="color: #06b057;" v-if="i.status==1">在售</text>
						<text class="status" style="color: #ff8c18;" v-else-if="i.status==2">下架</text>
						<text class="status" style="color: #ff5946;" v-else>库存不足</text>
						<navigator :url="'/pages/shopping/shopUpdate?id='+i.good_id"><button type="primary">修改</button>
						</navigator>
					</view>
				</view>


			</view>
		</view>

	</view>
</template>

<script>
	import top from "@/components/top.vue"
	import {
		GetShopping,
		listbytype,
		adminType,
		adminList,
		getCategory
	} from "@/api/shopping.js"
	import {
		goodadd
	} from '@/api/cart.js'
	import {
		information,
		islogin
	} from "@/api/user.js"
	export default {
		components: {
			top
		},
		data() {
			return {
				data: '',
				dataTitle: [{
					value: 0,
					text: '全部'
				}],
				value: 0,
				isadmin: false,
				page: 1,
				maxpage: 0,
				pricedesc: -1, // 排序 -1表示不排序 0为升序排序 1为降序排序
				salesdesc: -1,
				composite: 1,
				keyword: '',
				paramValue: 1 // 传过来的分类value值
			}
		},
		methods: {
			//加载数据
			async onload(page = 1, bottom = 0) {
				//参数2 触底加载
				let res
				let params = {
					page: page,
					category_id: this.value,
					salesdesc: this.salesdesc,
					pricedesc: this.pricedesc,
					keyword: this.keyword
				}
				if (this.value == 0) {
					// 查询所有商品
					if (this.isadmin) {
						res = await adminList(params)
					} else {
						res = await GetShopping(params)
					}
				} else {
					// 根据分类查询
					if (this.isadmin) {
						res = await adminType(params)
					} else {
						res = await listbytype(params)
					}
				}
				if (res.code != 200) {
					this.showError(res.msg)
					return
				}
				if (bottom == 0) {
					this.maxpage = res.data.pagecount
					this.data = res.data.list;
					this.page = 1
				} else {
					this.data = this.data.concat(res.data.list)

				}
			},
			back() {
				uni.navigateBack()
			},
			addCart(i) {
				if (!islogin()) return
				goodadd({
					good_id: i.good_id
				}).then(e => {
					uni.showToast({
						title: '加入购物车成功'
					})
				})
			},
			// 根据价格排序，单独一个方法
			activePrice() {
				this.composite = -1
				this.salesdesc = -1
				if (this.pricedesc == 0) {
					this.pricedesc = 1
				} else if (this.pricedesc == -1 || this.pricedesc == 1) {
					this.pricedesc = 0
				}
				this.onload(1)
			},
			active(e) {
				// 排序 -1表示不排序
				// 0为升序排序 1为降序排序
				this.composite = -1
				this.pricedesc = -1
				this.salesdesc = -1
				if (e == 0) { // 综合
					this.composite = 1
				} else if (e == 1) { // 销量
					this.salesdesc = 1
				}
				this.onload(1)
			},
			change(e) {
				this.onload(1)
			},
			// 搜索
			onSearch(keyword) {
				this.keyword = keyword
				this.onload(1)
			},
			onClear() {
				this.keyword = ''
				this.onload(1)
			},
			showError(title) {
				uni.showToast({
					icon: 'error',
					mask: true,
					title: title,
				})
			},
		},
		async mounted() {
			// 查询商品分类
			let resp = await getCategory()
			let category = resp.data
			category.forEach((item) => {
				this.dataTitle.push({
					value: parseInt(item.category_id),
					text: item.name
				})
			});
			// 
			let da = this.dataTitle
			console.log(da)
		},
		onShow() {
			this.onload()
		},
		onLoad(e) {
			if (typeof e.id !== 'undefined') {
				let id = e.id
				this.value = parseInt(id)
			}
			if (typeof e.keyword !== 'undefined' && e.keyword.trim() !== '') {
				this.keyword = e.keyword
			}
			this.isadmin = Boolean(e.isadmin)
		},
		//下拉加载
		async onReachBottom() {
			if (this.page < this.maxpage) {
				this.onload(++this.page, 1)
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
			margin: 15rpx 0 0 10rpx;
			width: 50%;
		}

		.icon {
			margin-top: 12rpx;
		}

		.add {
			background-color: #ffa63b;
			color: white;
			margin: 0 1vw;
			width: 18vw;
			padding: 0;
		}

		.topAdd {
			margin-top: 4vw;
		}
	}

	.article {
		margin: 5vw;
		padding: 1vw;
		height: 21vh;
		position: relative;

		image {
			width: 100%;
			height: 17vh;
		}

		.artCont {

			font-weight: 600;
			font-size: 14px;
			padding: 0 2vw;
			text-overflow: ellipsis;
			overflow: hidden;
			white-space: nowrap;
		}

		border: #d7d7d7 solid 1px;
		border-radius: 3vw;

	}

	.tabbar {
		border-bottom: #bbbbbb 1px solid;
		padding: 1vw 3vw;
		justify-content: space-between;
		display: flex;
		align-items: center;

		.tabItem {
			padding: 2vw;
		}

		.select {
			width: 30%;
		}

		.flexTop {
			display: flex;
			align-items: center;
			padding: 2vw 1vw;
		}
	}

	.tabActive {
		background-color: #ff8c00;
		color: white;
	}

	.data {
		text-align: center;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-evenly;
		padding-bottom: 10vh;
	}

	.bor {
		width: 46vw;
		padding: 3vw;
		font-size: 12px;
		box-shadow: 0 0 10px #c8c8c8;
		border: solid #c8c8c8 1px;
		border-radius: 2vw;
		padding: 2vw;
		background-color: #fff;
		margin: 1vh 2vw;
		box-sizing: border-box;

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

	.admin {
		.bor {
			font-size: 14px;
			display: flex;
			width: 90vw;
			margin: 3vh auto;
			align-items: center;

			image {
				box-sizing: border-box;
				border-radius: 5vw;
				vertical-align: middle;

			}

			.img {
				text-align: center;
				flex: 1;
				margin: 0 2vw;
			}

			.right {
				.status {
					font-size: 16px;
					font-weight: 600;
				}

				flex: 1;
				padding-left: 2vw;
				border-left: 2px solid #ddd;

				view {
					display: flex;
					justify-content: space-between;
					margin-right: 5vw;

					button {
						padding: 1vw 5vw;
					}

					.price {
						color: #06b057;
					}
				}
			}
		}
	}
</style>