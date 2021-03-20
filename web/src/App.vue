<template>
  <div id="app">
    <a-layout id="components-layout-demo-top-side-2">
      <a-layout-header class="header">
        <div class="logo" />
        <a-menu
                theme="dark"
                mode="horizontal"
                :default-selected-keys="['2']"
                :style="{ lineHeight: '64px' }"
        >
          <a-menu-item key="1">
            nav 1
          </a-menu-item>
          <a-menu-item key="2">
            nav 2
          </a-menu-item>
          <a-menu-item key="3">
            nav 3
          </a-menu-item>
        </a-menu>
      </a-layout-header>
      <a-layout>
        <a-layout-sider width="200" style="background: #fff">
          <a-menu
                  mode="inline"
                  :default-selected-keys="['1']"
                  :default-open-keys="['sub1']"
                  :style="{ height: '100%', borderRight: 0 }"
          >
            <a-sub-menu key="sub1">
              <span slot="title"><a-icon type="user" />subnav 1</span>
              <a-menu-item key="1">
                option1
              </a-menu-item>
              <a-menu-item key="2">
                option2
              </a-menu-item>
              <a-menu-item key="3">
                option3
              </a-menu-item>
              <a-menu-item key="4">
                option4
              </a-menu-item>
            </a-sub-menu>
            <a-sub-menu key="sub2">
              <span slot="title"><a-icon type="laptop" />subnav 2</span>
              <a-menu-item key="5">
                option5
              </a-menu-item>
              <a-menu-item key="6">
                option6
              </a-menu-item>
              <a-menu-item key="7">
                option7
              </a-menu-item>
              <a-menu-item key="8">
                option8
              </a-menu-item>
            </a-sub-menu>
            <a-sub-menu key="sub3">
              <span slot="title"><a-icon type="notification" />subnav 3</span>
              <a-menu-item key="9">
                option9
              </a-menu-item>
              <a-menu-item key="10">
                option10
              </a-menu-item>
              <a-menu-item key="11">
                option11
              </a-menu-item>
              <a-menu-item key="12">
                option12
              </a-menu-item>
            </a-sub-menu>
          </a-menu>
        </a-layout-sider>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
          Content
        </a-layout-content>
      </a-layout>
      <a-layout-footer style="text-align: center">
        Ant Design ©2018 Created by Ant UED
      </a-layout-footer>
    </a-layout>

  </div>
</template>

<script lang="ts">
import Vue from 'vue';
import HelloWorld from './components/HelloWorld.vue';

export default Vue.extend({
  name: 'App',
  components: {

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
