<template>
	<view >
		<view class="main">
		<view class="">
			<text>收货人</text>
			<uni-easyinput v-model="data.consignee"  placeholder="请输入收货人姓名" :clearable='false'></uni-easyinput>
		</view>
		<view class="">
			<text>手机号</text>
			<uni-easyinput v-model="data.phone"  placeholder="请输入手机号" :clearable='false'></uni-easyinput>
		</view>
		<view class="">
			<text>所在地区</text>
			<uni-data-picker :localdata="items" v-model="address" popup-title="选择省市区街道" :clear-icon="false"
				@change="onchange"></uni-data-picker>
			<view class="icon">
				&#xebbb;
			</view>
		</view>
		<view class="">
			<text>详细地址</text>
			<uni-easyinput v-model="data.detail" type="textarea"  placeholder="小区楼栋、门牌号等" :clearable='false'></uni-easyinput>
			
		</view>
			<button type="warn" @click="change">保存</button>
		</view>
	</view>
</template>

<script>
	import {updateaddress} from '@/api/address.js'
	export default {
		data() {
			return {
				data:{},
				show:true,
				address:'',
				items: [{
						text: "广东省",
						value: "广东省",
						children: [{
								text: "深圳市",
								value: "深圳市",
								children: [{
									text: "罗湖区",
									value: "罗湖区"
								}]
							},
							{
								text: "清远市",
								value: "清远市",
								children: [{
									text: "清城区",
									value: "清城区"
								}]
							}
						]
					},
					{
						text: "江西省",
						value: "江西省",
						children: [{
								text: "南昌市",
								value: "南昌市",
								children: [{
									text: "东湖区",
									value: "东湖区"
								}]
							},
							{
								text: "赣州市",
								value: "赣州市",
								children: [{
									text: "章贡区",
									value: "章贡区"
								}]
							}
						]
					},
					{
						text: "浙江省",
						value: "浙江省",
						children: [{
								text: "杭州市",
								value: "杭州市",
								children: [{
									text: "上城区",
									value: "上城区"
								}]
							},
							{
								text: "温州市",
								value: "温州市",
								children: [{
									text: "鹿城区",
									value: "鹿城区"
								}]
							}
						]
					}
				]
			}
		},
		methods: {
			change(){
				updateaddress(this.data).then(e=>{
					console.log(e)
					uni.navigateBack()
				})
			}
		},
		onLoad(e) {
			this.data=JSON.parse(e.updata)
			this.address=this.data.district
		}
		
	}
</script>

<style lang="scss" scoped>
.main{
	padding: 5vh 4vw;
	view{
		display: flex;
		padding: 2vh 3vw;
		text{
			width: 23vw;
			margin-right: 5vw;
		}
		.icon{
			padding: 0;
			font-size: 24px;
		}
		input,textarea,.bor{
			border: 1px solid #afafaf;
			
		}
		
	}
}
</style>
