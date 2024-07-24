<!-- 
管理员 
文章列表页面 -->

<script>
	import {
		AllArticle
	} from "@/api/article.js"
	import {
		getArticle
	} from "@/api/article.js"
	import top from "@/components/top.vue"


	export default {
		components: {
			top
		},
		data() {
			return {
				data: '',
				keyword: '',
				isDescOrder: true, // 是否为降序排序
				isShowEmpty: false, //是否显示空白提示（搜索数据为空时显示）
				dataTitle: [{
						value: 0,
						text: '全部'
					}, {
						value: 1,
						text: '乡村振兴'
					}, {
						value: 2,
						text: '特产介绍'
					}, {
						value: 3,
						text: '农业技术'
					}, {
						value: 4,
						text: '问题答疑'
					},

				],
				currentTypeIndex: 0 // 当前文章分类的索引
			}
		},
		methods: {
			back() {
				uni.navigateBack()
			},
			// 加载文章列表
			async loadAllArticle(page) {
				// 返回结果
				let res = null
				// 请求参数
				let reqParam = {
					page: page,
					desc: this.isDescOrder == true ? 1 : 0,
					keyword: this.keyword
				}
				// currentTypeIndex=0表示请求所有文章列表
				if (this.currentTypeIndex != 0) {
					reqParam.article_type = this.currentTypeIndex
					res = await getArticle(reqParam)
				} else {
					res = await AllArticle(reqParam)
				}
				console.log('加载文章列表')
				console.log(res)
				// 错误处理
				if (res.code != 200) {
					uni.showToast({
						icon: 'error',
						mask: true,
						title: '加载文章失败',
						duration: 3000
					})
				} else {
					console.log(res.data.list)
					this.data = res.data.list
					if (res.data.pagecount != 0) {
						// 搜索成功，清空输入框
						this.keyword = ''
					}
				}
				this.isShowEmpty = (res.code != 200) || (res.data.pagecount == 0)
			},

			// 切换文章分类
			typeChange(e) {
				console.log('切换文章分类：' + e)
				this.loadAllArticle(1)
			},
			// 升序排序、降序排序切换
			orderClick() {
				this.isDescOrder = !this.isDescOrder
				console.log('切换排序，当前是否为降序：' + this.isDescOrder)
				this.loadAllArticle(1)
			},
			onSearch(keyword) {
				console.log('搜索：' + keyword)
				this.keyword = keyword
				this.loadAllArticle(1)
			}
		},
		mounted() {
			this.loadAllArticle(1)
		}
	}
</script>

<template>
	<view>
		<top></top>
		<view class="top">
			<uni-icons class="icon" type="back" size="25px" color="white" @click='back'></uni-icons>
			<view class="search">
				<u-search placeholder="搜索文章" v-model="keyword" :animation="true" @search="onSearch" @custom="onSearch">
				</u-search>
			</view>
			<!-- <search placeholder=" 搜索文章" size="50vw"></search> -->
		</view>

		<view class="tabbar">
			<view @click="orderClick">
				<view v-show="isDescOrder">按时间降序 <uni-icons type="arrow-down"></uni-icons></view>
				<view v-show="!isDescOrder">按时间升序 <uni-icons type="arrow-up"></uni-icons></view>
			</view>
			<uni-data-select v-model="currentTypeIndex" :localdata="dataTitle" @change="typeChange" class="list"
				:clear="false">
			</uni-data-select>
		</view>

		<!-- 查询的数据 为空 时显示 -->
		<u-empty mode="search" :show="isShowEmpty">
		</u-empty>
		<!-- 查询的数据 不为空 时显示 -->
		<scroll-view scroll-y="true" v-show="!isShowEmpty">
			<view v-for="i in data" :key='i.article_id'>
				<!-- 				<navigator :url="'/pages/article/articleDetails?id='+ i.article_id">
					<aricleCard :thumbnail_url="i.thumbnail_url" :title="i.title" :datetime="i.date_created"
						:article-type="i.article_type" />
				</navigator> -->

				<view>
					<navigator :url="'/pages/article/articleDetails?id='+ i.article_id">
						<aricleCardAd :thumbnail_url="i.thumbnail_url" :title="i.title" :datetime="i.date_created"
							:article-type="i.article_type" :author="i.author" :subtitle="i.subtitle" />
					</navigator>
				</view>

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