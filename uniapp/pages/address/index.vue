<template>
	<view class="main">
		<radio-group @change="change">
			<view class="list" v-for="i in list" :key="i.address_id">
				<view class="top">
					<view class="flex">
						<view class="left">
							<text>{{i.consignee}}</text>
							<text>{{i.phone}}</text>
						</view>
						<view class="" @click="thisremove(i.address_id)">
							x
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
						<navigator :url="'/pages/address/edit?updata='+JSON.stringify(i)">
							<view class="bor">
								修改
							</view>
						</navigator>
					</view>

				</view>
			</view>
		</radio-group>
		<view class="btn">
			<navigator :url="'/pages/address/add?updata='">
				<button type='warn'>新建地址</button>
			</navigator>
		</view>
	</view>
</template>

<script>
	import {
		addressList,
		remove,
		updatedefault
	} from '@/api/address.js'

	import {
		islogin
	} from "@/api/user.js"
	export default {
		data() {
			return {
				list: []
			}
		},
		methods: {
			change(e) {
				updatedefault({
					address_id: e.detail.value
				}).then(e => {})
			},
			thisremove(e) {
				let data = this
				uni.showModal({
					title: '提示：',
					content: '请确认是否要删除',
					success: function(res) {
						if (res.confirm) {
							remove(e).then(e => {
								if (e.code == 200) {
									uni.showToast({
										title: '删除成功',
										success() {
											addressList().then(e => {
												data.list = e.data
											})
										}
									})
								} else {
									uni.showToast({
										title: '删除失败',
										mask: true
									})
								}
							})
						}
					}
				})
			}
		},
		onShow() {
			if (!islogin()) {
				return
			}
			addressList().then(e => {
				this.list = e.data
			})
		}
	}
</script>

<style lang="scss" scoped>
	.main {
		background-color: #efefef;

		.list {
			background-color: #fff;
			margin: 2vw 0;
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

		.btn {
			margin: 10vw 5vw;
		}
	}
</style>