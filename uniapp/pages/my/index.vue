<script>
	import top from "@/components/top.vue"
	import mylink from "@/components/mylink.vue"
	import bottom from "@/components/bottom.vue"
	import * as errorCode from "@/constant/errorCode.js"
	import {
		information,
		cancellation,
		islogin
	} from "@/api/user.js"
	export default {
		components: {
			top,
			mylink
		},
		data() {
			return {
				userPage: [{
						url: '/pages/my/information',
						icon: 'account-fill',
						color: '#FF9D26',
						title: '个人信息',
						subtitle: '编辑个人信息',
					},
					{
						url: '/pages/my/job',
						icon: 'account-fill',
						color: '#FD5242',
						title: '我的就业',
						subtitle: '我的就业入口',
					},
					{
						url: '/pages/my/enterprise',
						icon: 'account-fill',
						color: '#FD5242',
						title: '我的企业信息',
						subtitle: '我的企业信息入口',
					},
					{
						url: '/pages/my/publish',
						icon: 'chat-fill',
						color: '#36CD7E',
						title: '我的发布和评论',
						subtitle: '我的发布入口',
					},
					{
						url: '/pages/my/privacy',
						icon: 'info-circle-fill',
						color: '#54BCBD',
						title: '隐私与协议',
						subtitle: '隐私与协议入口',
					}
				],
				adminPage: [{
						url: '/pages/my/job?isAdmin=true',
						icon: 'coupon',
						color: '#FC2511',
						title: '招聘信息管理',
						subtitle: '审核招聘信息和查看所有招聘信息入口',
					},
					{
						url: '/pages/article/publishArticle?isAdmin=true',
						icon: 'arrow-up-fill',
						color: '#36CD7E',
						title: '发布文章',
						subtitle: '发布文章入口',
					},
					{
						url: '/pages/article/articleList?isAdmin=true',
						icon: 'file-text-fill',
						color: '#FD5242',
						title: '所有文章',
						subtitle: '查看和管理文章入口',
					},
					{
						url: '/pages/my/review',
						icon: 'file-text-fill',
						color: '#FD5242',
						title: '评论审核',
						subtitle: '评论审核入口',
					},
					{
						url: '/pages/shopping/shop?id=0&isadmin=1',
						icon: 'bag-fill',
						color: '#379FD1',
						title: '商品管理',
						subtitle: '商品管理入口',
					},
					{
						url: '/pages/shopping/category',
						icon: 'bag-fill',
						color: '#379FD1',
						title: '商品分类管理',
						subtitle: '商品分类管理入口',
					},
					{
						url: '/pages/order/index?isadmin=1',
						icon: 'order',
						color: '#6FBADE',
						title: '订单管理',
						subtitle: '订单管理入口',
					},
					{
						url: '/pages/my/userList',
						icon: 'account',
						color: '#e2d84b',
						title: '用户列表',
						subtitle: '用户列表入口',
					}
				],
				user: {},
				isAdmin: false, // 是否显示管理员页面
				isShowModal: false,
				modalTitle: '确定注销账号吗？',
				modalContent: '注销账号后将不可恢复您的数据'
			}
		},
		methods: {
			// 跳转到登录页面
			toLoginPage() {
				uni.redirectTo({
					url: '/pages/login/index'
				})
			},
			// 退出登录
			onLogout() {
				uni.removeStorageSync('token')
				this.toLoginPage()
			},
			// 确定注销账号
			async confirm() {
				this.isShowModal = false
				const resp = await cancellation();
				if (resp.code == errorCode.SUCCESS) {
					this.showSuccess('注销成功')
				} else {
					this.showError('注销失败')
				}
				// 延时跳转到登录页面
				setTimeout(() => {
					uni.removeStorageSync('token')
					this.toLoginPage()
				}, 1500)
			},
			// 取消注销账号
			cancel() {
				this.isShowModal = false
			},
			showSuccess(title) {
				uni.showToast({
					title: title,
				})
			},
			showError(title) {
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
		},
		mounted() {

		},
		async onShow() {
			// 获取当前用户信息
			try {
				var resp = await information();
				if (resp.code != errorCode.SUCCESS)
					throw new Error(resp.msg)
				this.user = resp.data
				this.isAdmin = (resp.data.is_admin == 1)
				this.user.phone = this.user.phone.substring(0, 3) + '****' + this.user.phone.substr(7, 4)
			} catch (e) {
				console.error('获取用户信息失败，原因')
				console.error(e)
				this.showError('请先登录')
				// 延时跳转到登录页面
				setTimeout(() => {
					uni.removeStorageSync('token')
					this.toLoginPage()
				}, 1500)
			}
		}
	}
</script>
<template>
	<view>
		<!-- <top></top> -->
		<view class="top"></view>
		<!-- 用户信息 -->
		<view class="userInfo">
			<image :src="user.avatar_url" mode="" class="avatar"></image>
			<view class="more">
				<view>{{user.name}}</view>
				<view>{{user.phone}}</view>
			</view>
			<view v-if="isAdmin" class="adminFont">管理员</view>
		</view>

		<!-- 用户 和 管理员显示的页面列表 -->
		<mylink v-for="i in userPage" :key="i.title" :url='i.url'>
			<template #image>
				<u-icon :name="i.icon" :color="i.color" size="32"></u-icon>
				<!-- <image :src="i.url" mode="" class="image"></image> -->
			</template>
			<template #title>
				<text>{{i.title}}</text>
			</template>
			<template #subtitle>
				<text>{{i.subtitle}}</text>
			</template>
		</mylink>

		<!-- 客服按钮 -->
		<open-contact />

		<!-- 管理员显示的页面列表  -->
		<view v-if="isAdmin" class="admin">
			<view class="title">管理员</view>
			<mylink v-for="i in adminPage" :key="i.title" :url='i.url'>
				<template #image>
					<u-icon :name="i.icon" :color="i.color" size="32"></u-icon>
				</template>
				<template #title>
					<text>{{i.title}}</text>
				</template>
				<template #subtitle>
					<text>{{i.subtitle}}</text>
				</template>
			</mylink>
		</view>

		<!-- 底部按钮 -->
		<view class="btn">
			<button type="primary" class="blue" @click="onLogout()">退出登录
			</button>
			<button type="warn" @click="isShowModal = true">注销账号</button>
		</view>

		<u-modal :show="isShowModal" :title="modalTitle" :content="modalContent" :showCancelButton="true"
			:buttonReverse="true" @confirm="confirm()" @cancel="cancel()"></u-modal>
	</view>
</template>


<style scoped lang="scss">
	.top {
		background-color: #3cb371;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.userInfo {
		background-color: #3cb371;
		height: 13vh;
		display: flex;
		align-items: center;
		margin-bottom: 8rpx;
		font-size: 18px;

		.avatar {
			width: 17vw;
			height: 17vw;
			border: 1px solid white;
			margin-left: 10vw;
			border-radius: 15vw;
		}

		.more {
			color: white;
			margin-left: 5vw;
			height: 70%;
			display: flex;
			flex-direction: column;
			justify-content: space-around;
		}

		.adminFont {
			margin-left: 30rpx;
			color: red;
			font-size: 16px;
		}
	}

	.image {
		width: 30px;
		height: 30px;
	}

	.admin {
		margin-top: 5vh;
		padding-bottom: 5vh;
		border: 1px solid #ddd;
		border-radius: 3vw;
		padding-bottom: 5vw;

		.title {
			font-weight: 600;
			text-align: center;
			font-size: 20px;
			padding: 2vh 0;
		}
	}

	.btn {
		margin: 0 5vw;
		padding-bottom: 10vh;

		button {
			margin: 8vh 0;
		}

		.blue {
			background-color: #007aff;

		}
	}
</style>