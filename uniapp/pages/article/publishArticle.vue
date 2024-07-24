<template>
	<view>
		<view class="outline">
			<!-- 管理员才显示 -->
			<view class="type" v-if="isAdmin">
				<u-tag text="文章分类" type="success" size="large"></u-tag>
				<view class="select">
					<uni-data-select v-model="value" :localdata="types" class="list" :clear="false">
					</uni-data-select>
				</view>
			</view>

			<view class="top">
				<text class="title">标题:</text>
				<view class="topBtn"><u-button type="primary" shape="circle" @click="onPublish">发布</u-button></view>
			</view>

			<input type="text" maxlength="50" v-model="inputTitle" placeholder="请输入标题" class="input font" />
			<view class="content">文章内容:</view>
			<!-- 编辑器工具栏 -->
			<view class="toolbar">
				<view class="btn">
					<u-button type="primary" @click="onInsertImage()">插入图片</u-button>
				</view>
				<view class="btn">
					<u-button type="primary" @click="handleSetBlod">粗体</u-button>
				</view>
				<!-- <view class="btn">
					<u-button type="primary" @click="handleSetHeader('H1')">h1</u-button>
				</view> -->
				<view class="btn">
					<u-button type="primary" @click="handlePreviousStep">撤销</u-button>
				</view>
				<view class="btn">
					<u-button type="primary" @click="handleNextStep">恢复</u-button>
				</view>
				<view class="btn">
					<u-button type="primary" @click="handleSetColor">字体颜色</u-button>
				</view>
			</view>
			<editor id="editor" show-img-size='true' show-img-toolbar='true' class="ql-container m-ql-container"
				@ready="onEditorReady" placeholder="请输入内容"></editor>

			<view class="bottom">
				<!-- <view class="bottomBtn"><u-button class="bottomBtn" icon="photo" type="primary" @click="onInsertImage"
						text="添加图片"></u-button></view> -->
			</view>
		</view>
	</view>
</template>

