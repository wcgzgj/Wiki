<template>
    <a-layout-footer style="text-align: center">
        FARO_Z 电子书<span v-show="user.id">，欢迎：{{user.name}}</span>
    </a-layout-footer>
</template>

<script lang="ts">
    import {computed, onMounted, ref} from "vue";
    import store from "@/store";
    import {Tool} from "@/util/tool";

    export default {
        name: "the-footer",
        setup() {
            // user 会根据 computed函数，动态监听 store.state.user的变化
            // 之前也说过，computed 和缓存差不多
            // 如果缓存监听的内容没有变化，那就直接从缓冲中取
            // 如果缓存监听的内容发生了插入、更新等操作，那就修改缓存，user 的值也随之改变
            const user = computed(() => store.state.user);

            let websocket: any;
            let token: any;
            const onOpen = () => {
                console.log('WebSocket连接成功，状态码：', websocket.readyState)
            };
            const onMessage = (event: any) => {
                console.log('WebSocket收到消息：', event.data);
            };
            const onError = () => {
                console.log('WebSocket连接错误，状态码：', websocket.readyState)
            };
            const onClose = () => {
                console.log('WebSocket连接关闭，状态码：', websocket.readyState)
            };
            const initWebSocket = () => {
                // 连接成功
                websocket.onopen = onOpen;
                // 收到消息的回调
                websocket.onmessage = onMessage;
                // 连接错误
                websocket.onerror = onError;
                // 连接关闭的回调
                websocket.onclose = onClose;
            };

            onMounted(() => {
                // WebSocket
                if ('WebSocket' in window) {
                    token = Tool.uuid(10);
                    // 连接地址：ws://127.0.0.1:8880/ws/xxx
                    websocket = new WebSocket(process.env.VUE_APP_WS_SERVER + '/ws/' + token);
                    initWebSocket()

                    // 关闭
                    // websocket.close();
                } else {
                    alert('当前浏览器 不支持')
                }
            });


            return {
                user
            }
        }
    }
</script>

<style scoped>

</style>