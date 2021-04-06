<template>
    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-form
                    layout="inline"
                    :model="docState"
                    @finish="handleQuery({page: 1,size: pagination.pageSize})"
            >
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
                    :data-source="level1"
                    :pagination="false"
                    :loading="loading"
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
            title="文档表单"
            v-model:visible="modalVisible"
            :confirm-loading="modalLoading"
            @ok="handleModalOk"
    >
        <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
            <a-form-item label="名称">
                <a-input v-model:value="doc.name" />
            </a-form-item>
            <a-form-item label="父文档">
                <a-tree-select
                        v-model:value="doc.parent"
                        style="width: 100%"
                        :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                        :tree-data="treeSelectData"
                        placeholder="请选择父文档"
                        tree-default-expand-all
                        :replaceFields="{title: 'name',key: 'id',value: 'id'}"
                >
                </a-tree-select>
            </a-form-item>

            <a-form-item label="顺序">
                <a-input v-model:value="doc.sort" />
            </a-form-item>
            <a-form-item label="内容">
                <div id="content"></div>
            </a-form-item>
        </a-form>
    </a-modal>
</template>

<script>
    import {defineComponent, onMounted, ref,createVNode} from 'vue';
    import axios from "axios";
    // 下面这个组件，是为了方便显示后端传来的校验的
    import {message,Modal} from 'ant-design-vue';
    import {Tool} from "@/util/tool";
    import {useRoute} from "vue-router";
    import ExclamationCircleOutlined from "@ant-design/icons-vue/ExclamationCircleOutlined";
    import E from 'wangeditor';

    export default defineComponent({
        name: 'AdminDoc',
        setup() {
            const route=useRoute();
            // console.log("路由：", route);
            // console.log("route.path：", route.path);
            // console.log("route.query：", route.query);
            // console.log("route.param：", route.params);
            // console.log("route.fullPath：", route.fullPath);
            // console.log("route.name：", route.name);
            // console.log("route.meta：", route.meta);

            const docs = ref();

            const loading = ref(false);

            const docState =ref({
                name: ""
            });

            //富文本框对象
            const editor = new E('#content');


            const columns = [
                {
                    title: '名称',
                    dataIndex: 'name'
                },
                {
                    title: '父文档',
                    key: 'parent',
                    dataIndex: 'parent'
                },
                {
                    title: '顺序',
                    key: 'sort',
                    dataIndex: 'sort'
                },
                {
                    title: '操作',
                    key: 'action',
                    slots: { customRender: 'action' }
                }
            ];



            const level1=ref();

            /**
             * 数据查询
             * params在下面的 onMounted函数中，我们已经定义了
             **/
            const handleQuery = () => {
                loading.value = true;
                // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
                level1.value = [];

                axios.get("/doc/all").then((response) => {
                    loading.value=false;
                    const data = response.data;
                    if (data.success) {

                        docs.value = data.content;
                        level1.value=[];
                        level1.value=Tool.array2Tree(docs.value,0);

                    } else {
                        message.error(data.message);
                    }
                });
            };



            // -------- 表单 ---------
            // 因为树选择组件的属性状态，会随当前编辑的节点而变化，所以单独声明一个响应式变量
            const treeSelectData = ref();
            const doc=ref({});
            const modalVisible = ref(false);
            const modalLoading = ref(false);
            const handleModalOk = () => {
                modalLoading.value = true;
                axios.post("/doc/save", doc.value).then((response) => {
                    const data = response.data; // data 其实就是commenResp
                    modalLoading.value = false;
                    if (data.success) { //commenResp里就有一个布尔值 success，判断是不是成功

                        // 保存成功以后，
                        // 去掉模态框 modal
                        // 去掉loading效果
                        modalVisible.value = false;


                        // 重新加载列表
                        // 使用之前定义过的 handleQuery 函数
                        handleQuery();
                    } else {
                        message.error(data.message);
                    }
                });
            };


            /**
             * 将某节点及其子孙节点全部置为disabled
             */
            const setDisable = (treeSelectData, id) => {
                // console.log(treeSelectData, id);
                // 遍历数组，即遍历某一层节点
                for (let i = 0; i < treeSelectData.length; i++) {
                    const node = treeSelectData[i];
                    if (node.id === id) {
                        // 如果当前节点就是目标节点
                        console.log("disabled", node);
                        // 将目标节点设置为disabled
                        node.disabled = true;

                        // 遍历所有子节点，将所有子节点全部都加上disabled
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            for (let j = 0; j < children.length; j++) {
                                setDisable(children, children[j].id)
                            }
                        }
                    } else {
                        // 如果当前节点不是目标节点，则到其子节点再找找看。
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            setDisable(children, id);
                        }
                    }
                }
            };


            // let ids=[];
            const deleteIds= [];
            const deleteNames= [];

            /**
             * 查找整根树枝
             */
            const getDeleteIds = (treeSelectData, id) => {
                // console.log(treeSelectData, id);
                // 遍历数组，即遍历某一层节点
                for (let i = 0; i < treeSelectData.length; i++) {
                    const node = treeSelectData[i];
                    if (node.id === id) {

                        // ids.push(id);
                        deleteIds.push(id);
                        deleteNames.push(node.name);

                        // 遍历所有子节点
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            for (let j = 0; j < children.length; j++) {
                                getDeleteIds(children, children[j].id)
                            }
                        }
                    } else {
                        // 如果当前节点不是目标节点，则到其子节点再找找看。
                        const children = node.children;
                        if (Tool.isNotEmpty(children)) {
                            getDeleteIds(children, id);
                        }
                    }
                }
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
                doc.value=Tool.copy(record);

                // 不能选择当前节点及其所有子孙节点，作为父节点，会使树断开
                treeSelectData.value = Tool.copy(level1.value);
                setDisable(treeSelectData.value, record.id);

                // 为选择树添加一个"无"
                treeSelectData.value.unshift({id: 0, name: '无'});

                setTimeout(function () {
                    editor.create();
                }, 100);

            };


            /**
             * 新增
             */
            const add = () => {
                modalVisible.value = true;
                //因为在新增的时候,我们不希望表单中有初始数据
                doc.value={
                    //获取 url 中传入的 ebookId 信息
                    ebookId: route.query.ebookId
                };

                treeSelectData.value = Tool.copy(level1.value);

                // 为选择树添加一个"无"
                // unshift 方法，是在前面添加（和 push 是反过来的）
                treeSelectData.value.unshift({id: 0, name: '无'});

                setTimeout(function () {
                    editor.create();
                }, 100);
            };

            /**
             * 删除
             * @param id 传进来的，要用来判断删除的id
             */
            const handelDelete=(id) => {
                /**
                 * 定义了一个全局变量 ids
                 * 当执行 getDeleteIds()后，就会把获得的数值，放入 ids 中
                 * 所以，下面会直接使用一个 ids 变量
                 */
                getDeleteIds(level1.value,id);
                Modal.confirm({
                    title: '重要提醒',
                    icon: createVNode(ExclamationCircleOutlined),
                    content: '将删除：【' + deleteNames.join("，") + "】删除后不可恢复，确认删除？",
                    onOk() {
                        // console.log(ids)
                        axios.delete("/doc/delete/" + deleteIds.join(",")).then((response) => {
                            const data = response.data;  // data = commonResp
                            if (data.success) {
                                //如果删除成功，要重新查询后端数据
                                //从而能显示出是删除了一个数据
                                handleQuery();
                            }
                        });
                    },
                });

            };


            onMounted(() => {
                //每次新打开这个页面的时候
                //都必须查询所有数据，显示在页面上
                handleQuery();
            });


            return {
                docs,
                columns,
                loading,
                docState,
                handleQuery,
                level1,

                edit,
                add,
                handelDelete,

                doc,
                modalVisible,
                modalLoading,
                handleModalOk,

                treeSelectData
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