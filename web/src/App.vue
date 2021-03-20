<template>
  <div id="app">
    <a-layout id="components-layout-demo-top-side-2">
      <!-- 3. 使用组件-->
      <the-header></the-header>
      <router-view></router-view>
      <the-footer></the-footer>
    </a-layout>

  </div>
</template>

<script lang="ts">
import Vue from 'vue';
//1. 导入组件
import TheHeader from './components/the-header.vue';
import TheFooter from './components/the-footer.vue';


export default Vue.extend({
  name: 'App',
  components: {
    //2. 放置组件
    TheHeader,
    TheFooter
  }
});
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

#components-layout-demo-top-side-2 .logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 28px 16px 0;
  float: left;
}
</style>
