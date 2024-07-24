<template>
	<view class="content">
		<swiper :indicator-dots="true" :autoplay="true" :interval="3000" :circular="true" class="banner">
			<swiper-item v-for="i in banner" :key="i">
				<view class="swiper-item">
					<image :src="i" mode="scaleToFill"></image>
				</view>
			</swiper-item>

		</swiper>
		<view class="white">
			<uni-segmented-control :current="current" :values="items" @clickItem="onClickItem" styleType="text"
				activeColor="#55a5fd"></uni-segmented-control>
		</view>
		<view>
			<!-- 企业招聘 -->
			<view v-show="current === 0">
				<search placeholder="搜索企业、岗位" type=1 v-model="keyword" @search="onSearch" @clear="onClear">
					<template #title>
						<text>如需发布企业招聘请点击</text>
					</template>
					<template #btn>
						<text class="btn">发布招聘</text>
					</template>
				</search>
				<navigator :url="'/pages/job/jobDetails?id='+i.information_id+'&type=1'" v-for="i in data"
					:key="i.information_id">
					<list>
						<template #title>
							<text>{{i.title}}</text>
						</template>
						<template #name>
							<text>岗位：{{i.work}}</text>
						</template>

						<template #number>
							<view class="number">
								<text>招聘人数：{{i.recruits_number}}</text>
							</view>
						</template>

						<template #money>
							<text>报 酬：{{i.salary}}</text>
						</template>

						<template #address>
							<text>工作地：{{i.work_address}}</text>
						</template>
						<template #start>
							<text>发布时间：{{i.publish_time}}</text>
						</template>
						<template #out>
							<text>截止时间：{{i.deadline}}</text>
						</template>
					</list>
				</navigator>
			</view>
			<!-- 零工招聘 -->
			<view v-show="current === 1">
				<search placeholder="搜索工作、工作地" type=2 v-model="keyword" @search="onSearch" @clear="onClear">
					<template #title>
						<text>如需发布零工招聘请点击</text>
					</template>
					<template #btn>
						<text>发布零工招聘</text>
					</template>
				</search>
				<navigator :url="'/pages/job/jobDetails?id='+i.information_id+'&type=2'" v-for="i in data"
					:key="i.information_id">
					<list>
						<template #title>
							<text>{{i.title}}</text>
						</template>
						<template #name>
							<text>工作简介：{{i.work}}</text>
						</template>

						<template #number>
							<view class="number">
								<text>招聘人数：{{i.recruits_number}}</text>
							</view>
						</template>

						<template #money>
							<text>报酬：{{i.salary}}</text>
						</template>

						<template #address>
							<text>工作地：{{i.work_address}}</text>
						</template>
						<template #start>
							<text>发布时间：{{i.publish_time}}</text>
						</template>
						<template #out>
							<text>截止时间：{{i.deadline}}</text>
						</template>

					</list>
				</navigator>
			</view>
			<!-- 零工求职 -->
			<view v-show="current === 2">
				<search placeholder="搜索工作、工作地" type=3 v-model="keyword" @search="onSearch" @clear="onClear">
					<template #title>
						<text>如需发布零工求职请点击</text>
					</template>
					<template #btn>
						<text>发布求职</text>
					</template>
				</search>
				<navigator :url="'/pages/job/jobDetails?id='+i.information_id+'&type=3'" v-for="i in data"
					:key="i.information_id">
					<list>
						<template #title>
							<text>{{i.title}}</text>
						</template>
						<template #name>
							<text>求职工作：{{i.expected_work}}</text>
						</template>
						<template #money>
							<text>期望报酬：{{i.expected_salary}}</text>
						</template>

						<template #address>
							<text>工作地点：{{i.work_address}}</text>
						</template>
						<template #start>
							<text>工作开始时间：{{i.start_time}}</text>
						</template>
						<template #out>
							<text>工作结束时间：{{i.end_time}}</text>
						</template>
						<template #username>
							<text>求职者：{{i.contact_person}}</text>
						</template>
						<template #tel>
							<text>联系电话：{{i.contact_information}}</text>
						</template>
					</list>
				</navigator>
			</view>
		</view>
		<bottom></bottom>
	</view>
</template>

<script>
	import bottom from "@/components/bottom.vue"
	import * as errorCode from '@/constant/errorCode.js'
	import search from "@/components/JobSearch.vue"
	import list from "@/components/JobList.vue"

	import {
		selectJob
	} from "@/api/job.js"
	import {
		getbanner
	} from "@/api/index.js"
	export default {
		components: {
			list,
			bottom,
			search
		},
		data() {
			return {
				current: 0,
				items: ["企业招聘", '零工招聘', '零工求职'],
				data: '',
				banner: [],
				keyword: '',
				page: 1,
				maxpage: 0
			}
		},
		methods: {
			// 切换就业类型：企业招聘、零工招聘 ...
			onClickItem(e) {
				if (this.current != e.currentIndex) {
					this.current = e.currentIndex;
					this.page = 1
					this.keyword = '';
					this.loadJobs();
				}
			},
			// 搜索
			onSearch(keyword) {
				this.keyword = keyword
				console.log('搜索：' + this.keyword)
				this.loadJobs()
			},
			// 搜索框，点击清空按钮
			onClear(keyword) {
				this.keyword = keyword
				this.loadJobs()
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
			},
			// 根据当前 就业状态 加载信息
			async loadJobs(page = 1, bottom) {
				try {
					let res = await selectJob({
						page: page,
						type: this.current + 1,
						keyword: this.keyword
					})
					if (res.code == errorCode.SUCCESS) {
						this.maxpage = res.data.pagecount
						if (bottom) {
							this.data = this.data.concat(res.data.list)
						} else this.data = res.data.list

					} else {
						this.showError(res.msg)
					}
				} catch (e) {
					console.error('加载就业列表失败，原因：')
					console.error(e)
					this.showError('加载失败')
				}
			}
		},
		async mounted() {
			let res = await getbanner({
				type: 3
			})
			this.banner = res.data.img_url
		},
		onShow() {
			this.loadJobs()
		},
		async onReachBottom() {
			if (this.page < this.maxpage) {
				let data = this
				const res = await this.loadJobs(++page, true)
				this.data = this.data.concat(res.data.list)
			}

		}
	}
</script>

<style scoped lang="scss">
	.banner {


		image {
			width: 100%;
			height: 20vh;
		}
	}

	.number {
		padding: 2vw 4vw;
	}

	.white {
		background-color: #fff;
		padding-bottom: 1vh;
	}

	.btn {
		width: 20vw;
	}

	.content {
		padding-bottom: 8vh;
	}
</style>: