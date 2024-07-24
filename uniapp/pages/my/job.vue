<!-- 用户：我发布的招聘
管理员：招聘信息管理
 均合并为此页面
 -->

<template>
	<view>
		<view class="top">
			<view class="search">
				<u-search placeholder="搜索企业/岗位" v-model="keyword" @clear="onClear" @search="onSearch"
					@custom="onSearch">
				</u-search>
			</view>
			<view class="flex">
				<view class="data-select">
					<uni-data-select v-model="selectStatus" :localdata="types" class="list" @change="onStatusChange"
						:clear="false">
					</uni-data-select>
				</view>
				<uni-segmented-control :current="currentType" :values="items" @clickItem="onClickItem" class="right"
					styleType="text" activeColor="#55a5fd"></uni-segmented-control>
			</view>
		</view>

		<view>
			<!-- 企业招聘 -->
			<view v-show="currentType === 0">
				<!-- 查询的数据 为空 时显示 -->
				<u-empty mode="search" :show="data.length==0">
				</u-empty>
				<navigator :url="'/pages/job/jobDetails?id='+i.information_id+'&type=1'" v-for="i in data"
					:key="i.information_id" :show="data.length!=0">
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
						<template #tel>
							<view class="bottom">
								<view class="statusStr">{{i.status_str}}</view>
								<view v-if="i.status==3" class="reason">理由：{{i.reason}}</view>
								<!-- 普通用户显示 修改/删除-->
								<view v-if="!isAdmin">
									<JobBottom :data="i" @delete="onDelete" @update="onUpdate">
									</JobBottom>
								</view>
								<!-- 管理员才显示 通过/拒绝-->
								<view v-if="isAdmin">
									<JobBottomAd :showDel="i.status != 1" :showPassReject="i.status == 1" :data="i"
										@delete="onDelete" @pass="onPass" @reject="onReject">
									</JobBottomAd>
								</view>
							</view>
						</template>
					</list>
				</navigator>

			</view>
			<!-- 零工招聘 -->
			<view v-show="currentType === 1">
				<!-- 查询的数据 为空 时显示 -->
				<u-empty mode="search" :show="data.length==0">
				</u-empty>
				<navigator :url="'/pages/job/jobDetails?id='+i.information_id+'&type=2'" v-for="i in data"
					:key="i.information_id" :show="data.length!=0">
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
							<text>工作地: {{i.work_address}}</text>
						</template>
						<template #start>
							<text>发布时间：{{i.publish_time}}</text>
						</template>
						<template #out>
							<text>截止时间：{{i.deadline}}</text>
						</template>
						<template #tel>
							<view class="bottom">
								<view class="statusStr">{{i.status_str}}</view>
								<view v-if="i.status==3" class="reason">理由：{{i.reason}}</view>
								<!-- 普通用户显示 修改/删除-->
								<view v-if="!isAdmin">
									<JobBottom :data="i" @delete="onDelete" @update="onUpdate">
									</JobBottom>
								</view>
								<!-- 管理员才显示 通过/拒绝-->
								<view v-if="isAdmin">
									<JobBottomAd :showDel="i.status != 1" :showPassReject="i.status == 1" :data="i"
										@delete="onDelete" @pass="onPass" @reject="onReject">
									</JobBottomAd>
								</view>
							</view>
						</template>
					</list>
				</navigator>
			</view>
			<!-- 零工求职 -->
			<view v-show="currentType === 2">
				<!-- 查询的数据 为空 时显示 -->
				<u-empty mode="search" :show="data.length==0">
				</u-empty>
				<navigator :url="'/pages/job/jobDetails?id='+i.information_id+'&type=3'" v-for="i in data"
					:key="i.information_id" :show="data.length!=0">
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
							<text>求职者:{{i.contact_person}}</text>
						</template>
						<template #tel>
							<text>联系电话:{{i.contact_information}}</text>
							<view class="bottom">
								<view class="statusStr">{{i.status_str}}</view>
								<view v-if="i.status==3" class="reason">理由：{{i.reason}}</view>
								<!-- 普通用户显示 修改/删除-->
								<view v-if="!isAdmin">
									<JobBottom :data="i" @delete="onDelete" @update="onUpdate">
									</JobBottom>
								</view>
								<!-- 管理员才显示 通过/拒绝-->
								<view v-if="isAdmin">
									<JobBottomAd :showDel="i.status != 1" :showPassReject="i.status == 1" :data="i"
										@delete="onDelete" @pass="onPass" @reject="onReject">
									</JobBottomAd>
								</view>
							</view>
						</template>
					</list>
				</navigator>
			</view>
		</view>

	</view>
