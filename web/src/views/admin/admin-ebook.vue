<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <p>
                <a-button type="primary" @click="add()" size="large">
                    新增
                </a-button>
            </p>
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
                <template v-slot:action="{ text, record }">
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
                <a-input v-model:value="ebook.description" type="textarea" />
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
                axios.post("/ebook/save", ebook.value).then((response) => {
                    const data = response.data; // data 其实就是commenResp
                    if (data.success) { //commenResp里就有一个布尔值 success，判断是不是成功

                        // 保存成功以后，
                        // 去掉模态框 modal
                        // 去掉loading效果
                        modalVisible.value = false;
                        modalLoading.value = false;

                        // 重新加载列表
                        // 使用之前定义过的 handleQuery 函数
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize
                        });
                    }
                });
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
                //因为整个页面中隐藏了一个表单
                //我们点击edit以后，就会把这个隐藏的属性，设置为可见
                //这样，就可以出现一个供我们编辑的表单了
                //而且，表单中的数据，也可以通过 ebook 来展示
                //这个 ebook 进行了双向绑定
                modalVisible.value = true;
                ebook.value=record;

                // 我发现，使用雪花算法后，前端传入的id值，会出现精度偏差
                console.log("传入的一整行的值为："+JSON.stringify(record));
            };


            /**
             * 新增
             */
            const add = () => {
                modalVisible.value = true;
                //因为在新增的时候,我们不希望表单中有初始数据
                ebook.value={};
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
                add,

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