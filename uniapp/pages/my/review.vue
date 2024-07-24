<template>
	<view class="content">
		<u-empty mode="list" :show="commentIsEmpty">
		</u-empty>

		<!-- 查询的数据 不为空 时显示 -->
		<view class="list" v-for="i in data" :key='i.comment_id'>
			<view class="top flex">
				<!-- 用户信息 -->
				<view class="flex">
					<image class="avatar" :src="i.avatar_url" mode=""></image>
					<view class="middle">
						<view class="name">{{i.user}}</view>
						<view class="dateTime">{{i.date_created}}</view>
					</view>
				</view>
			</view>
			<view class="flex">
				<view class="comment">{{i.comment}}</view>
			</view>
			<navigator :url="'/pages/article/articleDetails?id='+i.article_id" v-show="!commentIsEmpty">
				<view class="gray">
					<text class="commentTitle">{{i.article_title}}</text>
					<text class="dateTime">{{i.date_created}}</text>
				</view>
			</navigator>
			<image v-if="i.img_urls.length!=0" class="commentImg" v-for="(item,index) in i.img_urls " :key="index"
				:src="item" mode="scaleToFill" @click="previewImg(item)">
			</image>
			<view class="flex btn">
				<button type="primary" @click="update(i.comment_id,2)">通过</button>
				<button type="warn" @click="update(i.comment_id,3)">拒绝</button>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		reviewList,
		review
	} from "@/api/comment.js"
	export default {
		data() {
			return {
				data: [],
				page: 1,
				maxpage: 0,
				commentIsEmpty: true
			};
		},
		methods: {
			previewImg(url) {
				uni.previewImage({
					urls: [url]
				})
			},
			async update(id, stu) {
				let obj = {
					comment_id: id,
					status: stu
				}
				// 修改状态
				let res = await review(obj)
				if (res.code != 200) {
					this.showError(res.msg)
					return
				}
				uni.showToast({
					title: '审核成功',
					duration: 500
				})
				await this.loadList()
			},
			// 查询待审核列表
			async loadList(page = 1, isBottom = false) {
				// 查询待审核列表
				let res = await reviewList()
				if (res.code != 200) {
					this.showError(res.msg)
					return
				}
				if (isBottom) {
					this.data = this.data.concat(res.data.list)
				} else {
					this.page = 1
					this.data = res.data.list
					this.maxpage = res.data.pagecount
					this.commentIsEmpty = (res.data.pagecount == 0)
				}
			},
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
		},
		async mounted() {
			await this.loadList()
		},
		onReachBottom() {
			if (this.page < this.maxpage) {
				this.loadList(++this.page, true)
			}
		}
	}
</script>

<style lang="scss">
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

			.btn {
				width: 50%;
				margin-top: 2vh;
			}
		}

	}
</style>