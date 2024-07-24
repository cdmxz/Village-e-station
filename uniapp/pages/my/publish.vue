<template>
	<view>
		<view class="white">
			<uni-segmented-control :current="current" :values="items" @clickItem="onClickItem" styleType="text"
				activeColor="#55a5fd"></uni-segmented-control>
		</view>
		<view class="content">
			<!-- 问题列表 -->
			<view v-show="current==0">
				<u-empty mode="search" :show="articleIsEmpty"></u-empty>
				<navigator :url="'/pages/article/articleDetails?id='+i.article_id" v-for="(i,index) in data"
					:key='index' v-show="!articleIsEmpty">
					<view class="list">
						<!-- 删除图标 -->
						<view class="remove icon" @click.native.stop="onClickDelete(i.article_id)">&#xe68e;</view>

						<view class="title">{{i.title}}
						</view>
						<view class="dateTime">
							<text>{{i.date_created}}</text>
							<text>
								<text class="icon">&#xe618;</text>
								<text>{{i.read_number}}</text>
							</text>
						</view>
					</view>

				</navigator>
			</view>
			<!-- 评论列表 -->
			<view v-show="current==1">
				<!-- 查询的数据 为空 时显示 -->
				<u-empty mode="search" :show="commentIsEmpty">
				</u-empty>
				<!-- 查询的数据 不为空 时显示 -->
				<view class="list" v-for="(i,index) in data" :key='index' v-show="!commentIsEmpty">
					<view class="top flex">
						<!-- 用户信息 -->
						<view class="flex">
							<image class="avatar" :src="userImg" mode="scaleToFill"></image>
							<view class="middle">
								<view class="name">{{userName}}</view>
								<view class="dateTime">{{i.comment_date_created}}</view>
							</view>
						</view>
						<!-- 删除图标 -->
						<view class="remove icon" @click.native.stop="onCommentDelete(i.comment_id)">&#xe68e;</view>
					</view>
					<view class="flex">
						<!-- 评论内容 -->
						<view class="comment">{{i.comment}}</view>
						<text v-if="i.status==1" class="status-waiting">待审核</text>
						<text v-else-if="i.status==3" class="status-failed">审核失败</text>
						<!-- 	<text v-else class="status-success">审核成功</text> -->
					</view>
					<image v-if="i.img_urls.length!=0" class="commentImg" v-for="(item,index) in i.img_urls "
						:key="index" :src="item" mode="" @click="previewImg(item)">
					</image>
					<navigator :url="'/pages/article/articleDetails?id='+i.article_id">
						<view class="gray">
							<text class="commentTitle">{{i.article_title}}</text>
							<text class="dateTime">{{i.comment_date_created}}</text>
						</view>
					</navigator>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		MyPublish,
		mycomment,
		deleteArticle,
		deleteComment
	} from '@/api/article.js'

	export default {
		data() {
			return {
				current: 0,
				items: ['问题', '评论'],
				data: [],
				userImg: '',
				userName: '',
				commentIsEmpty: true,
				articleIsEmpty: true,
				page: 1,
				maxpage: 0
			}
		},
		methods: {
			showError(title) {
				uni.hideLoading()
				uni.showToast({
					mask: true,
					icon: 'error',
					title: title
				})
			},
			showSuccess(title) {
				uni.hideLoading()
				uni.showToast({
					icon: 'success',
					title: title
				})
			},
			previewImg(url) {
				uni.previewImage({
					urls: [url]
				})
			},
			onClickItem(e) {
				if (this.current != e.currentIndex) {
					this.current = e.currentIndex;
					this.page = 1
					this.loadList()
				}
			},
			onClickDelete(id) {
				let that = this
				uni.showModal({
					title: '提示',
					content: '确认删除该问题吗？',
					success: async function(res) {
						if (res.confirm) {
							// 显示加载框
							uni.showLoading({
								mask: true
							})
							var resp = await deleteArticle(id)
							if (resp.code != 200) {
								that.showError(resp.msg)
							} else {
								uni.redirectTo({
									url: '/pages/my/publish?current=0'
								})
							}
						}
					}
				})
			},
			onCommentDelete(comment_id) {
				let that = this
				uni.showModal({
					title: '提示',
					content: '确认删除该评论吗？',
					success: async function(res) {
						if (res.confirm) {
							// 显示加载框
							uni.showLoading({
								mask: true
							})
							var resp = await deleteComment(comment_id)
							if (resp.code != 200) {
								that.showError(resp.msg)
							} else {
								uni.redirectTo({
									url: '/pages/my/publish?current=1'
								})
							}
						}
					}
				})
			},
			async loadList(type = this.current, page = 1, bottom) {
				try {
					if (type == 0) {
						let res = await MyPublish({
							page: page
						})
						if (res.code != 200) {
							throw res
						}
						this.maxpage = res.data.pagecount
						if (bottom) {
							this.data = this.data.concat(res.data.list)
						} else {
							this.data = res.data.list
						}
						this.articleIsEmpty = (res.data.pagecount == 0)
					} else {
						let res = await mycomment({
							page: page
						})
						if (res.code != 200)
							throw res
						this.maxpage = res.data.pagecount
						this.commentIsEmpty = (res.data.pagecount == 0)
						this.userImg = res.data.avatar_url
						this.userName = res.data.user
						if (bottom) {
							this.data = this.data.concat(res.data.list)
						} else {
							this.data = res.data.list
						}
						var s = res.data.list
						console.log(s)
					}
				} catch (e) {
					console.error('加载我的发布失败，原因：')
					console.error(e)
					this.showError('加载失败')
				}
			}
		},
		mounted() {
			this.loadList()
		},
		onLoad(e) {
			if (typeof e.current === 'string') {
				this.current = Number(e.current)
			}
		},
		async onReachBottom() {
			if (this.page < this.maxpage) {
				this.loadList(this.current, ++this.page, true)
			}

		}
	}
