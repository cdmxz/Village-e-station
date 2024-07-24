<template>
	<view>
		<view class="top">
			<view class="flex">
				<label class="radio">
					<checkbox value="r1" @click="all" :checked='allbox' />全选
				</label>
				<text class="icon" @click="clear">
					&#xe68e;清空购物车
				</text>
			</view>
		</view>
		<view class="uni-list">
			<checkbox-group @change="checkboxChange">
				<view class="list flex" @longpress="removeShop(item.good_id)" v-for="(item, index) in items"
					:key="item.good_id">
					<view class="radio">
						<checkbox :value="item.name" :checked="checked" />
					</view>
					<image :src="item.thumbnail_url" mode=""></image>
					<view class="flex price">
						<view>{{item.name}}</view>
						<view>￥{{(item.price*item.number).toFixed(2)}}</view>
					</view>
					<view class="btn flex">
						<view class="remove" @click="remove(item)">
							-
						</view>
						<text class="num">
							{{item.number}}
						</text>
						<view class="add" @click="add(item)">
							+
						</view>
					</view>
					<view v-if="item.status==2">
						已下架
					</view>
					<!-- 剩余量小于购买量 -->
					<view v-if="item.status==3 || item.surplus < item.amount">
						库存不足
					</view>
				</view>
			</checkbox-group>




		</view>
		<view class="fixed flex">
			<view class="">
				总价：
				<text>￥{{total}}（不含运费）</text>
			</view>
			<view class="btn" @click="submit()">
				结算
			</view>
		</view>

	</view>
</template>


<script>
	import {
		GetCart,
		UpdataCart,
		clean,
		gooddelete
	} from '@/api/cart.js'
	import {
		islogin,
		information
	} from "@/api/user.js"
	export default {
		data() {
			return {
				num: 1,
				items: [],
				value: [],
				checked: false,
				allbox: false,
				options: [{
					text: '删除',
					style: {
						backgroundColor: '#007aff'
					}
				}],
				page: 1,
				maxpage: 0,
				isAdmin: false
			}
		},
		methods: {
			showError(title) {
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
			submit() {
				// if (this.isAdmin) {
				// 	this.showError('管理员不能下单')
				// 	return
				// }
				if (this.value.length == 0) {
					this.showError('请选择商品')
					return
				}
				let arr = []
				this.value.forEach(e => {
					arr.push(JSON.stringify(this.items.filter(el => el.name == e)[0]))
				})
				let json = '[' + arr.join(',') + ']'
				uni.redirectTo({
					url: '/pages/order/submit?id=' + json + '&total=' + this.total
				})
			},
			checkboxChange(evt) {
				this.value = evt.detail.value
				if (this.value.length == this.items.length) {
					this.allbox = true
					this.checked = true
				} else {
					this.allbox = false
				}
			},
			async add(e) {
				if (e.surplus != e.amount) {
					++e.number
				}
				let resp = await UpdataCart({
					good_id: e.good_id,
					number: e.number
				})
				if (resp.code != 200) {
					this.showError(resp.msg)
					e.number = e.number - 1
					return
				}
			},
			remove(e) {
				console.log(e)
				if (e.number != 1) {
					--e.number
					return
				}
				UpdataCart({
					good_id: e.good_id,
					number: e.number
				})
			},
			all() {
				if (this.checked) {
					this.checked = !this.checked
					this.value = []
				} else {
					this.checked = !this.checked
					this.value = this.items.map(item => item.name)
				}
			},
			clear() {
				let items = this
				uni.showModal({
					title: '提示：',
					content: '请确认是否要清空购物车',
					success: function(res) {
						if (res.confirm) {
							items.items = []
							clean()
						}
					}
				})
			},
			removeShop(e) {
				let data = this
				uni.showModal({
					title: '确定要删除该商品吗',
					success: function(res) {
						if (res.confirm) {
							gooddelete(e).then(e => {
								if (e.code == 200) {
									uni.showToast({
										title: '删除成功',
										success() {
											GetCart({
												page: 1
											}).then(e => {
												data.items = e.data.list
											})
										}
									})
								}
							})
						}
					}
				});
			}
		},
		mounted() {
			information().then(e => {
				this.isAdmin = e.data.is_admin == 1
			})
		},
		onShow() {
			if (!islogin())
				return
			GetCart({
				page: 1
			}).then(e => {
				this.maxpage = e.data.pagecount
				this.items = e.data.list
			})
		},
		computed: {
			total() {
				let num = 0
				this.value.forEach(e => {
					this.items.forEach(a => {
						if (a.name == e) {
							num += a.price * a.number
						}
					})
				})
				return num.toFixed(2)
			}
		}, //下拉加载
		async onReachBottom() {
			let data = this
			if (data.page < data.maxpage) {
				GetCart({
					page: ++data.page
				}).then(e => {
					this.items = this.items.concat(e.data.list)
				})
			}


		}
	}
</script>
<style lang="scss" scoped>
	.top {
		background-color: #fff;
		padding: 2vw;

		.flex {
			justify-content: space-between;
		}

		.icon {
			color: #c33164;
		}
	}

	.uni-list {
		background-color: #efefef;
		padding-top: 1vh;
		min-height: 90vh;
		padding-bottom: 5vh;
	}

	.list {
		position: relative;
		padding: 3vw;
		background-color: #fff;
		margin: 2vh 0;

		.radio {
			align-self: center;
		}

		image {
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
			right: 2vw;

			text {
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

	.fixed {
		position: fixed;
		bottom: 0;
		width: 100%;
		justify-content: space-between;
		background-color: #fff;
		align-items: center;

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

	.overflow {
		width: 120vw;
		display: flex;
	}
</style>