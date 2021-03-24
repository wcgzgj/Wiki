<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-table
                    :columns="columns"
                    :row-key="record => record.id"
                    :data-source="ebooks"
                    :pagination="pagination"
                    :loading="loading"
                    @change="handleTableChange"
            >
                <template #cover="{ text: cover }">
                    <img v-if="cover" :src="cover" alt="avatar" />
                </template>
                <template v-slot:action="{record}">
                    <a-space size="small">
                        <a-button type="primary" @click="edit(record)">
                            编辑
                        </a-button>
                        <a-button type="danger">
                            删除
                        </a-button>
                    </a-space>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>

    <a-modal
            title="电子书表单"
            v-model:visible="modalVisible"
            :confirm-loading="modalLoading"
            @ok="handleModalOk"
    >
        <a-form :model="ebook" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <a-form-item label="封面">
                <a-input v-model:value="ebook.cover" />
            </a-form-item>
            <a-form-item label="名称">
                <a-input v-model:value="ebook.name" />
            </a-form-item>
            <a-form-item label="分类一">
                <a-input v-model:value="ebook.category1Id" />
            </a-form-item>
            <a-form-item label="分类二">
                <a-input v-model:value="ebook.category2Id" />
            </a-form-item>
            <a-form-item label="描述">
                <a-input v-model:value="ebook.desc" type="textarea" />
            </a-form-item>
        </a-form>
    </a-modal>

</template>

<script>
    import {defineComponent, onMounted, ref} from 'vue';
    import axios from "axios";

    export default defineComponent({
        name: 'AdminEbook',
        setup() {
            const ebooks = ref();

            // 设定分页参数
            // 固定是每次从第一页开始，每页查四行
            const pagination = ref({
                current: 1,
                pageSize: 4,
                total: 0
            });
            const loading = ref(false);

            const columns = [
                {
                    title: '封面',
                    dataIndex: 'cover',
                    slots: { customRender: 'cover' }
                },
                {
                    title: '名称',
                    dataIndex: 'name'
                },
                {
                    title: '分类一',
                    key: 'category1Id',
                    dataIndex: 'category1Id'
                },
                {
                    title: '分类二',
                    dataIndex: 'category2Id'
                },
                {
                    title: '文档数',
                    dataIndex: 'docCount'
                },
                {
                    title: '阅读数',
                    dataIndex: 'viewCount'
                },
                {
                    title: '点赞数',
                    dataIndex: 'voteCount'
                },
                {
                    title: 'Action',
                    key: 'action',
                    slots: { customRender: 'action' }
                }
            ];

            /**
             * 数据查询
             * params在下面的 onMounted函数中，我们已经定义了
             **/
            const handleQuery = (p) => {
                loading.value = true;
                // axios传参的固定写法
                // 第二个参数要写成 {params: 我们的参数}
                axios.get("/ebook/list", {
                    params: {
                        page: p.page,
                        size: p.size
                    }
                }).then((response) => {
                    loading.value = false;
                    const data = response.data;
                    // 因为content还封装了一个 PageResp
                    // 所以要获取列表信息，还要再往下访问一层
                    ebooks.value = data.content.list;

                    // 重置分页按钮
                    pagination.value.current = p.page;
                    pagination.value.total = data.content.total;
                });
            };

            /**
             * 表格点击页码时触发
             */
            const handleTableChange = (pagination) => {
                console.log("看看自带的分页参数都有啥：" + pagination);
                handleQuery({
                    page: pagination.current,
                    size: pagination.pageSize
                });
            };

            // -------- 表单 ---------
            const ebook=ref({});
            const modalVisible = ref(false);
            const modalLoading = ref(false);
            const handleModalOk = () => {
                modalLoading.value = true;
                setTimeout(() => {
                    modalVisible.value = false;
                    modalLoading.value = false;
                }, 2000);
            };

            /**
             * 编辑
             * 这里的const xxx=()=>{}
             * 其实就是一个函数
             * 函数名为 xxx
             * 实现为()=>{}
             * ()里面可以放参数
             */
            const edit = (record) => {
                modalVisible.value = true;
                ebook.value=record;
            };


            onMounted(() => {
                handleQuery({
                    page: 1,
                    size: pagination.value.pageSize
                });
            });

            return {
                ebooks,
                pagination,
                columns,
                loading,
                handleTableChange,

                edit,

                ebook,
                modalVisible,
                modalLoading,
                handleModalOk
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