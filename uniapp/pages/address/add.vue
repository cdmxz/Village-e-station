<template>
	<view>
		<view class="main">
			<view>
				<text>收货人</text>
				<uni-easyinput v-model="data.consignee" placeholder="请输入收货人姓名" :clearable='false'></uni-easyinput>
			</view>
			<view>
				<text>手机号</text>
				<uni-easyinput type="digit" v-model="data.phone" placeholder="请输入手机号"
					:clearable='false'></uni-easyinput>
			</view>
			<view class="region">
				<text>所在地区</text>
				<input class="regionInput" placeholder="请选择省市区" disabled type="text" v-model="address"
					@tap="openPicker" />
				<cc-selectDity :province="data.province" :city="data.city" :area="data.district" :show="show"
					@changeClick="changeClick" @sureSelectArea="onsetCity" @hideShow="onhideShow"></cc-selectDity>
			</view>

			<view>
				<text>详细地址</text>
				<uni-easyinput v-model="data.detail" type="textarea" placeholder="小区楼栋、门牌号等"
					:clearable='false'></uni-easyinput>

			</view>
			<button type="warn" @click="save">保存</button>
		</view>
	</view>
</template>

<script>
	import {
		add,
		updatedefault
	} from '@/api/address.js'
	export default {
		components: {

		},
		data() {
			return {
				data: {
					consignee: "",
					phone: "",
					province: "广东省",
					city: "广州市",
					district: "天河区",
					detail: ""
				},
				show: false,
				// areaCode: "440106",
				address: "",
			}
		},
		methods: {
			// 判断对象的属性是否有空属性
			haveEmptyValue(obj) {
				for (let key of Object.keys(obj)) {
					if (obj[key] === "" || obj[key] === null || (typeof obj[key] === 'object' && obj[key] === {})) {
						// 如果属性值为空字符串、null或者是个空对象，则认为该属性为空
						return true;
					}
				}
				// 遍历完所有属性都没有找到空值，则认为对象的所有属性都不为空
				return false;
			},
			save() {
				let result = this.haveEmptyValue(this.data)
				if (result) {
					this.showError('数据不完整')
					return
				}
				add(this.data).then((res) => {
					if (res.code != 200) {
						this.showError(req.msg)
						return
					}
					updatedefault({
						address_id: res.data.address_id
					}).then((res) => {
						if (res.code != 200) {
							this.showError(res.msg)
						} else {
							this.showSuccess('新建成功')
							setTimeout(e => {
								uni.navigateBack()
							}, 1500)
						}
					})
				})
			},
			openPicker() {
				console.log('打开地址选择器')
				this.show = true
			},
			changeClick(value, value2, value3, value4) {
				console.log('地址选择器 = ' + value + value2 + value3 + value4);

				this.data.province = value;
				this.data.city = value2;
				this.data.district = value3;
				this.areaCode = value4;
			},
			onhideShow() {
				this.show = false
				console.log('关闭地址选择器')
			},
			//选中省市区
			onsetCity(e) {
				// let data = e.detail.target.dataset;
				this.address = this.data.province + '/' + this.data.city + '/' + this.data.district;
				// this.data.province = data.province;
				// this.data.city = data.city;
				// this.data.district = data.area;
				this.show = false
			},
			showError(title) {
				uni.showToast({
					mask: true,
					icon: 'error',
					title: title
				})
			},
			showSuccess(title) {
				uni.showToast({
					mask: true,
					icon: 'success',
					title: title
				})
			}
		},
		mounted() {}
	}
</script>

<style lang="scss" scoped>
	.main {
		padding: 5vh 4vw;

		view {
			display: flex;
			padding: 2vh 3vw;

			text {
				width: 23vw;
				margin-right: 5vw;
			}

			.icon {
				padding: 0;
				font-size: 24px;
			}

			textarea,
			.bor {
				border: 1px solid #afafaf;
			}

		}

		.region {
			padding-right: 0;
		}

		.regionInput {
			font-size: 12px;
			padding: 2vw 1vw;
			width: 60%;
			// margin-left: 3vw;
			border: 1px solid #cfcfcf;
			border-radius: 1vw;
		}
	}
</style>