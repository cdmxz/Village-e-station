<template>
	<view class="main" v-if="type!=3">
		<!-- 零工求职 -->
		<uni-section class="title" title="招聘描述" titleFontSize="20px">
		</uni-section>
		<view>
			<view v-if="type==1">
				<view>岗位</view>
				<uni-easyinput v-model="data.work" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="请输入" :clearable='false' maxlength="50"></uni-easyinput>
			</view>
			<view v-if="type==2">
				<view>工作内容简介</view>
				<uni-easyinput v-model="data.work" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="装修" :clearable='false' maxlength="50"></uni-easyinput>
			</view>
			<view>
				<view>招聘人数</view>
				<view class="flex">
					<uni-easyinput v-model="data.recruits_number" type="number" :styles="styles"
						:placeholderStyle="placeholderStyle" placeholder="请输入" :clearable='false'
						maxlength="4"></uni-easyinput>
					<text>人</text>
				</view>

			</view>
			<view>
				<view>薪酬</view>
				<view class="flex">
					<uni-easyinput v-model="data.salary" :styles="styles" :placeholderStyle="placeholderStyle"
						placeholder="请输入" :clearable='false'></uni-easyinput>
					<text>元/月</text>
				</view>

			</view>
			<view>
				<view>工作地点</view>
				<uni-easyinput v-model="data.work_address" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="请输入" :clearable='false'></uni-easyinput>
			</view>
			<view v-if="type==2">
				<view>
					<view>工作开始时间</view>
					<uni-datetime-picker type="datetime" v-model="data.start_time" @change="date" :clear-icon='false' />
				</view>
				<view>
					<view>工作结束时间</view>
					<uni-datetime-picker type="datetime" v-model="data.end_time" :clear-icon='false' />
				</view>
				<view>
					<view>报名截止时间</view>
					<uni-datetime-picker type="datetime" v-model="data.deadline" :clear-icon='false' />
				</view>
			</view>
			<view v-else>
				<view>招聘截止时间</view>
				<uni-datetime-picker type="datetime" v-model="data.deadline" :clear-icon='false' />
			</view>


		</view>

		<uni-section class="title" title="招聘联系人" titleFontSize="20px">

		</uni-section>
		<view>
			<view>
				<view>联系人</view>
				<uni-easyinput v-model="data.contact_person" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="请输入" maxlength="50"></uni-easyinput>
			</view>
			<view>
				<view>联系电话/邮箱</view>
				<uni-easyinput v-model="data.contact_information" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="请输入" maxlength="50"></uni-easyinput>
			</view>
		</view>

		<uni-section class="title" title="岗位详情" titleFontSize="20px">
		</uni-section>
		<view>
			<uni-easyinput type="textarea" v-model="data.job_details" placeholder="请输入" maxlength="600"></uni-easyinput>
		</view>
		<!-- 岗位详情底部提示 -->
		<uni-section title="" sub-title="1.请写明岗位名称、岗位职责、工作地、年龄要求、学历要求、薪资、五险一金、食宿、其他福利、其他要求。\n2.如招聘多个岗位,请按岗位分别列明。"
			subTitleColor="#AA7138" subTitleFontSize='14px'>
		</uni-section>


		<view>
			<view class="noteTitle">温馨提示:</view>
			<view class="note">1.本条招聘信息提交后,经后台管理员审核通过后,本条用工需求方在平台展示</view>
			<view class="note">2.到达截止时间后,本条信息会自动下架</view>
		</view>
		<button type="primary" @click="submit">提交</button>
	</view>

	<view class="main" v-else>
		<uni-section class="title" title="求职概述" titleFontSize="20px">
		</uni-section>
		<view>
			<view>
				<view>求职工作</view>
				<uni-easyinput v-model="data.expected_work" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="请输入" :clearable='false' maxlength="50"></uni-easyinput>
			</view>
			<view>
				<view>薪酬</view>
				<uni-easyinput v-model="data.expected_salary" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="请输入" :clearable='false' maxlength="50"></uni-easyinput>
			</view>
			<view>
				<view>工作地点</view>
				<uni-easyinput v-model="data.work_address" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="请输入" :clearable='false' maxlength="200"></uni-easyinput>
			</view>
			<view>
				<view>工作开始时间</view>
				<uni-datetime-picker type="datetime" v-model="data.start_time" @change="date" :clear-icon='false' />
			</view>
			<view>
				<view>工作结束时间</view>
				<uni-datetime-picker type="datetime" v-model="data.end_time" :clear-icon='false' />
			</view>
			<view>
				<view>展示下架时间</view>
				<uni-datetime-picker type="datetime" v-model="data.deadline" :clear-icon='false' :disabled="true" />
			</view>
		</view>


		<uni-section class="title" title="求职联系方式" titleFontSize="20px">
		</uni-section>
		<view>
			<view>
				<view>求职者姓名</view>
				<uni-easyinput v-model="data.contact_person" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="请输入" :clearable='false' maxlength="50"></uni-easyinput>
			</view>
			<view>
				<view>联系电话/邮箱</view>
				<uni-easyinput v-model="data.contact_information" :styles="styles" :placeholderStyle="placeholderStyle"
					placeholder="请输入" :clearable='false' maxlength="50"></uni-easyinput>
			</view>
		</view>
		<view>
			<view class="noteTitle">温馨提示:</view>
			<view class="note">1.本条招聘信息提交后,经后台管理员审核通过后,本条用工需求方在平台展示</view>
			<view class="note">2.到达截止时间后,本条信息会自动下架</view>
		</view>
		<button type="primary" @click="submit">提交</button>
	</view>
</template>

