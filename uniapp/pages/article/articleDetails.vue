<!-- 文章详情、问题详情 -->
<template>
	<view class="main">
		<!-- 内容部分 -->
		<view>
			<text class="title">
				{{ data.title }}
			</text>
			<view class="note">
				<text>发布：{{data.author}}</text>
				<text>时间:</text>
				<text>{{ data.date_created }}</text>
				<text>阅读量:</text>
				{{ data.read_number }}
			</view>
			<view class="content">
				<u-parse :content="data.content"></u-parse>
			</view>
		</view>

		<!-- 评论部分 只在文章分类为 问题答疑 显示-->
		<view v-if="data.article_type==4" class="comment">
			<uni-easyinput v-model="value" placeholder="请输入评论"></uni-easyinput>
			<view class="userComment" v-if="inputText!=''">
				<u-upload :fileList="imgUrls" @afterRead="addImg" @delete="deleteImg" :previewFullImage="true"
					:maxCount="2"></u-upload>
			</view>
			<!-- 发布评论控件 -->
			<view class="control">
				<button type="primary" @click="submit">发表</button>
			</view>
			<!-- 评论内容 -->
			<view>
				<view class="title">
					评论({{comment.length}})
				</view>
				<view class="list" :style="{paddingBottom:isadmin?'6vh':'1vh'}" v-for="i in comment"
					:key="i.comment_id">
					<image :src="i.avatar_url" mode="scaleToFill" class="user" @click="previewImg(i.avatar_url)">
					</image>
					<view class="right">
						<view class="userName">{{i.user}}</view>
						<view>{{i.comment}}</view>
						<view class="commentImgBox">
							<image class="commentImg" v-for="(item,index) in i.img_urls " :key="index" :src="item"
								mode="" @click="previewImg(item)">
							</image>
						</view>
					</view>

					<!-- 当前用户不是管理员，且是本人评论，可删除 -->
					<view v-if="!isadmin">
						<view class="remove" v-if="i.is_my_publish">
							<button type="warn" size="mini" @click="remove(i.comment_id)">删除</button>
						</view>
					</view>

					<!-- 管理员可删除所有人评论 -->
					<view class="remove" v-if="isadmin">
						<button type="warn" size="mini" @click="remove(i.comment_id)">删除评论</button>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>


