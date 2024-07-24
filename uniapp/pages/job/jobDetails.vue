<template>
	<view>
		<!-- 等待数据加载完成在渲染，避免出现错误： xxx undefined -->
		<view class="content" v-if="data">
			<list v-if='type!=3'>
				<template #title>
					<text>{{data.title}}</text>
				</template>
				<template #name>
					<text>岗位：{{data.work}}</text>
				</template>

				<template #number>
					<view class="number">
						招聘人数：{{data.recruits_number}}
					</view>
				</template>

				<template #money>
					<text>报 酬：{{data.salary}}</text>
				</template>

				<template #address>
					<view>
						<text>联系人：{{data.contact_person}}</text>
					</view>
					<view>
						<text>联系电话/邮箱：{{data.contact_information}}</text>
					</view>
					<view>
						<text>工作地：{{data.work_address}}</text>
					</view>
					<view>
						<text>发布时间：{{data.publish_time}}</text>
					</view>
					<view>
						<text>招聘截止时间：{{data.deadline}}</text>
					</view>
				</template>
			</list>

			<list v-else>
				<template #title>
					<text>{{data.title}}</text>
				</template>
				<template #name>
					<text>求职工作：{{data.expected_work}}</text>
				</template>

				<template #number>
					<view class="number">
						求职者：{{data.contact_person}}
					</view>
				</template>

				<template #money>
					<text>期望报酬：{{data.expected_salary}}</text>
				</template>

				<template #address>
					<view>
						<text>联系电话：{{data.contact_information}}</text>
					</view>
					<view>
						<text>工作地点：{{data.work_address}}</text>
					</view>
					<view>
						<text>工作开始时间：{{data.publish_time}}</text>
					</view>
					<view>
						<text>工作结束时间：{{data.deadline}}</text>
					</view>
				</template>

			</list>

			<view v-if='type!=3'>
				<view class="title">岗位详情</view>
				<uni-card>
					<view v-html="data.job_details">
					</view>
				</uni-card>
			</view>
			<view v-if="type==1 && data.enterprise_information">
				<view class="title">企业信息</view>
				<uni-card>
					<view>
						企业名称：{{data.enterprise_information.name}}
					</view>
					<view>
						信用代码：{{data.enterprise_information.credit_code}}
					</view>
					<view>
						成立时间：{{data.enterprise_information.established_time}}
					</view>
					<view>
						企业地址：{{data.enterprise_information.address}}
					</view>
					<view>
						营业执照：
						<image :src="data.enterprise_information.business_license_url" @click="OnLicenseClick"></image>
					</view>
					<view>
						招聘授权委托书：
						<image :src="data.enterprise_information.authorization_letter_url" @click="OnLetterClick">
						</image>
					</view>
					<view>
						企业实景：
						<image :src="data.enterprise_information.photo_url" @click="OnPhotoClick"></image>
					</view>
				</uni-card>
			</view>

		</view>

	</view>
</template>

<script>
	import {
		details
	} from '@/api/job.js'
	import list from "@/components/JobList.vue"
	export default {
		components: {
			list
		},
		data() {
			return {
				data: [],
				type: ''
			};
		},
		methods: {
			OnPhotoClick() {
				uni.previewImage({
					urls: [this.data.enterprise_information.photo_url]
				})
			},
			OnLetterClick() {
				uni.previewImage({
					urls: [this.data.enterprise_information.authorization_letter_url]
				})
			},
			OnLicenseClick() {
				uni.previewImage({
					urls: [this.data.enterprise_information.business_license_url]
				})
			},
			showError(title) {
				uni.showToast({
					icon: 'error',
					mask: true,
					title: title,
				})
			}
		},
		async onLoad(e) {
			// this.type = e.type = 3
			this.type = e.type
			// e.id = '1737817914640052226'
			try {
				let res = await details({
					type: e.type,
					information_id: e.id
				});
				if (res.code == 200) {
					this.data = res.data

				} else {
					this.showError(res.msg)
				}
			} catch (e) {
				console.error('加载就业详情失败，原因：')
				console.error(e)
				this.showError('加载失败')
			}
		}
	}
</script>

<style scoped lang="scss">
	.content {
		.number {
			padding: 2vw 4vw;
		}

		view {
			padding: 1vh 0;
		}

		.title {
			margin-left: 2vw;
			font-size: 18px;
			font-weight: bold;
		}

		image {
			display: block;
			width: 100%;
		}

		view {
			overflow: visible;
			white-space: normal;
		}
	}
</style>