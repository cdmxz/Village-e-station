<!-- 修改个人信息
管理员和用户一体 -->

<template>
	<view class="main">
		<view class="user">
			<!-- 头像 -->
			<view class="list">
				<view class="right">
					<text>头像</text>
				</view>
				<!-- #ifdef H5 -->
				<image @click="onChooseImg" class="circular" :src="user.avatar_url" mode=""></image>
				<!-- #endif -->
				<!-- #ifdef MP -->
				<button class="button img" open-type="chooseAvatar" @chooseavatar="onChooseAvatar">
					<image class="avatar"
						:src="user.avatar_url === '' ? '/static/avatar/default.jpg' : user.avatar_url"></image>
				</button>
				<!-- #endif -->
			</view>
			<!-- 昵称 -->
			<view class="list">
				<view class="right">
					<text class="red">*</text>
					<text>昵称</text>
				</view>
				<input type="nickname" v-model="user.nick_name" maxlength="11">
			</view>
			<!-- 姓名 -->
			<view class="list">
				<view class="right">
					<text class="red">*</text>
					<text>姓名</text>
				</view>
				<input type="text" v-model="user.name" maxlength="11">
			</view>
			<!-- 电话 -->
			<view class="list">
				<view class="right">
					<text class="red">*</text>
					<text>电话</text>
				</view>
				<input type="number" v-model="user.phone" maxlength="11">
			</view>
			<!-- 证件号码 -->
			<!-- <view class="list" v-if="!isAdmin">
				<view class="right">
					<text class="red">*</text>
					<text>证件号码</text>
				</view>
				<input type="idcard" v-model="user.id_number" maxlength="18">
			</view> -->
			<!-- 密码  管理员显示-->
			<view class="list" v-if="isAdmin">
				<view class="right">
					<text class="red"> </text>
					<text>新密码</text>
				</view>
				<input type="text" v-model="user.password" maxlength="15" placeholder="不显示旧密码,更改密码直接输入"
					@input="pwdInput">
			</view>
			<view v-show="isShowPWd1">
				<view class="list">
					<view class="right">
						<text class="red"> </text>
						<text>新密码</text>
					</view>
					<input type="text" v-model="user.password1" maxlength="15" placeholder="请再次输入新密码">
				</view>
			</view>

			<!-- 乡村 -->
			<view class="list">
				<view class="right">
					<text class="red">*</text>
					<text>乡村</text>
				</view>
				<view @click="onClickRegion">
					<input type="text" v-model="user.village" disabled="true">
				</view>
			</view>
		</view>
		<button @click="save">保存</button>
		<regionalComponents v-show="regionaStatus" ref="region" @cancel="cancel" @sure="sure" />
		<uni-popup ref="inputDialog" type="dialog">
			<uni-popup-dialog ref="inputClose" mode="input" title="请输入村庄" value="" placeholder="例如: 连樟村"
				@confirm="dialogInputConfirm">
			</uni-popup-dialog>
		</uni-popup>
	</view>
</template>

