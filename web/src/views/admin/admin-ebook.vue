<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-form
                    layout="inline"
                    :model="ebookState"
                    @finish="handleQuery({page: 1,size: pagination.pageSize})"
            >
                <a-form-item>
                    <a-input v-model:value="ebookState.name" placeholder="书籍名称">
                    </a-input>
                </a-form-item>
                <a-form-item>
                    <a-button
                            type="primary"
                            html-type="submit"
                    >
                        查询
                    </a-button>
                </a-form-item>
                <a-form-item>
                    <a-button type="primary" @click="add()" >
                        新增
                    </a-button>
                </a-form-item>
            </a-form>


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

                <template v-slot:action="{ record }">
                    <a-space size="small">
                        <a-button type="primary" @click="edit(record)">
                            编辑
                        </a-button>


                        <!--气泡确认框-->
                        <a-popconfirm
                                title="删除后不可恢复，确认删除？"
                                ok-text="确认"
                                cancel-text="取消"
                                @confirm="handelDelete(record.id)"
                        >
                            <!--delete是关键字，要用其他单词加以区分-->
                            <a-button type="danger">
                                删除
                            </a-button>

                        </a-popconfirm>


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

            <a-form-item label="分类">
                <a-cascader
                        v-model:value="categoryIds"
                        :field-names="{ label: 'name', value: 'id', children: 'children' }"
                        :options="level1"
                />
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
    // 下面这个组件，是为了方便显示后端传来的校验的
    import {message} from 'ant-design-vue';
    import {Tool} from "@/util/tool";

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

            const ebookState =ref({
                name: ""
            });

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
                    title: '操作',
                    key: 'action',
                    slots: { customRender: 'action' }
                }
            ];


            /**
             * 数据查询
             * params在下面的 onMounted函数中，我们已经定义了
             **/
            const handleQuery = (p) => {
                //加载成功前，显示loading图标
                loading.value = true;
                // axios传参的固定写法
                // 第二个参数要写成 {params: 我们的参数}
                axios.get("/ebook/list", {
                    // 后端查询也要进行分页
                    // 不然如果后端有100w条数据，一次查出来，服务器就挂了
                    params: {
                        page: p.page,
                        size: p.size,
                        name: ebookState.value.name
                    }
                }).then((response) => {
                    loading.value=false;
                    const data = response.data;
                    if (data.success) {
                        const data = response.data;
                        //获取所有电子书信息
                        ebooks.value = data.content.list;

                        // 重置分页按钮
                        pagination.value.current = p.page;
                        pagination.value.total = data.content.total;
                    } else {
                        message.error(data.message);
                    }
                });
            };

            /**
             * 表格点击页码时触发
             */
            const handleTableChange = (pagination) => {
                console.log("看看自带的分页参数都有啥：" + JSON.stringify(pagination));
                handleQuery({
                    page: pagination.current,
                    size: pagination.pageSize
                });
            };


            // -------- 表单 ---------
            /**
             * 数组，[100, 101]对应：前端开发 / Vue
             */
            const categoryIds = ref();
            const ebook=ref({});
            const modalVisible = ref(false);
            const modalLoading = ref(false);
            const handleModalOk = () => {
                modalLoading.value = true;
                ebook.value.category1Id = categoryIds.value[0];
                ebook.value.category2Id = categoryIds.value[1];

                axios.post("/ebook/save", ebook.value).then((response) => {
                    const data = response.data; // data 其实就是commenResp
                    modalLoading.value = false;
                    if (data.success) { //commenResp里就有一个布尔值 success，判断是不是成功

                        // 保存成功以后，
                        // 去掉模态框 modal
                        // 去掉loading效果
                        modalVisible.value = false;


                        // 重新加载列表
                        // 使用之前定义过的 handleQuery 函数
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize
                        });
                    } else {
                        message.error(data.message);
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

                //编辑时复制对象，这样前端页面就不会在还没确认的情况下，响应式变化了
                ebook.value=Tool.copy(record);

                // 我发现，使用雪花算法后，前端传入的id值，会出现精度偏差
                //所以，需要加入一个jackson配置，具体配置，我写在文档里了
                // console.log("传入的一整行的值为："+JSON.stringify(record));
                categoryIds.value = [ebook.value.category1Id, ebook.value.category2Id]
            };


            /**
             * 新增
             */
            const add = () => {
                modalVisible.value = true;
                //因为在新增的时候,我们不希望表单中有初始数据
                ebook.value={};
            };

            /**
             * 删除
             * @param id 传进来的，要用来判断删除的id
             */
            const handelDelete=(id) => {
                axios.delete("/ebook/delete/" + id).then((response) => {
                    const data = response.data;
                    if (data.success) {
                        //如果删除成功，要重新查询后端数据
                        //从而能显示出是删除了一个数据
                        handleQuery({
                            page: pagination.value.current,
                            size: pagination.value.pageSize
                        });
                    }
                });
            };


            const level1 =  ref();
            /**
             * 查询所有分类
             **/
            const handleQueryCategory = () => {
                loading.value = true;
                axios.get("/category/all").then((response) => {
                    loading.value = false;
                    const data = response.data;
                    if (data.success) {
                        const categorys = data.content;
                        console.log("原始数组：", categorys);

                        level1.value = [];
                        level1.value = Tool.array2Tree(categorys, 0);
                        console.log("树形结构：", level1.value);
                    } else {
                        message.error(data.message);
                    }
                });
            };

            onMounted(() => {
                handleQueryCategory();
                //每次新打开这个页面的时候
                //都必须查询所有数据，显示在页面上
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
                ebookState,
                handleQuery,

                edit,
                add,
                handelDelete,

                ebook,
                modalVisible,
                modalLoading,
                handleModalOk,
                categoryIds,
                level1
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