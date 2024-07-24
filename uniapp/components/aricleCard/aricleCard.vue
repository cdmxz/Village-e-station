<!-- 乡村振兴、农业技术 列表卡片 -->
<template>
	<view>
		<view class="article" v-show="articleType==1||articleType==3">
			<image :src="thumbnail_url" mode="" class="img" v-if="thumbnail_url!='' && thumbnail_url != null">
			</image>
			<view class="artCont">{{title}}</view>
			<view class="author">发布：{{author}}</view>
			<view class="datetime">{{datetime}}</view>
		</view>

		<view class="article" v-show="articleType==2">
			<view class="introduce">
				<image :src="thumbnail_url" mode="" v-if="thumbnail_url!='' && thumbnail_url != null"></image>
				<view class="right">
					<view class="title">{{ title }}</view>
					<view class="artCont">{{ subtitle }}</view>
					<view class="author">发布：{{author}}</view>
					<view class="datetime">{{ datetime }}</view>
				</view>
			</view>
		</view>

		<view class="article" v-show="articleType==4">
			<view class="problem">
				<view class="artCont">{{ title }}</view>
				<view class="author">发布：{{author}}</view>
				<view class="datetime">{{ datetime }}</view>
				<image :src="thumbnail_url" mode="" v-if="thumbnail_url!='' && thumbnail_url != null"></image>
			</view>
		</view>
	</view>
	</view>
</template>

<script>
	export default {
		name: "aricleCard",
		props: {
			// 略缩图链接
			thumbnail_url: {

			},
			// 标题 
			title: {

			},
			// 发布时间
			datetime: {

			},
			// 作者
			author: {

			},
			// 文章类型
			articleType: {

			},
			// 特产介绍子标题
			subtitle: {
				default: ''
			}
		},
		data() {
			return {
				// date: ""
			};
		},
		mounted() {
			// 日期时间字符串，只保留日期
			// this.date = this.datetime.split(' ')[0];
			// this.subtitle = "1"
		}

	}
</script>

<style lang="scss">
	.article {
		margin: 5vw;
		padding: 3vw;
		position: relative;
		min-height: 10vh;
		border: #d7d7d7 solid 1px;
		border-radius: 3vw;

		.img {
			width: 100%;
			height: 20vh;
			border-radius: 2vw;
		}

		.author {
			color: #767b7e;
			font-size: 12px;
			position: absolute;
			left: 4vw;
			bottom: 1vw;
		}

		// 标题
		.artCont {
			font-weight: bold;
			font-size: 16px;
			padding: 0 0 15rpx 5rpx;
			margin-bottom: 1vh;
			text-overflow: ellipsis;
			overflow: hidden;
			white-space: nowrap;
		}

		.datetime {
			color: #767b7e;
			font-size: 12px;
			position: absolute;
			right: 2vw;
			bottom: 1vw;
		}

		// 特产介绍
		.introduce {
			display: flex;

			image {
				width: 42vw;
				height: 35vw;
				margin: 1vw 0 4vw 3vw;
				border-radius: 2vw;
			}

			// 特产介绍标题
			.title {
				font-weight: bold;
				font-size: 20px;
				margin: 1vh 8vw 3vh 8vw;
			}

			.right {
				width: 60vw;

				// 特产介绍 subtitle
				.artCont {
					font-weight: 400;
					color: #666a6d;
					white-space: normal;
					margin-left: 3vh;
				}
			}
		}

		.problem {
			image {
				width: 100%;
				height: 20vh;
				border-radius: 2vw;
				margin-bottom: 2vh;
			}
		}
	}
</style>