</template>

<script>
	import * as jobType from '@/constant/jobType.js'
	import * as jobStatus from '@/constant/jobStatus.js'
	import * as errorCode from '@/constant/errorCode.js'
	import list from '@/components/JobList.vue'
	import search from '@/components/JobSearch.vue'
	import {
		AdminJob,
		review,
		deleteInfo,
		MyJob
	} from '@/api/job.js'
	export default {
		components: {
			list
		},
		data() {
			return {
				currentType: 0, // 当前选择的就业分类索引
				items: ['企业招聘', '零工招聘', '零工求职'],
				// 就业状态
				types: [{
						value: jobStatus.ALL,
						text: '状态:全部'
					}, {
						value: jobStatus.REVIEW_PENDING,
						text: '状态:待审核'
					}, {
						value: jobStatus.REVIEW_SUCCESS,
						text: '状态:审核成功'
					}, {
						value: jobStatus.REVIEW_FAILURE,
						text: '状态:审核失败'
					}, {
						value: jobStatus.DEADLINE,
						text: '状态:已下架'
					},

				],
				selectStatus: jobStatus.ALL, // 当前选择的就业状态索引
				isAdmin: false,
				data: [],
				keyword: '',
				page: 1,
				maxpage: 0
			}
		},
		methods: {
			// 就业状态改变时
			onStatusChange(e) {
				this.loadJobs();
			},
			// 切换就业类型：企业招聘、零工招聘 ...
			onClickItem(e) {
				if (this.currentType != e.currentIndex) {
					this.currentType = e.currentIndex;
					this.page = 1
					this.loadJobs();
				}
			},
			// 搜索
			onSearch(keyword) {
				console.log('搜索：' + keyword)
				this.page = 1
				this.keyword = keyword
				this.loadJobs()
			},
			// 搜索框，点击清空按钮
			onClear() {
				this.loadJobs()
			},
			// 通过按钮
			async onPass(i) {
				try {
					uni.showLoading({
						mask: true,
						title: '请求中...'
					})
					var res = await review({
						information_id: i.information_id,
						type: this.currentType + 1,
						status: jobStatus.REVIEW_SUCCESS
					})
					uni.hideLoading()
					if (res.code != errorCode.SUCCESS) {
						this.showError(e.msg)
					} else {
						this.loadJobs()
					}
				} catch (e) {
					that.showError('删除失败')
					console.error('删除失败')
					console.error(e)
				}
			},
			// 拒绝按钮
			onReject(i) {
				uni.showModal({
					title: '提示',
					editable: true,
					placeholderText: '请输入拒绝理由',
					success: async (res) => {
						if (!res.confirm) {
							return
						}
						if (res.content.trim() == '') {
							data.showError('请输入理由')
							return
						}
						if (res.content.length > 30) {
							data.showError('理由字数过多')
							return
						}
						try {
							uni.showLoading({
								mask: true,
								title: '请求中...'
							})
							var res = await review({
								information_id: i.information_id,
								type: this.currentType + 1,
								status: jobStatus.REVIEW_FAILURE,
								reason: res.content
							})
							uni.hideLoading()
							if (res.code != errorCode.SUCCESS) {
								this.showError(e.msg)
							} else {
								this.loadJobs()
							}
						} catch (e) {
							this.showError('删除失败')
							console.error('删除失败')
							console.error(e)
						}
					}
				})
			},
			// 删除按钮
			onDelete(i) {
				var that = this
				uni.showModal({
					title: '提示',
					content: '确认删除吗？',
					success: async function(res) {
						if (res.confirm) {
							try {
								uni.showLoading({
									mask: true,
									title: '删除中...'
								})
								var res = await deleteInfo({
									information_id: i.information_id,
									type: that.currentType + 1
								})
								if (res.code != errorCode.SUCCESS) {
									that.showError(e.msg)
								}
								that.loadJobs()
							} catch (e) {
								that.showError('删除失败')
								console.error('删除失败')
								console.error(e)
							}
						}
					}
				})
			},
			onUpdate(i) {
				uni.navigateTo({
					url: '/pages/job/publish?type=' + (this.currentType + 1) + '&id=' + i.information_id
				})
			},
			// 根据当前 用户身份、招聘类型、就业状态 的加载信息
			async loadJobs() {
				try {
					if (this.isAdmin) {
						this.loadAdmin()
					} else {
						this.loadMy();
					}
				} catch (e) {
					console.error('加载就业列表失败，原因：')
					console.error(e)
					this.showError('加载失败')
				}
			},
			// 加载管理员信息列表
			async loadAdmin(page = 1) {
				this.data.length = 0
				let res = await AdminJob({
					type: this.currentType + 1,
					status: this.selectStatus,
					keyword: this.keyword,
					page: page
				})
				this.maxpage = res.data.pagecount
				this.data = res.data.list
			},
			// 加载用户信息列表
			async loadMy() {
				this.data.length = 0
				let res = await MyJob({
					type: this.currentType + 1,
					status: this.selectStatus,
					keyword: this.keyword
				})
				this.maxpage = res.data.pagecount
				this.data = res.data.list
			},
			showError(title) {
				uni.hideLoading()
				uni.showToast({
					icon: 'error',
					mask: true,
					title: title,
				})
			},
			showSuccess(title) {
				uni.hideLoading()
				uni.showToast({
					mask: true,
					title: title,
				})
			}
		},
		onShow() {
			// 开始时只加载企业招聘的信息
			// 在切换就业分类、就业状态时，加载目标信息
			this.loadJobs()
		},
		// 判断当前用户是否为管理员
		onLoad(e) {
			this.isAdmin = Boolean(e.isAdmin)
			if (this.isAdmin) {
				uni.setNavigationBarTitle({
					title: '招聘信息管理'
				})
			}
		},
		async onReachBottom() {
			if (this.page < this.maxpage) {
				let data = this
				const res = this.loadJobs(++page)
				this.data = this.data.concat(res.data.list)
			}
		}
	}
</script>

<style scoped lang="scss">
	.top {
		background-color: #ffffff;
		margin: 4vh 0;

		.search {
			margin: 15rpx 0rpx 0 25rpx;
			width: 90%;
		}

		.flex {
			margin-top: 2vh;
			display: flex;

			.data-select {
				width: 30vw;
				margin-right: 60rpx;
				margin-left: 20rpx;

				.list {
					background-color: #ffc34b;
				}
			}

			.right {
				flex: 1;
			}
		}
	}


	.number {
		padding: 2vw 4vw;
	}

	.bottomFlex {
		margin-top: 1vh;
		display: flex;
		padding: 0 20vw;
		gap: 2vw;
	}

	.btnDel {
		margin: 0 auto;
		padding: 0 35vw;
	}

	.statusStr {
		text-align: right;
		font-size: 16px;
		margin: 0 7vh 2vh 0;
		color: #3CB371;
	}

	.reason {
		text-align: right;
		font-size: 17px;
		margin-bottom: 2vh;
		color: red;
		font-weight: bold;
	}
</style>