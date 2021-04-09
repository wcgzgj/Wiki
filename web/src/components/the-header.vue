<template>
    <a-layout-header class="header">
        <div class="logo" />
        <a-menu
                theme="dark"
                mode="horizontal"
                v-model:selectedKeys="selectedKeys1"
                :style="{ lineHeight: '64px' }"
        >

            <a-menu-item key="/">
                <router-link to="/">首页</router-link>
            </a-menu-item>

            <a-menu-item key="/admin/user">
                <router-link to="/admin/user">用户管理</router-link>
            </a-menu-item>

            <a-menu-item key="/admin/ebook">
                <router-link to="/admin/ebook">电子书管理</router-link>
            </a-menu-item>

            <a-menu-item key="/admin/category">
                <router-link to="/admin/category">分类管理</router-link>
            </a-menu-item>

            <a-menu-item key="/about">
                <router-link to="/about">关于我们</router-link>
            </a-menu-item>

            <a class="login-menu" @click="showLoginModal">
                <span>登录</span>
            </a>

        </a-menu>
    </a-layout-header>


    <a-modal
            title="登录表单"
            v-model:visible="loginModalVisible"
            :confirm-loading="loginModalLoading"
            @ok="login"
    >
        <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <a-form-item label="用户名">
                <a-input v-model:value="loginUser.loginName" />
            </a-form-item>
            <a-form-item label="密码">
                <a-input v-model:value="loginUser.password" type="password"/>
            </a-form-item>
        </a-form>
    </a-modal>


</template>

<script lang="ts">
    import { defineComponent, ref } from 'vue';
    import axios from "axios";
    import {message} from "ant-design-vue";

    declare let hexMd5;
    declare let KEY;

    export default {
        name: "the-header",

        setup() {
            const loginUser = ref();
            loginUser.value= {
                loginName: "test",
                password: "test"
            };

            const loginModalVisible = ref();
            loginModalVisible.value=false;

            const loginModalLoading = ref();
            loginModalLoading.value=false;

            const showLoginModal= ()=> {
                loginModalVisible.value=true;
            }


            const login = () => {
                console.log("开始登录!")
                loginModalLoading.value=true;

                loginUser.value.password = hexMd5(loginUser.value.password + KEY);

                axios.post("/user/login",loginUser.value).then((response)=>{
                    loginModalLoading.value=false;
                    const data = response.data;
                    if (data.success) {
                        loginModalVisible.value=false;
                        message.success("登录成功!")
                    } else {
                        /**
                         * 使用 antd 的组件，弹出错误信息
                         */
                        message.error(data.message);
                    }
                });
            }

            return {
                //ref
                loginUser,
                loginModalVisible,
                loginModalLoading,

                //method
                showLoginModal,
                login
            }
        }
    }
</script>

<style scoped>
    .login-menu {
        float: right;
        color: white;
    }

</style>