<template>
	<view class="main" :style="{width: size}" >
		<input type="text" :placeholder="placeholder"  >
		<uni-icons type="search" class="search"></uni-icons>
	</view>
</template>

<script>
	export default {
		name:'IndexSearch',
		props:{
			placeholder:String,
			size:String
		},
		data() {
			return {
				
			}
		},
		methods: {
			
		}
	}
</script>

<style scoped lang="scss">
	.main{
		position: relative;
		background-color: white;
		border-radius: 2vw;
		padding:1vw 3vw ;
		font-size: 12px;
		
		.search{
			position: absolute;
			right: 2vw;
			top: 50%;
			transform: translateY(-50%);
		}
	}
	
	
</style>
