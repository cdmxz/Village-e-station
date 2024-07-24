
// #ifndef VUE3
import Vue from 'vue'
import App from './App'
import uView from '@/uni_modules/uview-ui'
Vue.use(uView);

Vue.config.productionTip = false

App.mpType = 'app'

uni.$on('beforeRouterEnter',(to,from,next)=>{
	console.log(to)
	console.log(from)
	console.log(next)
	console.log(1111111111)
})
const app = new Vue({
    ...App
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
import App from './App.vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif