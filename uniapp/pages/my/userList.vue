<template>
	<view>
		<top></top>
		<view class="top">
			<uni-icons class="icon" type="back" size="26" color="white" @click='back'></uni-icons>
			<view class="search">
				<u-search placeholder="搜索用户真名、手机" v-model="keyword" :animation="true" @search="onSearch"
					@custom="onSearch" @clear="onClear">
				</u-search>
			</view>
		</view>

		<view class="container">
			<view class="flex">
				<uni-segmented-control :current="currentType" :values="items" @clickItem="onClickItem" class="right"
					styleType="text" activeColor="#55a5fd"></uni-segmented-control>
				<view class="topBtn">
					<!-- v-show='currentType==1' -->
					<u-button text="添加管理员" type="primary" @click="onAddClick"></u-button>
				</view>
			</view>

			<view class="u-page">
				<u-list @scrolltolower="scrolltolower" height='80vh'>
					<u-list-item v-for="(item, index) in data" :key="index">
						<u-cell :label="'用户名：'+item.nick_name" :title="'真名：'+item.name" :value="'手机：'+item.phone"
							center>
							<u-avatar slot="icon" shape="square" size="35" :src="item.avatar_url"
								customStyle="margin: -3px 5px -3px 0"></u-avatar>
						</u-cell>
					</u-list-item>
				</u-list>
			</view>
			<u-divider :text="'总计用户个数：'+total"></u-divider>

		</view>
	</view>
</template>

<script>
	import top from "@/components/top.vue"
	import * as errorCode from '@/constant/errorCode.js'
	import {
		userList
	} from '@/api/user.js'

	export default {
		components: {
			top
		},
		data() {
			return {
				data: [],
				keyword: '', //搜索关键字
				page: 1,
				maxpage: 0,
				currentType: 0, // 当前选择的分类索引
				items: ['用户', '管理员'],
				total: 0 // 总条数
			}
		},
		onShow() {
			this.loadUsers();
		},
		methods: {
			// 滑到底部，加载更多
			scrolltolower() {
				if (this.page < this.maxpage) {
					this.page++
					this.loadUsers()
				} else {
					this.isBottom = true
				}
			},
			// 切换类型
			onClickItem(e) {
				if (this.currentType != e.currentIndex) {
					this.currentType = e.currentIndex;
					this.data.length = 0
					this.page = 1
					this.loadUsers();
				}
			},
			// 搜索
			onSearch(keyword) {
				console.log('搜索：' + keyword)
				this.data.length = 0
				this.page = 1
				this.keyword = keyword
				this.loadUsers();
			},
			// 清空搜索
			onClear(keyword) {
				this.data.length = 0
				this.page = 1
				this.keyword = ''
				this.loadUsers();
			},
			// 添加管理员
			onAddClick() {
				uni.navigateTo({
					url: '/pages/login/register?isAdmin=true'
				})
			},
			back() {
				uni.navigateBack()
			},
			// 加载用户列表
			async loadUsers() {
				try {
					this.isBottom = false
					let res = await userList({
						type: this.currentType + 1,
						page: this.page,
						keyword: this.keyword
					})
					this.maxpage = res.data.pagecount
					this.total = res.data.total
					this.data = this.data.concat(res.data.list)
				} catch (e) {
					console.error('加载用户列表失败，原因：')
					console.error(e)
					this.showError('加载失败')
				}
			},
			showError(title) {
				uni.showToast({
					icon: 'error',
					mask: true,
					title: title,
					duration: 2000
				})
			},
		}
	}
</script>




<style lang="scss">
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


	.container {
		padding: 1vw 2vw;

		.flex {
			display: flex;

			.right {
				flex: 1;
			}
		}

	}

	.topBtn {
		width: 30%;
	}

	.u-page {
		margin-top: 1vw;
		border-top: 1px #b3b3b3 solid;
	}
</style>