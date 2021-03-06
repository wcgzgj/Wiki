<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <p>
                <a-form layout="inline" :model="param">
                    <a-form-item>
                        <a-input v-model:value="param.loginName" placeholder="登录名">
                        </a-input>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="handleQuery({page: 1, size: pagination.pageSize})">
                            查询
                        </a-button>
                    </a-form-item>
                    <a-form-item>
                        <a-button type="primary" @click="add()">
                            新增
                        </a-button>                    </a-form-item>
                </a-form>
            </p>
            <a-table
                    :columns="columns"
                    :row-key="record => record.id"
                    :data-source="users"
                    :pagination="pagination"
                    :loading="loading"
                    @change="handleTableChange"
            >
                <template v-slot:action="{ record }">
                    <a-space size="small">
                        <a-button type="primary" @click="edit(record)">
                            编辑
                        </a-button>
                        <a-button type="primary" @click="resetPassword(record)">
                            重置密码
                        </a-button>
                        <a-popconfirm
                                title="删除后不可恢复，确认删除?"
                                ok-text="是"
                                cancel-text="否"
                                @confirm="handleDelete(record.id)"
                        >
                            <a-button type="danger">
                                删除
                            </a-button>
                        </a-popconfirm>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>

    <!--用户信息修改表单-->
    <a-modal
            title="用户表单"
            v-model:visible="modalVisible"
            :confirm-loading="modalLoading"
            @ok="handleModalOk"
    >
        <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <a-form-item label="登录名">
                <!--如果 user.id 有值，即是进行编辑操作的，就让输入框不可以更改-->
                <!--因为我们不允许修改登录名-->
                <a-input v-model:value="user.loginName" :disabled="!!user.id"/>
            </a-form-item>
            <a-form-item label="昵称">
                <a-input v-model:value="user.name" />
            </a-form-item>
            <!--v-show，是为了让我们在编辑的时候，无法修改密码-->
            <a-form-item label="密码" v-show="!user.id">
                <a-input v-model:value="user.password" />
            </a-form-item>
        </a-form>
    </a-modal>


    <!--重置密码表单-->
    <a-modal
            title="重置密码"
            v-model:visible="resetModalVisible"
            :confirm-loading="resetModalLoading"
            @ok="handleResetModalOk"
    >
        <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <!--v-show，是为了让我们在编辑的时候，无法修改密码-->
            <a-form-item label="密码">
                <a-input v-model:value="user.password" />
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script lang="ts">
    import { defineComponent, onMounted, ref } from 'vue';
    import axios from 'axios';
    import { message } from 'ant-design-vue';
    import {Tool} from "@/util/tool";

    declare let hexMd5;
    declare let KEY;


    export default defineComponent({
        name: 'AdminUser',
        setup() {
            const param = ref();
            param.value = {};
            const users = ref();
            const pagination = ref({
                current: 1,
                pageSize: 10,
                total: 0
            });
            const loading = ref(false);

            const columns = [
                {
                    title: '登录名',
                    dataIndex: 'loginName'
                },
                {
                    title: '名称',
                    dataIndex: 'name'
                },
                {
                    title: '密码',
                    dataIndex: 'password'
                },
                {
                    title: 'Action',
                    key: 'action',
                    slots: { customRender: 'action' }
                }
            ];

            /**
             * 数据查询
             **/
            const handleQuery = (params: any) => {
                loading.value = true;
                // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
                users.value = [];
                axios.get("/user/list", {
                    params: {
                        page: params.page,
                        size: params.size,
                        loginName: param.value.loginName
                    }
                }).then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        users.value = data.content.list;

                        // 重置分页按钮
                        pagination.value.current = params.page;
                        pagination.value.total = data.content.total;
                    } else {
                        message.error(data.message);
                    }
                });
            };

            /**
             * 表格点击页码时触发
             */
            const handleTableChange = (pagination: any) => {
                console.log("看看自带的分页参数都有啥：" + pagination);
                handleQuery({
                    page: pagination.current,
                    size: pagination.pageSize
                });
            };

            // -------- 表单 ---------
            const user = ref();
            const modalVisible = ref(false);
            const modalLoading = ref(false);
            const handleModalOk = () => {
                modalLoading.value = true;

                /**
                 * 前端调用 MD5 加密算法
                 * KEY 是盐值，让密码更为复杂
                 */
                user.value.password = hexMd5(user.value.password + KEY);

                axios.post("/user/save", user.value).then((response) => {
                    modalLoading.value = false;
                    const data = response.data; // data = commonResp
                    if (data.success) {
                        modalVisible.value = false;

                        // 重新加载列表
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize,
                        });
                    } else {
                        message.error(data.message);
                    }
                });
            };



            // -------- 重置密码 ---------
            const resetModalVisible = ref(false);
            const resetModalLoading = ref(false);
            const handleResetModalOk = () => {
                modalLoading.value = true;

                user.value.password = hexMd5(user.value.password + KEY);

                axios.post("/user/reset-password", user.value).then((response) => {
                    modalLoading.value = false;
                    const data = response.data; // data = commonResp
                    if (data.success) {
                        resetModalVisible.value = false;

                        // 重新加载列表
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize,
                        });
                    } else {
                        message.error(data.message);
                    }
                });
            };

            /**
             * 重置密码
             */
            const resetPassword = (record: any) => {
                resetModalVisible.value = true;
                user.value = Tool.copy(record);
                user.value.password="";
            };




            /**
             * 编辑
             */
            const edit = (record: any) => {
                modalVisible.value = true;
                user.value = Tool.copy(record);
            };

            /**
             * 新增
             */
            const add = () => {
                modalVisible.value = true;
                user.value = {};
            };

            const handleDelete = (id: any) => {
                axios.delete("/user/delete/" + id).then((response) => {
                    const data = response.data; // data = commonResp
                    if (data.success) {
                        // 重新加载列表
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize,
                        });
                    }
                });
            };

            onMounted(() => {
                handleQuery({
                    page: 1,
                    size: pagination.value.pageSize,
                });
            });

            return {
                param,
                users,
                pagination,
                columns,
                loading,
                handleTableChange,
                handleQuery,

                edit,
                add,
                resetPassword,

                user,
                modalVisible,
                modalLoading,
                handleModalOk,
                resetModalVisible,
                resetModalLoading,
                handleResetModalOk,

                handleDelete
            }
        }
    });
</script>

<style scoped>
    img {
        width: 50px;
        height: 50px;
    }
</style>