<script>
	import * as errorCode from '@/constant/errorCode.js'
	import {
		publish,
		details,
		update
	} from "@/api/job.js"
	import {
		get
	} from "@/api/enterprise.js"
	import {
		islogin
	} from '@/api/user.js'
	import * as jobType from "@/constant/jobType.js"
	import {
		textReview
	} from '../../api/review'
	import {
		requestSubscribe
	} from '@/utils/wxPush.js'

	import * as reviewType from '@/constant/reviewType.js'

	export default {
		data() {
			return {
				placeholderStyle: "font-size:14px",
				styles: {},
				data: {
					// "title": "",
					"work": "",
					"recruits_number": "",
					"salary": "",
					"contact_person": "",
					"contact_information": "",
					"work_address": "",
					// "start_time": "",
					// "end_time": "",
					"deadline": "",
					"job_details": ""
				},
				type: 1, // 招聘类型
				infoId: '', // 招聘信息id，为空表示发布招聘，不为空表示修改招聘
				tmplIds: [
					'h4c-zrBGQDoYC8_Ivl9ITe9Hm3t9leeHRtB03hWZToc', // 审核结果通知
				]
			}
		},
		methods: {
			date(e) {
				this.data.deadline = e
			},
			//提交
			async submit() {
				// 校验参数是否为空
				for (let key in this.data) {
					if (this.data[key] == '') {
						console.log('信息不完整:' + key)
						this.showError('信息不完整')
						return
					}
				}
				// 请求参数
				let paramObj = {
					type: this.type
				}
				if (this.type == jobType.ENTERPRISE_HIRES) {
					// 发布企业招聘
					await this.checkEnterpriseInformation()
					this.data.title = this.data.enterprise_information.name
					paramObj.en_hires_information = this.data
				} else if (this.type == jobType.GIG_HIRES) {
					// 发布零工招聘
					this.data.title = this.data.work
					paramObj.gig_hires_information = this.data
				} else {
					// 发布零工求职
					this.data.title = this.data.expected_work
					paramObj.job_hunting_information = this.data
				}
				// 显示加载框
				uni.showLoading({
					mask: true,
					title: '发布中...'
				})
				// 审核岗位详情（零工求职不需要岗位详情）
				if (this.type != jobType.GIG_JOB_HUNTING) {
					let resp = await textReview({
						text: this.data.job_details,
						type: reviewType.COMMENT_DETECTION
					})
					if (resp.code == errorCode.SUCCESS) {
						let tips = resp.data.risk_tips
						if (tips != "") {
							this.showError("岗位详情包含：" + tips)
							return
						}
					}
				}
				// 向后端发起请求
				let res
				if (this.infoId !== '') {
					// 修改招聘
					res = await update(paramObj)
				} else {
					// 发布招聘
					res = await publish(paramObj)
				}
				if (res.code == errorCode.SUCCESS) {
					this.showSuccess('发布成功')
					await requestSubscribe(this.tmplIds)
					setTimeout(e => uni.navigateBack(),
						500)
				} else {
					this.showError(res.msg)
				}
			},
			showError(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					icon: 'error'
				})
			},
			showSuccess(title) {
				uni.hideLoading()
				uni.showToast({
					title: title,
					icon: 'success',
					duration: 500
				})
			},
			// 检查当前用户的企业信息是否填写
			async checkEnterpriseInformation() {
				let resp = await get()
				if (resp.code == errorCode.ENTERPRISE_INFORMATION_NOT_FOUND) {
					this.showError('请填写企业信息')
					setTimeout(() => {
						uni.navigateTo({
							url: '/pages/my/enterprise'
						})
					}, 1500)
				} else if (resp.code != errorCode.SUCCESS) {
					this.showError(resp.msg)
				} else {
					this.data.enterprise_information = resp.data
				}
			}
		},
		mounted() {
			if (!islogin())
				return
			if (this.type == jobType.ENTERPRISE_HIRES) {
				// 获取当前用户的企业信息
				this.checkEnterpriseInformation()
			}
		},
		onLoad(e) {
			// 就业类型
			this.type = (typeof e.type === 'undefined') ? jobType.ENTERPRISE_HIRES : e.type
			// 招聘信息id，修改招聘信息时使用
			this.infoId = (typeof e.id === 'undefined') ? '' : e.id
			if (e.type == jobType.GIG_JOB_HUNTING) {
				this.data = {
					// "title": "",
					"expected_work": "",
					"expected_salary": "",
					"contact_person": "",
					"contact_information": "",
					"work_address": "",
					"start_time": "",
					"end_time": "",
					"deadline": ""
				}
			}
			if (this.infoId !== '') {
				// 加载招聘信息详情
				uni.setNavigationBarTitle({
					url: '修改招聘信息'
				})
				details({
					type: this.type,
					information_id: this.infoId
				}).then(e => {
					if (e.code == errorCode.SUCCESS) {
						this.data = e.data
					} else {
						this.showError(e.msg)
					}
				}).catch(e => {
					this.showError('加载招聘信息失败')
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.main {
		padding: 3vw 3vw 3vw 2vw;

		view {
			padding-left: 2vw;
			margin-bottom: 2vh;
			// padding-right: 3vw;
			font-size: 16px;
		}

		.title {
			padding-left: 0;
			font-weight: bold;
		}

		.noteImage {
			margin-left: 2vw;
			font-size: 14px;
			color: red;
		}

		.flex text {
			margin-right: 50vw;
			font-size: 16px;
			line-height: 30px;
		}

		// 底部温馨提示
		.noteTitle {
			color: red;
		}

		.note {
			font-weight: bold;
		}
	}
</style>