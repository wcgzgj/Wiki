import { createApp } from "vue";
import App from "./App.vue";
import router from './router'
import store from './store'

import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/antd.css';

// Vue.config.productionTip = false
//
// Vue.use(Antd);
//
// new Vue({
//   router,
//   store,
//   render: h => h(App)
// }).$mount('#app')


const app = createApp(App)
app.config.globalProperties.$http = ()=>{
  console.log("")
}
app.use(Antd)
app.use(store).use(router).mount('#app')