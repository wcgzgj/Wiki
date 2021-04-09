<template>
    <a-layout-footer style="text-align: center">
        FARO_Z 电子书，欢迎：{{user.name}}
    </a-layout-footer>
</template>

<script>
    import {computed,ref} from "vue";
    import store from "@/store";

    export default {
        name: "the-footer",
        setup() {
            // user 会根据 computed函数，动态监听 store.state.user的变化
            // 之前也说过，computed 和缓存差不多
            // 如果缓存监听的内容没有变化，那就直接从缓冲中取
            // 如果缓存监听的内容发生了插入、更新等操作，那就修改缓存，user 的值也随之改变
            const user = computed(() => store.state.user);

            // const user = ref();
            // user.value=store.state.user;

            // const user = ref();
            // user.value=computed(()=> {
            //     store.state.user;
            // })

            return {
                user
            }
        }
    }
</script>

<style scoped>

</style>