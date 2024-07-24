<template>
	<view>
		<uni-section class="title" title="商品概述" titleFontSize="20px"></uni-section>
		<view class="list">
			<view>
				<text>商品名称</text>
				<uni-easyinput v-model="data.name" :focus='true' placeholder="请输入" :clearable='false'
					maxlength="50"></uni-easyinput>

			</view>
			<view>
				<text>价格</text>
				<uni-easyinput type="digit" @input="priceInput" v-model="data.price" placeholder="请输入"
					:clearable='false' maxlength='8'></uni-easyinput>元

			</view>
			<view>
				<text>邮费</text>
				<uni-easyinput type="digit" @input="postageInput" v-model="data.postage" placeholder="请输入"
					:clearable='false' maxlength='8'></uni-easyinput>元

			</view>
			<view>
				<text>库存</text>
				<uni-number-box v-model="data.stock" :max='9999' />
				件
			</view>
			<view>
				<text>分类</text>
				<uni-data-select style="width: 25vw;" v-model="data.category_id" :localdata="category"
					:clear='false'></uni-data-select>
				<button></button>
			</view>
			<view>
				<text>商品图片</text>
				<u-upload :fileList="imgList" @afterRead="addImg" @delete="deleteImg" name="1" multiple
					:previewFullImage="true" :maxCount="3"></u-upload>
				<text style="color: red;">只能选3张图片</text>
			</view>

			<!-- 商品状态 -->
			<view>
				<text>商品状态</text>
				<uni-data-select v-model="data.status" :localdata="range" :clear='false'></uni-data-select>
			</view>
		</view>
		<!-- 商品详情部分 -->
		<uni-section class="title" title="商品详情" titleFontSize="20px"></uni-section>
		<view class="listDetails">
			<view class="editor">
				<editor id="editor" show-img-size='true' show-img-toolbar='true' class="ql-container m-ql-container"
					@ready="onEditorReady" placeholder="请输入内容"></editor>
			</view>
			<!-- 添加图片控件 -->
			<view class="control">
				<view class="imgControl" @click="onInsertImage">
					<u-icon name="photo" class="icon"></u-icon>
					添加图片
				</view>
			</view>
		</view>
		<!-- 是修改商品时显示 -->
		<view class="btn" v-if="!isAddType">
			<button type="warn" @click="Delete">删除商品</button>
			<button type="primary" @click="updateShop">提交</button>
		</view>
		<!-- 添加商品时显示 -->
		<view style="display: flex;justify-content: flex-end;" v-else>
			<button type="primary" style="margin: 0;" @click="addShop">添加</button>
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
		update,
		ShopDetails,
		ShopAdd,
		ShopDelete,
		getCategory
	} from "@/api/shopping.js"
	export default {
		data() {
			return {
				category: [{
						value: 1,
						text: '水果蔬菜'
					}, {
						value: 2,
						text: '粮油米面'
					}, {
						value: 3,
						text: '休闲零食'
					}, {
						value: 4,
						text: '禽畜肉蛋'
					},

				],
				range: [{
						value: 1,
						text: '在售'
					},
					{
						value: 2,
						text: '下架'
					}
				],
				data: {},
				good_id: '',
				imgList: [],
				isAddType: true,
				type: 0, //是否添加商品
			}
		},
		methods: {
			// 删除图片
			deleteImg(event) {
				this.imgList.splice(event.index, 1)
			},
			// 新增图片
			addImg(event) {
				let lists = [].concat(event.file)
				lists.map((item) => {
					this.imgList.push({
						...item,
						status: 'success',
						message: ''
					})
				})
			},
			// 校验data里面的数据是否有空值
			checkData() {
				for (let prop in this.data) {
					var val = this.data[prop]
					if (typeof this.data[prop] === 'string') {
						if (prop === 'thumbnail_url' || prop == 'description')
							continue
						if (val.trim() === '') {
							throw new Error(prop + '为空')
						}
					}
				}
			},
			// 构造发给api的数据data
			async createData() {
				var text = await this.getEditorText()
				// 判断商品详情是否为空，商品详情需要单独判断
				if (text.trim() == '') {
					this.showError('请填写商品详情')
					return false
				}
				if (this.imgList.length == 0) {
					this.showError('请上传1张图片')
					return false
				}
				try {
					this.checkData();
				} catch (e) {
					console.log(e)
					this.showError('数据不完整')
					return false
				}
				// 上传·商品图片 到oss
				let newImgUrl = []
				let url
				try {
					for (let i = 0; i < this.imgList.length; i++) {
						let img = this.imgList[i]
						if (img.url.startsWith("https://")) {
							// 更新商品详情时的图片已经在oss中，不必上传
							url = img.url
						} else {
							url = await this.uploadToOss(img.url)
							img.url = url
						}
						newImgUrl.push(url)
					}
				} catch (e) {
					this.showError('图片上传失败')
					return false
				}
				// 上传 商品详情 图片到oss
				let result = await this.uploadEditorImgs()
				if (!result) {
					return false
				}
				// 设置商品详情（上传 商品详情图片到oss再获取！！！）
				this.data.description = await this.getEditorContent()
				console.log(this.data.description)
				this.data.thumbnail_url = newImgUrl[0]
				if (newImgUrl.length == 1) {
					this.data.rotation_urls = [newImgUrl[0]]
				} else {
					this.data.rotation_urls = newImgUrl
				}
				this.data.surplus = this.data.stock
				return true
			},
			//修改商品
			async updateShop() {
				console.log('修改商品')
				// 显示加载框
				uni.showLoading({
					mask: true
				})
				// 构建data，并上传图片到oss
				let res = await this.createData()
				if (res == false) {
					return
				}
				let e = await update(this.data);
				if (e.code == 200) {
					this.showSuccess('修改成功')
					setTimeout(e => {
						uni.navigateBack()
					}, 1000)
				} else {
					this.showError(e.msg)
				}
			},
			//添加商品
			async addShop() {
				console.log('添加商品')
				// 显示加载框
				uni.showLoading({
					mask: true
				})
				let res = await this.createData()
				if (res == false) {
					return
				}
				let e = await ShopAdd(this.data)
				if (e.code == 200) {
					this.showSuccess('添加成功')
					setTimeout(e => {
						uni.navigateBack()
					}, 1000)
				} else {
					this.showError(e.msg)
				}
			},
			//删除商品
			Delete() {
				let that = this
				uni.showModal({
					title: '你确定要删除该商品吗？',
					success: async (res) => {
						if (res.confirm) {
							// 显示加载框
							uni.showLoading({
								mask: true
							})
							let e = await ShopDelete(that.data.good_id)
							if (e.code == 200) {
								uni.navigateBack()
								// this.showSuccess('删除成功')
								// setTimeout(e => {
								// 	uni.navigateBack()
								// }, 200)
							} else {
								this.showError(e.msg)
							}
						}
					}
				});
			},
			showError(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
			showSuccess(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					icon: 'error',
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
			// 添加html到editor组件
			appendEditorContent(newContent) {
				this.getEditorContent().then((html) => {
					uni.createSelectorQuery().select('#editor').context((res) => {
						res.context.setContents({
							html: html + newContent
						});
					}).exec()
				})
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
						src: imgUrl
					});

				}).exec()
			},
			// 上传图片到oss 本地路径：filePath
			async uploadToOss(filePath) {
				// 上传图片到oss
				var randomFileName = await generateRandomFileName(filePath)
				let e = await oss.uploadImageWithPromise({
					url: ossUploadPath.SHOP + randomFileName,
					imgPath: filePath, // 图片本地路径
				})
				if (e.isSuccess) {
					return e.url
				} else {
					throw new Error('上传文件失败，原因：' + e.msg)
				}
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
			async uploadEditorImgs() {
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
					try {
						let ossUrl = await this.uploadToOss(img)
						// 上传成功，替换html的src链接
						htmlString = htmlString.replaceAll(img, ossUrl)
					} catch (e) {
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

			},
			priceInput(e) {
				let val = e.match(/^\d*(\.?\d{0,2})/g)[0] || e
				// 重新赋值给input
				this.$nextTick(() => {
					this.data.price = val
				})
			},
			postageInput(e) {
				let val = e.match(/^\d*(\.?\d{0,2})/g)[0] || e
				// 重新赋值给input
				this.$nextTick(() => {
					this.data.postage = val
				})
			}
		},
		async onLoad(e) {
			let res = await getCategory()
			console.log(res)
			// 如果传过来good_id那就是修改模式
			if (typeof e.id !== 'undefined') {
				console.log(1)
				this.good_id = e.id
				this.isAddType = false
				await uni.setNavigationBarTitle({
					title: '修改商品'
				})
				// 如果是修改模式，就把商品详情，显示到界面
				// 加载商品详情，需要在此处加载商品详情，避免数据不同步
				try {
					const res = await ShopDetails({
						good_id: this.good_id
					})
					if (res.code != 200) {
						this.showError(res.msg)
					} else {
						this.data = res.data
						// 添加轮播图url到imgList
						// 不需要添加res.data.thumbnail_url到imgList
						// 因为res.data.rotation_urls已经包含了
						for (let i in res.data.rotation_urls) {
							let url = res.data.rotation_urls[i]
							this.imgList.push({
								url: url
							})
						}
						// 设置editor的内容
						var editorContent = this.data.description
						await this.setEditorContent(editorContent)
						editorContent = await this.getEditorContent()
						console.log(editorContent)
					}
				} catch (e) {
					this.showError('加载商品详情失败')
					console.error(e)
				}
			} else {
				// 否则就是新增商品
				this.isAddType = true
				this.data = {
					"name": "",
					"price": "",
					"postage": "",
					"thumbnail_url": "",
					"rotation_urls": [

					],
					"description": "",
					"category_id": 1,
					"status": 1,
					"stock": 1,
					"surplus": this.stock,
					"recent_sales": 1,
					"sales_quantity": 1
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	.listDetails {
		margin: 0 2vw;

		.control {
			display: flex;
			justify-content: flex-end;
			margin-right: 15vw;
			margin-bottom: 2vh;

			.imgControl {
				margin: 0;
				display: flex;
				flex-direction: column;
				align-items: center;
				height: 100%;

				.icon {
					margin: 0;
				}
			}
		}

		editor {
			border: 1px solid #dddddd;
			margin: 1vh auto;
			padding: 1vw;
			height: 80vh;
			border-radius: 2vh;
		}
	}

	.list {
		margin: 0 10vw;

		view {
			display: flex;
			margin: 2vh 0;
			align-items: center;

			text {
				font-weight: bold;
				width: 30vw;
			}
		}

		.img {
			height: 10vh;
			width: 10vh;
		}


	}

	uni-file-picker {
		height: 400rpx;
	}

	.btn {
		display: flex;
		justify-content: space-between;
		margin: 0 20vw;
	}
</style>