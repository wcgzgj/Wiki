<template>
  <a-layout id="components-layout-demo-top-side-2">
    <the-header></the-header>
    <router-view></router-view>
    <the-footer></the-footer>
  </a-layout>
</template>


<script lang="ts">
  import { defineComponent ,onMounted , ref} from 'vue';
  //1. 导入组件
  import TheHeader from './components/the-header.vue';
  import TheFooter from './components/the-footer.vue';


  export default defineComponent({
    name: 'App',
    components: {
      //2. 放置组件
      TheHeader,
      TheFooter
    },

  });
</script>

<style lang="scss">

#components-layout-demo-top-side-2 .logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.2);
  margin: 16px 28px 16px 0;
  float: left;
}

</style>