</script>

<style lang="scss">
	.white {
		background-color: #fff;
		border-bottom: #cbcbcb 2px solid;
	}

	.content {

		// 头像
		.avatar {
			width: 17vw;
			height: 17vw;
			border-radius: 50%;
			border: 1px #cbcbcb solid;
		}

		.middle {
			align-self: center;

			.name {
				margin-left: 20rpx;
				font-weight: bold;
			}
		}

		.top {
			justify-content: space-between;
		}

		.flex {
			display: flex;
		}

		.status-waiting {
			color: #ffb805;
			font-weight: bold;
		}

		.status-failed {
			color: #F56C6C;
			font-weight: bold;
		}

		.status-success {
			color: #3eba00;
			font-weight: bold;
		}

		.list {
			background-color: #fff;
			font-size: 16px;
			padding: 2vh 2vw;
			border-radius: 20rpx;
			border: #cbcbcb 1px solid;

			position: relative;

			.gray {
				background-color: #ececec;
				border: #e3e3e3 1px solid;
				padding: 2vw 0 2vw 2vw;
				display: flex;
				justify-content: space-between;
			}

			.comment {
				padding: 4vh 4vw 2vh 4vw;
				width: 100%;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}

			.remove {
				position: absolute;
				color: #c54b3f;
				font-size: 26px;
				text-align: right;
				font-weight: bold;
				right: 20rpx;
			}

			.title {
				margin: 0 0 5vw 2vw;
				font-weight: bold;
				font-size: 20px;
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
			}

			.commentTitle {
				overflow: hidden;
				text-overflow: ellipsis;
				white-space: nowrap;
				width: 100%;
			}

			.commentImg {
				height: 20vw;
				width: 20vw;
				object-fit: contain;
				margin-left: 2vw;
			}

			.dateTime {
				display: flex;
				justify-content: space-between;
				margin: 0 0 0 3vw;
				color: #878787;

				.icon {
					font-size: 25px;
				}
			}

			margin: 2vw;
		}

	}
</style>