<script>
	import * as ossUploadPath from '@/constant/ossUploadPath.js'
	import * as errorCode from '@/constant/errorCode.js'
	import oss from '@/api/oss.js'
	import generateRandomFileName from '@/utils/fileUtils.js'
	import {
		update
	} from '@/api/user.js'
	import {
		information
	} from '@/api/user.js'
	import {
		trimObjectStrings,
		isEmpty
	} from '@/utils/stringUtils.js'
	import {
		isPhoneNumber
	} from '@/utils/verifyUtils.js'
	export default {
		data() {
			return {
				user: {},
				oldUser: {},
				isAdmin: false,
				isShowPWd1: false,
				regionaStatus: false,
				regiona: '',
			}
		},
		onLoad() {
			setTimeout(() => {
				this.$refs.region.getScreen();
			}, 1000)
		},
		methods: {
			onChooseAvatar(e) {
				const filePath = e.detail.avatarUrl
				this.upload(filePath)
			},
			onClickRegion() {
				this.regionaStatus = true;
				uni.hideKeyboard();
			},
			cancel() {
				this.regionaStatus = false;
			},
			sure(data) {
				// console.log(data);
				this.regionaStatus = false;
				if (!data.includes('请选择')) {
					this.regiona = data[2] + " " + data[3]
					// 输入村庄名
					this.openInputDialog();
				}

			},
			openInputDialog() {
				this.$refs.inputDialog.open()
			},
			// 对话框的确认按钮触发
			dialogInputConfirm(e) {
				if (!isEmpty(e)) {
					this.user.village = this.regiona + " " + e;
				}
			},
			// 保存用户信息到后端
			save() {
				// 如果用户信息未做更改，就不上传
				if (JSON.stringify(this.user) ==
					JSON.stringify(this.oldUser)) {
					uni.navigateBack();
					return
				}
				if (this.user.password != '') {
					if (this.user.password != this.user.password1) {
						this.showToastError('输入密码不一致')
						return
					}
				}
				if (!isPhoneNumber(this.user.phone)) {
					this.showToastError('电话输入错误')
					return
				}
				// 更新用户信息到后端
				trimObjectStrings(this.user)
				update(this.user).then(e => {
					if (e.code == errorCode.SUCCESS) {
						this.showToastSuccess('保存成功')
						// 如果修改了密码，跳转到登录页面
						if (this.user.password != '') {
							console.log('跳转到登录页面')
							setTimeout(() => {
								uni.removeStorageSync('token')
								uni.navigateTo({
									url: '/pages/login/admin'
								})
							}, 1000)
							return
						}
					} else {
						this.showToastError('保存失败')
					}
					setTimeout(() => {
						uni.navigateBack();
					}, 1000)
				})
			},
			// 选择图片上传h5
			onChooseImg() {
				uni.chooseImage({
					count: 1,
					success: async (res) => {
						const tempFilePaths = res.tempFilePaths;
						const filePath = tempFilePaths[0];
						this.upload(filePath)
					}
				})
			},
			async upload(filePath) {
				console.log('插入图片，本地路径：')
				console.log(filePath)
				// 上传图片到oss
				var randomFileName = await generateRandomFileName(filePath)
				await oss.uploadImage({
					url: ossUploadPath.AVATAR + randomFileName,
					imgPath: filePath, // 图片本地路径
					callback: (e) => { // 回调函数 
						if (e.isSuccess) {
							this.user.avatar_url = e.url
						} else {
							console.log('上传文件失败，原因：')
							console.log(e.msg)
							this.showToastError('上传失败')
						}
					}
				});
			},
			// 上传成功后，从oss删掉旧的头像
			deleteOldAvatar() {
				oss.deleteImage(this.oldUser.avatar_url)
			},
			showToastError(title) {
				uni.showToast({
					title: title,
					icon: 'error',
					mask: true
				})
			},
			showToastSuccess(title) {
				uni.showToast({
					title: title,
					icon: 'success',
					mask: true
				})
			},
			pwdInput(e) {
				// 输入新密码时，显示再次输入框
				if (e.detail.value === '') {
					this.isShowPWd1 = false
				} else {
					this.isShowPWd1 = true
				}
			}
		},
		async mounted() {
			// 获取当前用户信息
			try {
				var resp = await information();
				if (resp.code != errorCode.SUCCESS)
					throw new Error(resp.msg)
				this.user = resp.data
				this.user.password = ''
				this.user.password1 = ''
				this.isAdmin = (resp.data.is_admin == 1)
				Object.assign(this.oldUser, resp.data)
			} catch (e) {
				console.error('获取用户信息失败，原因')
				console.error(e)
				uni.showToast({
					duration: 3000,
					icon: 'error',
					title: '获取用户信息失败',
					mask: true
				})
			}
		}
	}
</script>

<style scoped lang="scss">
	.img {
		padding: 0;
		margin-left: 2vw;
		width: 15vw;
		height: 15vw;

		.avatar {
			padding: 0;
			width: 100%;
			height: 100%;
		}
	}

	.main {
		margin: 4vw;
	}

	.user {
		border: 1px solid #acacac;
		margin: 4vw 0;
		border-radius: 3vw;
		padding: 4vw;
		font-size: 16px;

		.list {
			display: flex;
			border-bottom: #acacac 1px solid;
			padding: 10px;

			.right {
				width: 20vw;
			}

			input {
				width: 60vw;
			}

			.red {
				color: red;
			}
		}

	}

	.circular {
		width: 20vw;
		height: 20vw;
		border-radius: 50%;
	}

	button {
		background-color: #1987f7;
		color: white;
	}
</style>