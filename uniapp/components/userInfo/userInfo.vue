<template>
	<view class="userInfo">
		<image class="user" :src="comment[0].avatar_url" mode="scaleToFill"></image>
		<view class="right">
			<view>{{comment[0].user}}</view>
			<view>{{comment[0].user}}</view>
			<view>{{comment[0].user}}</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: "userInfo",
		data() {
			return {

			};
		}
	}
</script>

<style lang="scss">

</style>