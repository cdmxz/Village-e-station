<!-- 文章列表页面 -->
<!-- 
管理员和用户一体 -->

<script>
	import {
		getArticle,
		AllArticle,
		updateArticle,
		deleteArticle
	} from "@/api/article.js"
	import top from "@/components/top.vue"
	import * as articleType from '@/constant/articleType.js'
	import * as errorCode from '@/constant/errorCode.js'

	export default {
		components: {
			top
		},
		data() {
			return {
				data: [],
				keyword: '',
				isDescOrder: true, // 是否为降序排序
				isShowEmpty: false, //是否显示空白提示（搜索数据为空时显示）
				// 文章分类
				types: [{
						value: articleType.ALL,
						text: '全部'
					}, {
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
					},

				],
				selectType: articleType.ALL, // 当前文章分类的索引
				isAdmin: false,
				page: 1,
				maxpage: 0,
			}
		},
		methods: {
			back() {
				uni.navigateBack()
			},
			// 加载文章列表
			async loadAllArticle(page, Bottom = false) {
				// 返回结果
				let res = null
				// 请求参数
				let reqParam = {
					page: page,
					desc: this.isDescOrder == true ? 1 : 0,
					keyword: this.keyword
				}
				// 0表示请求所有类型的文章列表
				if (this.selectType != articleType.ALL) {
					reqParam.article_type = this.selectType
					res = await getArticle(reqParam)
				} else {
					res = await AllArticle(reqParam)
				}
				console.log('加载文章列表')
				// 错误处理
				if (res.code != 200) {
					this.showError('加载失败')
				} else {
					this.maxpage = res.data.pagecount
					if (Bottom) {
						this.data = this.data.concat(res.data.list)
					} else {
						this.data = res.data.list
					}
					// if (res.data.pagecount != 0) {
					// 	// 搜索成功，清空输入框
					// 	// this.keyword = ''
					// }
				}
				this.isShowEmpty = (res.code != 200) || (res.data.pagecount == 0)
			},
			// 切换文章分类
			typeChange(e) {
				this.page = 1
				this.loadAllArticle(1)
			},
			// 升序排序、降序排序切换
			orderClick() {
				this.isDescOrder = !this.isDescOrder
				console.log('切换排序，当前是否为降序：' + this.isDescOrder)
				this.page = 1
				this.loadAllArticle(1)
			},
			// 搜索文章
			onSearch(keyword) {
				console.log('搜索：' + keyword)
				this.page = 1
				this.keyword = keyword
				this.loadAllArticle(1)
			},
			// 删除文章
			onDelete(id) {
				uni.showModal({
					title: '提醒',
					content: '是否删除？',
					success: async (e) => {
						if (e.confirm) {
							var res = await deleteArticle(id)
							if (res.code != 200) {
								this.showError('删除失败')
							} else {
								this.loadAllArticle(1)
							}
						}
					}
				})
			},
			// 修改文章
			onUpdate(id) {
				uni.navigateTo({
					url: '/pages/article/publishArticle?isAdmin=true&articleId=' + id
				})
			},
			showError(title) {
				uni.showToast({
					icon: 'error',
					mask: true,
					title: title,
				})
			},
			showSuccess(title) {
				uni.showToast({
					title: title,
					duration: 500
				})
			}
		},
		onLoad(e) {
			this.isAdmin = Boolean(e.isAdmin)
		},
		onShow() {
			this.loadAllArticle(1)
		},
		//触底加载
		async onReachBottom() {
			let data = this

			if (data.page < data.maxpage) {
				this.loadAllArticle(++data.page, true)
			}

		}
	}
</script>

<template>
	<view>
		<top></top>
		<view class="top">
			<uni-icons class="icon" type="back" size="26" color="white" @click='back'></uni-icons>
			<view class="search">
				<u-search placeholder="搜索文章" v-model="keyword" :animation="true" @search="onSearch" @custom="onSearch">
				</u-search>
			</view>
		</view>

		<view class="tabbar">
			<view @click="orderClick">
				<view v-show="isDescOrder">按时间降序 <uni-icons type="arrow-down"></uni-icons></view>
				<view v-show="!isDescOrder">按时间升序 <uni-icons type="arrow-up"></uni-icons></view>
			</view>
			<uni-data-select v-model="selectType" :localdata="types" @change="typeChange" class="list" :clear="false">
			</uni-data-select>
		</view>

		<!-- 查询的数据 为空 时显示 -->
		<u-empty mode="search" :show="isShowEmpty">
		</u-empty>
		<!-- 查询的数据 不为空 时显示 -->
		<scroll-view scroll-y="true" v-show="!isShowEmpty">
			<view v-for="(i, index) in data" :key='index'>
				<navigator :url="'/pages/article/articleDetails?id='+ i.article_id">
					<!-- 用户显示的部分 -->
					<view v-if="!isAdmin">
						<aricleCard :thumbnail_url="i.thumbnail_url" :title="i.title" :datetime="i.date_created"
							:article-type="i.article_type" :author="i.author" :subtitle="i.subtitle" />
					</view>
					<!-- 管理员显示的部分 -->
					<view v-if="isAdmin">
						<aricleCardAd :thumbnail_url="i.thumbnail_url" :title="i.title" :datetime="i.date_created"
							:article-type="i.article_type" :author="i.author" :subtitle="i.subtitle"
							:isMyPublish="i.is_my_publish" @delete="onDelete(i.article_id)"
							@update="onUpdate(i.article_id)" />
					</view>
				</navigator>
			</view>
		</scroll-view>
	</view>
</template>


<style scoped lang="scss">
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

	.tabbar {
		border-bottom: #bbbbbb 1px solid;
		padding: 1vw 10vw;
		justify-content: space-between;
		display: flex;
		align-items: center;

		.list {
			width: 40%;
			border: none;
			max-width: 250rpx;
		}
	}
</style>