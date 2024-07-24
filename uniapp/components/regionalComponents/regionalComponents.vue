<template>
	<view class="content">
		<view class="region-box-layer"></view>
		<view class="region-box" :style="'height:'+ heightCot + '%'">
			<view class="title-box">
				<text class="title-txt" @click="cancel">取消</text>
				<text class="title-txt" @click="sure">确定</text>
			</view>
			<view class="map-txt">
				<text :class="showIndex== 0 ?'select':''" @click="anewSelect(0)">{{province}}</text>
				<text v-if="province != '请选择'">-</text>
				<text v-if="province != '请选择'" :class="showIndex== 1 ?'select':''"
					@click="anewSelect(1)">{{city}}</text>
				<text v-if="city != '请选择'">-</text>
				<text v-if="city != '请选择'" :class="showIndex== 2 ?'select':''" @click="anewSelect(2)">{{area}}</text>
				<text v-if="area != '请选择'">-</text>
				<text v-if="area != '请选择'" :class="showIndex== 3 ?'select':''" @click="anewSelect(3)">{{street}}</text>
			</view>
			<view class="main-box">
				<view v-if="showIndex == 0" class="list-box" @click="selectPro(index,item.label)"
					v-for="(item,index) in provinceData" :key="index">
					<text>{{item.label}}</text>
				</view>
				<view v-if="showIndex == 1" class="list-box" @click="selectCity(index,item.label)"
					v-for="(item,index) in cityData" :key="index">
					<text>{{item.label}}</text>
				</view>
				<view v-if="showIndex == 2" class="list-box" @click="selectaArea(index,item.label)"
					v-for="(item,index) in areaData" :key="index">
					<text>{{item.label}}</text>
				</view>
				<view v-if="showIndex == 3" class="list-box" @click="selectStreet(index,item.label)"
					v-for="(item,index) in streetsData" :key="index">
					<text>{{item.label}}</text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import provinceData from './city-data/province.js';
	export default {

		data() {
			return {
				showIndex: 0, //地区显示
				province: "请选择",
				city: "请选择",
				area: "请选择",
				street: "请选择",
				heightCot: 30, //设置屏幕高度 0 ~ 100
				provinceData: '', // 当前展示的省数据
				proIndex: '', //当前省的下标
				cityData: '', // 当前展示的市数据
				cityIndex: 0,
				areaData: '', //当前展示的区数据
				areaIndex: 0,
				streetsData: '', //当前展示的区数据
			}
		},
		mounted() {
			this.provinceData = provinceData;
		},
		methods: {
			//组件高度自适应
			getScreen() {
				let that = this;
				uni.getSystemInfo({
					success: res => {
						that.heightCot = (res.safeArea.height * 2) / 2
					}
				})
			},
			//取消
			cancel() {
				this.$emit('cancel')
			},
			//确定
			sure() {
				const province = this.province; //省
				const city = this.city; //市
				const area = this.area; //区县
				const street = this.street; //街镇
				const data = [province, city, area, street];
				this.$emit('sure', data)
			},
			//下拉选择
			anewSelect(num) {
				switch (num) {
					case 0:
						this.showIndex = 0;
						this.areaData = [];
						this.streetsData = [];
						this.city = '请选择';
						this.area = '请选择';
						this.street = '请选择'
						break;
					case 1:
						this.showIndex = 1;
						this.areaData = [];
						this.streetsData = [];
						this.area = '请选择';
						this.street = '请选择'
						break;
					case 2:
						this.showIndex = 2;
						this.streetsData = [];
						this.street = '请选择'
						break;
					case 3:
						break;
				}
			},
			//高德获取地区信息
			getMap(name, type) { //name选择名称 type类型
				let that = this;
				uni.request({
					url: 'https://restapi.amap.com/v3/config/district?parameters',
					data: {
						key: '88e5354cd6aee65f55fc91cb2709a83c',
						subdistrict: 1,
						keywords: name,
					},
					success: (res) => {
						let arr = res.data.districts[0].districts;
						let data = [];
						arr.forEach(item => {
							data.push({
								label: item.name
							})
						});

						if (type == 'pro') {
							that.cityData = []; // 市的所有數據
							that.cityData = data;
						} else if (type == 'city') {
							that.areaData = []; // 区的所有数据
							that.areaData = data;
						} else if (type == 'area') {
							that.streetsData = []; // 街道的所有数据
							that.streetsData = data;
						}

					}
				});
			},
			//选择当前省
			selectPro(index, label) {
				this.getMap(label, 'pro');
				this.proIndex = index; // 当前省的下标
				this.province = label;
				this.showIndex = 1;
			},
			//选择当前市
			selectCity(index, label) {
				this.getMap(label, 'city');
				this.city = label;
				this.cityIndex = index; // 当前市的下标
				this.showIndex = 2;
			},
			// 区、县
			selectaArea(index, label) {
				this.getMap(label, 'area');
				this.area = label;
				this.showIndex = 3;
			},
			//街、镇
			selectStreet(index, label) {
				this.street = label;
			}
		}
	}
</script>

<style lang="less" scoped>
	.region-box-layer {
		position: fixed;
		z-index: 998;
		top: 0;
		bottom: 0;
		left: 0;
		right: 0;
		background-color: rgba(0, 0, 0, .6);
	}

	.region-box {
		position: fixed;
		z-index: 999;
		bottom: 0;
		left: 0;
		right: 0;
		background-color: #FFFFFF;
		padding: 20upx;
		border-top: 1upx solid rgba(0, 0, 0, .2);
		border-radius: 16upx 16upx 0 0;
		max-height: 60%;

		.title-box {
			padding: 0 10upx 30upx 10upx;
			display: flex;
			justify-content: space-between;

			// border-bottom: 2upx solid #f2f2f2;
			.title-txt {
				color: #343434;
				font-size: 30upx;

				&:nth-child(2) {
					color: #1E90FF;
				}
			}
		}

		.map-txt {
			display: flex;
			justify-content: center;
			color: #343434;
			font-size: 30upx;

			text {
				margin: 0 12upx;
				border-bottom: 2upx solid #dedede;

				&:nth-child(even) {
					border: none;
				}
			}

			.select {
				color: #1E90FF;
				position: relative;

				&::after {
					content: '';
					width: 100%;
					height: 6upx;
					background: #1E90FF;
					position: absolute;
					left: 0;
					bottom: -8upx;
					// margin-left: -20upx;
				}
			}
		}

		.main-box {
			color: #343434;
			font-size: 30upx;
			height: 100%;
			overflow: auto;

			.list-box {
				display: flex;
				flex-direction: column;
				padding: 20upx 0;
				border-bottom: 2upx solid #dedede;
			}
		}
	}
</style>