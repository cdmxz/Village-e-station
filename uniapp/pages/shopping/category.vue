<template>
	<view>
		<view class="container">
			<view class="flex">
				<view class="topBtn">
					<!-- <u-button text="添加商品分类" type="primary" @click="onInputDialogToggle"></u-button> -->
				</view>
			</view>

			<view class="u-page">
				<u-list @scrolltolower="scrolltolower" height='60vh'>
					<u-list-item v-for="item in category" :key="item.category_id">
						<u-cell :title="'分类：'+item.name" center>
							<text slot="value">
								<text class="Btn" @click="onUpdateClick(item)">修改</text>
							</text>
						</u-cell>
					</u-list-item>
				</u-list>
			</view>
			<u-divider text='只允许4个商品分类'></u-divider>

		</view>
		<!-- 添加/修改 商品分类 -->
		<uni-popup ref="inputDialog" type="dialog">
			<uni-popup-dialog ref="inputClose" mode="input" title="请输入商品分类(4个字)" value="" placeholder="例如: 休闲零食"
				@confirm="dialogInputConfirm">
			</uni-popup-dialog>
		</uni-popup>
		<!-- 提示信息弹窗 -->
		<uni-popup ref="message" type="message">
			<uni-popup-message :type="msgType" :message="messageText" :duration="2000"></uni-popup-message>
		</uni-popup>
	</view>
</template>

<script>
	import {
		getCategory,
		updateCategory
	} from '@/api/shopping.js'
	export default {
		data() {
			return {
				category: [], // 商品分类列表
				messageText: '',
				msgType: 'success',
				MAX: 4, // 最多允许4个商品分类
				currentId: ''
			};
		},
		async mounted() {
			this.loadCategory()
		},
		methods: {
			// 加载商品分类
			async loadCategory() {
				let resp = await getCategory()
				if (resp.code != 200) {
					this.showError('获取商品分类失败')
					return
				}
				this.category = resp.data
			},
			// 修改分类
			async onUpdateClick(item) {
				this.currentId = item.category_id
				this.onInputDialogToggle()
			},
			showError(title) {
				this.msgType = 'error'
				this.messageText = title
				this.$refs.message.open()
			},
			showSuccess(title) {
				this.msgType = 'success'
				this.messageText = title
				this.$refs.message.open()
			},
			// 添加、修改商品分类对话框
			onInputDialogToggle() {
				// if (this.category.length >= this.MAX) {
				// 	this.showError(`最多允许${this.MAX}个商品分类`)
				// 	return
				// }
				this.$refs.inputDialog.open()
			},
			// 是否存在该商品分类名
			hasSameName(name) {
				let result = false
				this.category.forEach(item => {
					if (item.name == name) {
						result = true
						return
					}
				})
				return result
			},
			// 对话框的确认按钮触发
			dialogInputConfirm(text) {
				if (text.length > 4) {
					this.showError('修改失败，商品分类只能为4个字')
					return
				}
				if (this.hasSameName(text)) {
					this.showError('已经包含商品分类：' + text)
					return
				}
				updateCategory({
					name: text,
					category_id: this.currentId
				}).then(resp => {
					if (resp.code != 200) {
						this.showError(resp.msg)
					} else {
						this.loadCategory()
					}
				})
				// this.AddCategory(text)
			},
			// // 添加分类
			// async AddCategory(name) {
			// 	let resp = await addCategory({
			// 		name: name
			// 	})
			// 	if (resp.code != 200) {
			// 		this.showError(resp.msg)
			// 	} else {
			// 		await this.loadCategory()
			// 	}
			// }
		}
	}
</script>

<style lang="scss">
	.container {
		padding: 1vw 2vw;

		.flex {
			display: flex;
			justify-content: flex-end;
			padding-bottom: 1vw;
			border-bottom: 1px solid grey;
		}

		.Btn {
			background-color: #F56C6C;
			padding: 2vw;
			border-radius: 2vw;
			color: #ffffff;
		}
	}
</style>