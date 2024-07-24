<script>
	import top from "@/components/top.vue"
	import {
		getbanner
	} from "@/api/index.js"
	import {
		getArticle
	} from "@/api/article.js"
	import * as chartType from "@/constant/chartType.js"
	import {
		onMounted
	} from "vue"
	import {
		isEmpty
	} from "../../utils/stringUtils"
	import {
		tokenIsExpiration
	} from "../../api/user"
	import {
		getClineHeight
	} from "@/utils/screenUtils.js"

	export default {
		components: {
			top
		},
		data() {
			return {
				safeAreaInsets: '',
				current: 0, // 分段器索引
				tabbar: ['乡村振兴', '特产介绍', '农业技术', '问题答疑'],
				data1: [],
				banner: [],
				page: 1,
				maxpage: 0,
				scrollTop: 0, // scroll-view垂直方向滚动距离
				oldScrollTop: 0,
				privacyVisible: false, // 是否弹出隐私提醒
			}
		},
		methods: {
			onClickItem(e) {
				if (this.current != e.currentIndex) {
					this.current = e.currentIndex;
				}
				this.loadArticle()
			},
			async loadArticle(page = 1, Bottom = false) {
				this.goTop()
				// 获取乡村振兴 特产介绍 农业技术 问题答疑 的文章列表
				let req = await getArticle({
					article_type: this.current + 1,
					page: page
				})
				if (req.code != 200) {
					this.showError('加载文章失败')
					return
				}
				if (Bottom) {
					this.data1 = this.data1.concat(req.data.list)
				} else {
					this.page = 1
					this.data1 = req.data.list
					this.maxpage = req.data.pagecount
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
			scroll(e) {
				//记录scroll位置
				this.oldScrollTop = e.detail.scrollTop
			},
			// scroll-view滚回顶部
			goTop() {
				//视图会发生重新渲染
				this.scrollTop = this.oldScrollTop
				//当视图渲染结束 重新设置为0
				this.$nextTick(() => {
					this.scrollTop = 0
				});
			},
			// 弹出隐私同意框
			popUp() {

			}
		},
		async onShow() {
			// 获取轮播图数据
			let req = await getbanner({
				type: chartType.HOME
			})
			if (req.code != 200) {
				console.log('加载轮播图失败')
			} else {
				this.banner = req.data.img_url
			}
			await this.loadArticle()
		},
		async mounted() {
			// 检测token是否过期
			let token = uni.getStorageSync('token')
			if (isEmpty(token)) {
				// token为空，返回
				return
			}
			let isExpiration = await tokenIsExpiration(token)
			if (isExpiration == false) {
				console.log('token未过期')
			} else {
				console.log('token过期')
				this.showError('登录过期')
				uni.removeStorageSync('token')
				setTimeout(() => {
					uni.navigateTo({
						url: "/pages/login/index"
					})
				}, 1000)
			}
		},
		//触底加载
		async onReachBottom() {
			let data = this
			if (data.page < data.maxpage) {
				this.loadArticle(++data.page, true)
			}
		},
		onReady() {
			// #ifdef MP
			uni.getPrivacySetting({
				success: res => {
					console.log(res)
					if (res.needAuthorization) {
						// 需要弹出隐私协议
						this.privacyVisible = true
						this.$refs.showPrivacy.init(res => {
							if (res.event === 'agree') {
								console.log('用户同意')
							} else {
								console.log('用户不同意')
							}
						})
					}
				},
				fail: () => {
					console.log('查询隐私协议失败')
				}
			})
			// #endif
		}
	}
</script>

<template>
	<view>
		<!-- 隐私协议弹窗 -->
		<showPrivacyAgreement ref="showPrivacy" :visible.sync="privacyVisible"></showPrivacyAgreement>

		<top></top>
		<view class="top">
			<image src="@/static/logo.jpg" mode="scaleToFill"></image>
		</view>
		<view class="banner">
			<swiper class="swiper-container" :indicator-dots="true" :autoplay="true" :interval="4000" :duration="1000"
				circular='true'>
				<swiper-item v-for="(i, index) in banner" :key="index">
					<view class="swiper-item">
						<image :src="i" mode="scaleToFill"></image>
					</view>
				</swiper-item>
			</swiper>
		</view>

		<view class="nav-bar">
			<uni-grid class="icon" :column="4" :showBorder='false'>
				<navigator url="/pages/article/articleList">
					<uni-grid-item>
						<view style="color: #ffbf50;">&#xe897;</view>
						<text class="content">文章</text>
					</uni-grid-item>
				</navigator>
				<navigator url="/pages/article/publishArticle">
					<uni-grid-item>
						<view style="color: #fd6557;">&#xe856;</view>
						<text class="content">问题发布</text>
					</uni-grid-item>
				</navigator>
				<navigator url="/pages/shopping/index">
					<uni-grid-item>
						<view style="color: #3dcf83;">&#xe647;</view>
						<text class="content">商城</text>
					</uni-grid-item>
				</navigator>
				<navigator url="/pages/job/jobList">
					<uni-grid-item>
						<view style="color: #61c1c2;">&#xe8c8;</view>
						<text class="content">就业</text>
					</uni-grid-item>
				</navigator>
			</uni-grid>
		</view>

		<!-- tabbar -->
		<view class="tabbar">
			<uni-segmented-control :current="current" :values="tabbar" @clickItem="onClickItem" styleType="text"
				activeColor="#505050" />
		</view>

		<!-- 文章列表 -->
		<view class="content">
			<scroll-view scroll-y="true" @scroll="scroll" :scroll-top="scrollTop">
				<!-- 乡村振兴 -->
				<view v-show=" current===0">
					<view v-for="i in data1" :key='i.article_id'>
						<navigator :url="'/pages/article/articleDetails?id=' + i.article_id">
							<aricleCard :datetime='i.date_created' :title="i.title" :thumbnail_url="i.thumbnail_url"
								article-type="1" :author="i.author">
							</aricleCard>
						</navigator>
					</view>
				</view>
				<!-- 特产介绍 -->
				<view v-show="current === 1">
					<view v-for="i in data1" :key='i.article_id'>
						<navigator :url="'/pages/article/articleDetails?id=' + i.article_id">
							<aricleCard :datetime='i.date_created' :title="i.title" :thumbnail_url="i.thumbnail_url"
								article-type="2" :author="i.author" :subtitle="i.subtitle">
							</aricleCard>
						</navigator>
					</view>
				</view>
				<!-- 农业技术 -->
				<view v-show="current === 2">
					<view v-for="i in data1" :key='i.article_id'>
						<navigator :url="'/pages/article/articleDetails?id=' + i.article_id">
							<aricleCard :datetime='i.date_created' :title="i.title" :thumbnail_url="i.thumbnail_url"
								article-type="3" :author="i.author">
							</aricleCard>
						</navigator>
					</view>
				</view>
				<!-- 问题答疑 -->
				<view v-show="current === 3">
					<view v-for="i in data1" :key='i.article_id'>
						<navigator :url="'/pages/article/articleDetails?id=' + i.article_id">
							<aricleCard :datetime='i.date_created' :title="i.title" :thumbnail_url="i.thumbnail_url"
								article-type="4" :author="i.author">
							</aricleCard>
						</navigator>
					</view>
				</view>
			</scroll-view>
		</view>
	</view>
</template>

<style scoped lang="scss">
	.top {
		background-color: #3cb371;
		padding-left: 10px;
		display: flex;
		align-items: center;
		height: 7vh;

		image {
			width: 30vw;
			height: 5vh;
		}
	}

	.banner {
		swiper {
			width: 100%;
			height: 27vh;

			image {
				width: 100%;
				height: 27vh;
			}
		}
	}

	@font-face {
		font-family: 'icon';
		src: url('@/static/font/iconfont.ttf');
	}

	.icon {
		font-family: 'icon';
	}

	.nav-bar {
		padding: 5vw 2vw 0;
		font-weight: bold;

		.icon {
			display: flex;
			font-size: 10vw;
			justify-content: space-between;
			text-align: center;

			.content {
				font-size: 4vw;
				line-height: 10vw;
				color: #767b7e;
			}
		}
	}

	.tabbar {
		height: 77rpx;
		border: 1px solid #c8c8c8;
		border-radius: 0 0 3vw 3vw;
		padding: 10rpx;
	}

	scroll-view {
		height: 36vh;
	}
</style>