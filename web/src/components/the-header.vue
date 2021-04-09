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


            <!--气泡确认框-->
            <a-popconfirm
                    title="是否退出？"
                    ok-text="确认"
                    cancel-text="取消"
                    @confirm="logout"
            >
                <a class="login-menu" v-show="user.id">
                    <span>退出登录</span>
                </a>
            </a-popconfirm>

            <!--下面两个部分，是互斥显示的-->
            <!--当 user 有数据的时候，显示 您好: xxx-->
            <!--当 user 没有数据的时候，说明没有登录，就显示登录按钮-->
            <a class="login-menu" v-show="user.id">
                <span>你好: {{user.name}}</span>
            </a>


            <a class="login-menu" @click="showLoginModal" v-show="!user.id">
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
    import { defineComponent, ref ,computed} from 'vue';
    import axios from "axios";
    import {message} from "ant-design-vue";
    import store from "@/store";

    declare let hexMd5;
    declare let KEY;

    export default {
        name: "the-header",

        setup() {
            const loginUser = ref();
            loginUser.value= {
                loginName: "test",
                password: "abc123"
            };

            const user = computed(() => store.state.user);

            const loginModalVisible = ref();
            loginModalVisible.value=false;

            const loginModalLoading = ref();
            loginModalLoading.value=false;

            const showLoginModal= ()=> {
                loginModalVisible.value=true;
            }


            /**
             * 登录
             */
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
                        /**
                         * setUser: vuex 中 mutations中的方法
                         *
                         * 后面的参数，都是我们在 mutations 中自定义方法的参数
                         * state 参数因为是自带的，所以没有必要写
                         */
                        store.commit("setUser",data.content);
                    } else {
                        /**
                         * 使用 antd 的组件，弹出错误信息
                         */
                        message.error(data.message);
                    }
                });
            }

            /**
             * 退出登录
             */
            const logout = () => {
                console.log("退出登录!")

                axios.get("/user/logout/"+user.value.token).then((response)=>{
                    const data = response.data;
                    if (data.success) {
                        loginModalVisible.value=false;
                        message.success("退出登录成功!")

                        /**
                         * 退出登录时
                         * 将 sessionStorage 中对应 user的信息清空
                         * 因为
                         */
                        store.commit("setUser", {});
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
                user,

                //method
                showLoginModal,
                login,
                logout
            }
        }
    }
</script>

<style scoped>
    .login-menu {
        float: right;
        color: white;
        padding-left: 10px;
    }

</style>