<script>
	import oss from '@/api/oss.js'
	import * as errorCode from "@/constant/errorCode.js"
	import * as ossUploadPath from "@/constant/ossUploadPath.js"
	import generateRandomFileName from '@/utils/fileUtils.js'
	import {
		ArtDetails
	} from "@/api/article.js"
	import {
		list,
		publish,
		commentDel
	} from "@/api/comment.js"
	import {
		information,
		islogin
	} from "@/api/user.js"
	import {
		textReview
	} from '@/api/review'
	import * as reviewType from '@/constant/reviewType.js'


	export default {
		data() {
			return {
				value: '', //发布评论输入框
				imgUrls: [], //发布评论已选择的图片
				comment: '', //评论列表
				article_id: '',
				isadmin: false,
				data: {},
				path: 1,
				maxpath: 0,
				imageProp: {
					mode: 'aspectFit'
				},
				tmplIds: [
					'HjMKkjOpzKiy70Mdvo_0-2oXHBraqdLHH-wBJGBz0R8', // 评论删除提醒
				]
			}
		},
		methods: {
			// 判断是否为管理员
			async isAdmin() {
				var isAdmin = false
				try {
					var res = await information();
					if (res.code != errorCode.SUCCESS) {
						throw res.msg;
					}
					isAdmin = res.data.is_admin
				} catch (e) {
					console.error("获取用户信息失败，原因：" + e)
				}
				return isAdmin
			},
			showError(title) {
				uni.hideLoading()
				uni.showToast({
					icon: 'error',
					mask: true,
					title: title
				})
			},
			showSuccess(title) {
				uni.hideLoading()
				uni.showToast({
					icon: 'success',
					title: title,
					duration: 500
				})
			},
			// 批量上传图片
			async uploadImgs() {
				let result = []
				for (var i = 0; i < this.imgUrls.length; i++) {
					let imgUrl = this.imgUrls[i].url
					if (imgUrl.startsWith('https')) {
						continue
					}
					let randomFile = await generateRandomFileName(imgUrl)
					let objName = ossUploadPath.COMMENT + randomFile
					let res = await oss.uploadImageWithPromise({
						url: objName,
						imgPath: imgUrl
					})
					if (res.isSuccess) {
						result.push(res.url)
						this.imgUrls[i].url = res.url
					} else {
						console.error('上传图片失败，原因：')
						console.error(e)
					}
				}
				return result
			},
			previewImg(url) {
				uni.previewImage({
					urls: [url]
				})
			},
			//发表评论
			async submit() {
				if (!islogin()) {
					return
				}
				let that = this
				if (this.inputText == '') {
					this.showError('请输入评论')
					return;
				}
				uni.showLoading({
					mask: true,
					title: '发布中...'
				})
				let resp = await textReview({
					text: this.inputText,
					type: reviewType.COMMENT_DETECTION
				})
				if (resp.code == errorCode.SUCCESS) {
					let tips = resp.data.risk_tips
					if (tips != "") {
						this.showError("评论包含：" + tips)
						return
					}
				}
				// 上传图片
				let ossUrls = await this.uploadImgs();
				var res = await publish({
					article_id: that.article_id,
					comment: that.inputText,
					img_urls: ossUrls
				})
				if (res.code != errorCode.SUCCESS) {
					that.showError(res.msg);
				} else {
					that.showSuccess('发布成功')
					that.imgUrls = []
					that.inputText = ''
					await that.loadComment();
				}
			},
			//删除评论
			remove(e) {
				let that = this
				uni.showModal({
					title: '提示',
					content: '是否要删除该评论？',
					success: async (res) => {
						if (res.confirm) {
							var res = await commentDel(e)
							if (res.code != errorCode.SUCCESS) {
								that.showError(res.msg);
							} else {
								this.showSuccess('删除成功')
								await that.loadComment();
							}
						}
					}
				})
			},
			// 加载评论列表
			async loadComment(page = 1) {
				let res = await list({
					article_id: this.article_id,
					page: page
				})
				if (res.code != 200) {
					this.showError(res.msg)
				} else {
					this.maxpage = res.data.pagecount
					this.comment = res.data.list
				}
			},
			// 删除图片
			deleteImg(event) {
				this.imgUrls.splice(event.index, 1)
			},
			// 新增图片
			addImg(event) {
				let lists = [].concat(event.file)
				lists.map((item) => {
					this.imgUrls.push({
						...item,
						status: 'success',
						message: ''
					})
				})
			},
		},
		async onLoad(obj) {
			this.isadmin = await this.isAdmin()
			// 加载文章内容
			//this.article_id = '1740340760856858625'
			this.article_id = obj.id
			let res = await ArtDetails({
				article_id: this.article_id
			})
			if (res.code != 200) {
				this.showError(res.msg)
				return
			}
			this.data = res.data
			this.loadComment()
		},
		async onReachBottom() {
			if (this.page < this.maxpage) {
				this.loadComment(++this.page)
			}
		},
		computed: {
			inputText: {
				get: function() {
					return this.value.trim()
				},
				set: function(value) {
					this.value = value
				}
			}
		}
	}
</script>

<style scoped lang="scss">
	.userInfo {
		display: flex;
		justify-content: flex-end;
		border-radius: 2vw;
	}

	.main {
		margin: 1vh 5vw;

		.title {
			font-weight: bold;
			line-height: 28px;
		}

		.note {
			color: #c7c7c7;
			font-size: 12px;
			border-bottom: 1px solid #c7c7c7;
			text-align: right;
			margin: 1vh 0 2vh;
			padding-bottom: 1vh;

			text {
				margin-right: 2vw;
			}
		}

		// 评论部分样式
		.content {
			margin-bottom: 10vw;
		}

		.comment {
			border-top: 1px solid #ddd;
			padding-top: 5vw;
		}

		.control {
			border-top: 1px solid #ddd;
			padding-top: 1vh;
			display: flex;
			justify-content: flex-end;

			button {
				margin: 0;
				margin-left: 5vw;
			}
		}

		.userComment {
			padding: 1vh 0;
			display: flex;

			image {
				height: 15vh;
				width: 15vh;

			}
		}

		.list {
			position: relative;
			display: flex;
			padding-bottom: 1vh;
			border-bottom: 1px solid #ddd;

			.remove {
				position: absolute;
				right: 0;
				bottom: 1vh;
			}
		}

		.user {
			width: 10vw;
			height: 10vw;
			border-radius: 50%;
			margin-right: 5vw;
		}

		.right {
			width: 75vw;

			.userName {
				font-weight: bold;
			}

			.commentImgBox {
				padding-bottom: 3vh;

				.commentImg {
					height: 20vw;
					width: 20vw;
					object-fit: contain;
					margin-left: 2vw;
				}
			}
		}
	}
</style>