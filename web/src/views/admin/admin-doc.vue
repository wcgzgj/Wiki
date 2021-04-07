<template>



    <a-layout>
        <a-layout-content
                :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
        >
            <a-row :gutter="24">
                <a-col :span="8">
                    <p>
                        <a-form layout="inline" :model="param">
                            <a-form-item>
                                <a-button type="primary" @click="handelQuery()">
                                    查询
                                </a-button>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" @click="add()">
                                    新增
                                </a-button>
                            </a-form-item>
                        </a-form>
                    </p>
                    <a-table
                            v-if="level1.length > 0"
                            :columns="columns"
                            :row-key="record => record.id"
                            :data-source="level1"
                            :loading="loading"
                            :pagination="false"
                            size="small"
                            :defaultExpandAllRows="true"
                    >
                        <template #name="{text,record}">
                            {{record.sort}} {{text}}
                        </template>
                        <template v-slot:action="{record}">
                            <a-space size="small">
                                <a-button type="primary" @click="edit(record)" size="small">
                                    编辑
                                </a-button>
                                <a-popconfirm
                                        title="删除后不可恢复，确认删除?"
                                        ok-text="是"
                                        cancel-text="否"
                                        @confirm="handelDelete(record.id)"
                                >
                                    <a-button type="danger" size="small">
                                        删除
                                    </a-button>
                                </a-popconfirm>
                            </a-space>
                        </template>
                    </a-table>
                </a-col>
                <a-col :span="16">
                    <p>
                        <a-form layout="inline" :model="param">
                            <a-form-item>
                                <a-button type="primary" @click="handelSave()">
                                    保存
                                </a-button>
                            </a-form-item>
                        </a-form>
                    </p>
                    <a-form :model="doc" layout="vertical">
                        <a-form-item>
                            <a-input v-model:value="doc.name" placeholder="名称"/>
                        </a-form-item>
                        <a-form-item>
                            <a-tree-select
                                    v-model:value="doc.parent"
                                    style="width: 100%"
                                    :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                                    :tree-data="treeSelectData"
                                    placeholder="请选择父文档"
                                    tree-default-expand-all
                                    :replaceFields="{title: 'name', key: 'id', value: 'id'}"
                            >
                            </a-tree-select>
                        </a-form-item>
                        <a-form-item>
                            <a-input v-model:value="doc.sort" placeholder="顺序"/>
                        </a-form-item>
                        <a-form-item>
                            <div id="content"></div>
                        </a-form-item>
                    </a-form>
                </a-col>
            </a-row>

        </a-layout-content>
    </a-layout>
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

            const docs = ref();

            const loading = ref(false);

            const docState =ref({
                name: ""
            });

            //富文本框对象
            const editor = new E('#content');

            //将富文本框置于图层最底部，保证下拉框会在它上面
            editor.config.zIndex = 0;


            const columns = [
                {
                    title: '名称',
                    dataIndex: 'name',
                    slots: { customRender: 'name' }
                },
                {
                    title: '操作',
                    key: 'action',
                    slots: { customRender: 'action' }
                }
            ];



            const level1=ref();
            /**
             * 因为在 table 组件中，需要使用 level1.value.length
             * 所以，要对 level1 进行初始化，不然会出现空指针的问题
             */
            level1.value=[];

            /**
             * 数据查询
             * params在下面的 onMounted函数中，我们已经定义了
             **/
            const handelQuery = () => {
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

            const doc=ref();
            doc.value={};

            const modalVisible = ref();
            modalVisible.value=false;

            const modalLoading = ref();
            modalLoading.value=false;

            const handelSave = () => {
                console.log("执行保存操作");
                modalLoading.value = true;
                doc.value.content=editor.txt.html();
                console.log(doc.value.content);

                axios.post("/doc/save", doc.value).then((response) => {
                    const data = response.data; // data 其实就是commenResp
                    modalLoading.value = false;
                    if (data.success) { //commenResp里就有一个布尔值 success，判断是不是成功

                        // 保存成功以后，
                        // 去掉模态框 modal
                        // 去掉loading效果
                        modalVisible.value = false;


                        // 重新加载列表
                        // 使用之前定义过的 handelQuery 函数
                        handelQuery();
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
                                handelQuery();
                            }
                        });
                    },
                });

            };


            onMounted(() => {
                //每次新打开这个页面的时候
                //都必须查询所有数据，显示在页面上
                handelQuery();
                editor.create();
            });


            return {
                docs,
                columns,
                loading,
                docState,
                handelQuery,
                level1,

                edit,
                add,
                handelDelete,

                doc,
                modalVisible,
                modalLoading,
                handelSave,

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