<script>
	import oss from '@/api/oss.js'
	import * as ossUploadPath from '@/constant/ossUploadPath.js'
	import * as articleType from '@/constant/articleType.js'
	import * as errorCode from '@/constant/errorCode.js'
	import generateRandomFileName from '@/utils/fileUtils.js'
	import {
		publish,
		ArtDetails,
		updateArticle
	} from '@/api/article.js'
	import {
		fetchPageTitle,
		isValidUrl
	} from '@/utils/urlUtils.js'
	import {
		islogin
	} from "@/api/user.js"
	import {
		textReview
	} from '@/api/review'
	import * as reviewType from '@/constant/reviewType.js'
	export default {
		data() {
			return {
				isAdmin: false, // 当前用户是否为管理员
				articleId: '', // 文章id，如果跳转到该页面时传了文章id，则自动加载文章详情
				inputTitle: '', // 文章标题
				thumbnailUrl: null, // 略缩图链接使用上传的第一张图片
				types: [{ // 文章分类
					value: articleType.RURAL_REVITALIZATION,
					text: '乡村振兴'
				}, {
					value: articleType.SPECIALTY_INTRODUCTION,
					text: '特产介绍'
				}, {
					value: articleType.AGRICULTURAL_TECHNOLOGY,
					text: '农业技术'
				}, {
					value: articleType.QUESTION_AND_ANSWER,
					text: '问题答疑'
				}, ],
				value: 1,
				editorColor: 'balck'
			}
		},
		methods: {
			// 发布问题到后端
			async onPublish() {
				// 判断是否登录
				if (!islogin()) {
					return
				}
				// 数据校验 是否为空
				if (this.inputTitle == '') {
					this.showError('请填写标题')
					return
				}
				var text = await this.getEditorText()
				if (text.trim() === '') {
					this.showError('请填写内容')
					return
				}
				// 显示加载框
				uni.showLoading({
					mask: true,
					title: '发布中...'
				})
				// 审核标题
				let resp = await textReview({
					text: this.inputTitle,
					type: reviewType.COMMENT_DETECTION
				})
				if (resp.code == errorCode.SUCCESS) {
					let tips = resp.data.risk_tips
					if (tips != "") {
						this.showError("标题包含：" + tips)
						return
					}
				}
				var result = await this.uploadImgs();
				if (!result) {
					return
				}
				var html = await this.getEditorContent()
				try {
					// 拼接请求参数
					var reqParam = {
						article_id: this.articleId,
						title: this.inputTitle,
						content: html,
						thumbnail_url: this.thumbnailUrl,
						article_type: this.value,
						subtitle: text.substring(0, 25).trim()
					}
					var res;
					// articleId不为空，就更新文章而不是发布文章
					if (this.articleId != '') {
						res = await updateArticle(reqParam)
					} else {
						res = await publish(reqParam)
					}
					if (res.code == errorCode.SUCCESS) {
						this.showSuccess('发布成功')
						setTimeout(() => {
							uni.navigateBack()
						}, 1000)
					} else {
						throw res
					}
				} catch (e) {
					console.error('发布失败')
					console.error(e)
					this.showError('发布失败')
				}
			},
			showSuccess(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					icon: 'success',
				})
			},
			showError(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
			// 获取输入内容 text
			getEditorText() {
				return new Promise((resolve) => {
					uni.createSelectorQuery().select('#editor').context((res) => {
						res.context.getContents({
							success(e) {
								resolve(e.text)
							},
							fail(e) {
								resolve('')
								console.log('获取editor的内容失败')
							}
						})
					}).exec()
				})
			},
			// 获取输入内容html
			getEditorContent() {
				return new Promise((resolve) => {
					uni.createSelectorQuery().select('#editor').context((res) => {
						res.context.getContents({
							success(e) {
								resolve(e.html)
							},
							fail(e) {
								resolve('')
								console.log('获取editor的内容失败')
							}
						})
					}).exec()
				})
			},
			// 设置editor组件的内容
			setEditorContent(htmlContent) {
				uni.createSelectorQuery().select('#editor').context((res) => {
					res.context.setContents({
						html: htmlContent
					});

				}).exec()
			},
			// 插入文本到编辑器
			insertTextToEditor(text) {
				uni.createSelectorQuery().select('#editor').context((res) => {
					res.context.insertText({
						text: text
					});

				}).exec()
			},
			// 插入图片到编辑器
			insertImageToEditor(imgUrl) {
				uni.createSelectorQuery().select('#editor').context((res) => {
					res.context.insertImage({
						src: imgUrl,
						width: '320px'
					});

				}).exec()
			},
			// 获取html中所有的a标签链接
			getImgsFromHtml(htmlString) {
				var imgRegex = /<img [^>]*src=['"]([^'"]+)[^>]*>/gi
				var srcMatches;
				var result = [] // 结果
				while ((srcMatches = imgRegex.exec(htmlString)) !== null) {
					result.push(srcMatches[1])
				}
				return result
			},
			// 上传编辑器中所有的图片到oss
			async uploadImgs() {
				// 从html提取所有的图片链接
				var htmlString = await this.getEditorContent()
				var imgs = this.getImgsFromHtml(htmlString)
				let result = true
				// 上传所有图片到oss
				for (let item in imgs) {
					var img = imgs[item]
					if (img.startsWith('https')) {
						continue
					}
					var randomFileName = await generateRandomFileName(img)
					var e = await oss.uploadImageWithPromise({
						url: ossUploadPath.ARTICLE + randomFileName,
						imgPath: img, // 图片本地路径
					})
					if (e.isSuccess) {
						// 略缩图链接使用上传的第一张图片
						if (this.thumbnailUrl == null)
							this.thumbnailUrl = e.url
						// 上传成功，替换html的src链接
						htmlString = htmlString.replaceAll(img, e.url)
					} else {
						console.log('上传文件失败，原因：')
						console.log(e.msg)
						this.showError('上传失败')
						result = false
					}
				}
				await this.setEditorContent(htmlString)
				return result
			},
			// 点击添加图片按钮
			onInsertImage() {
				uni.chooseImage({
					success: async (res) => {
						const tempFilePaths = res.tempFilePaths;
						for (let item in tempFilePaths) {
							const filePath = tempFilePaths[item];
							console.log('插入图片，本地路径：' + filePath)
							await this.insertImageToEditor(filePath)
							await this.insertTextToEditor("\r\n")
						}
					}
				})
			},
			// editor的初始化事件
			async onEditorReady() {
				// 如果articleid不为空，就加载文章详情，显示到界面
				if (this.articleId != '') {
					const res = await ArtDetails({
						article_id: this.articleId
					})
					if (res.code != 200) {
						this.showError(res.msg)
					} else {
						this.value = Number(res.data.article_type)
						this.inputTitle = res.data.title
						var editorContent = res.data.content
						this.setEditorContent(editorContent)
					}
				}
				// else {
				// 	this.insertTextToEditor("\r\n")
				// }
			},
			//设置粗体
			handleSetBlod() {
				uni.createSelectorQuery().select('#editor').context((res) => {
					res.context.format("bold");
				}).exec()
			},
			//设置标题
			handleSetHeader(value) {
				uni.createSelectorQuery().select('#editor').context((res) => {
					res.context.format("header", value);
				}).exec()
			},
			//设置颜色
			handleSetColor() {
				uni.createSelectorQuery().select('#editor').context((res) => {
					if (this.editorColor != 'red') {
						this.editorColor = 'red'
					} else {
						this.editorColor = 'black'
					}
					res.context.format("color", this.editorColor);
				}).exec()
			},
			//设置list
			handleList(value) {
				uni.createSelectorQuery().select('#editor').context((res) => {
					res.context.format("list", value);
				}).exec()
			},
			//撤销上一步
			handlePreviousStep() {
				uni.createSelectorQuery().select('#editor').context((res) => {
					res.context.undo()
				}).exec()
			},
			//恢复上一步
			handleNextStep() {
				uni.createSelectorQuery().select('#editor').context((res) => {
					res.context.redo();
				}).exec()
			}
		},
		// 获取其它页面传过来的参数isAdmin、e.articleId
		onLoad(e) {
			this.isAdmin = Boolean(e.isAdmin)
			if (!this.isAdmin) {
				// 不是管理员
				this.value = articleType.QUESTION_AND_ANSWER
				uni.setNavigationBarTitle({
					title: '发布问题'
				})
			} else {
				// 是管理员
				if (typeof e.articleId !== 'undefined') {
					this.articleId = e.articleId
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.font {
		font-size: 12px;
	}

	.outline {
		margin: 0 4vw;

		.type {
			display: flex;
			justify-content: left;
			border-bottom: 1px solid #dddddd;
			font-weight: bold;
			padding: 1vh 0 1vh;

			.select {
				margin-left: 5vw;
				min-width: 200rpx;
			}
		}

		.top {
			display: flex;
			justify-content: space-between;
			border-bottom: 1px solid #dddddd;
			font-weight: bold;
			padding: 1vh 0 1vh;

			.topBtn {
				width: 170rpx;
				font-size: 15px;
				padding: 0 7vw;
			}

			.title {
				font-size: 20px;
				margin-top: 20rpx;
			}
		}

		.input {
			padding: 2vh 2vw;
			border: 1px solid #dddddd;
			border-radius: 2vw;
			margin: 2vh 0 3vh;
			font-size: 16px;
		}

		.content {
			border-bottom: 1px solid #dddddd;
			font-size: 18px;
			line-height: 30px;
			font-weight: bold;
		}


		.toolbar {
			padding: 0;
			display: flex;
			flex-wrap: wrap;
			padding-top: 16rpx;
			gap: 1vw;

			.btn {
				min-width: 96rpx;
				margin-bottom: 16rpx;
			}
		}

		editor {
			border: 1px solid #dddddd;
			margin: 1vh 0 2vw 0;
			padding: 2vw;
			height: 60vh;
			border-radius: 2vh;
			font-size: 18px;
		}

		.bottom {
			display: flex;
			justify-content: space-around;
			padding-top: 1vh;
			border-top: 1px solid #dddddd;
			margin-top: 1vh;
			text-align: center;

			.bottomBtn {
				width: 200rpx;
			}
		}
	}


	.popup-content {
		padding-bottom: 30vh;
		font-size: 18px;
		margin: 0 1vw;

		view {
			border-bottom: 1px solid #ddd;
			padding: 1vh 2vh;
		}
	}
</style>