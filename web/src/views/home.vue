<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
              mode="inline"
              :style="{ height: '100%', borderRight: 0 }"
              @click="handleClick"
      >
        <a-menu-item key="welcome">
          <MailOutlined />
          <span>欢迎</span>
        </a-menu-item>

        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span><user-outlined />{{item.name}}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined /><span>{{child.name}}</span>
          </a-menu-item>
        </a-sub-menu>



      </a-menu>
    </a-layout-sider>


    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎使用FARO_Z知识库</h1>
      </div>

      <a-list v-show="!isShowWelcome" item-layout="vertical" size="large" :data-source="ebooks" :grid="{ gutter: 20, column: 3 }">
        <!--下面会进行循环，每次都会取出ebooks中的一个数据 item-->
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ type, text } in actions" :key="type">
                <component v-bind:is="type" style="margin-right: 8px" />
                {{ text }}
              </span>
            </template>
            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar><a-avatar :src="item.cover" /></template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>


    </a-layout-content>
  </a-layout>
</template>



<script lang="ts">
  import {defineComponent, onMounted, ref} from 'vue';
  import axios from "axios";
  import { message } from 'ant-design-vue';
  import {Tool} from "@/util/tool";


  export default defineComponent({
  name: 'Home',
  components: {
    // StarOutlined,
    // LikeOutlined,
    // MessageOutlined,
  },


  setup() {
    const pagination = {
      onChange: (page: number) => {
        console.log(page);
      },
      pageSize: 3,
    };
    const actions: Record<string, string>[] = [
      { type: 'StarOutlined', text: '156' },
      { type: 'LikeOutlined', text: '156' },
      { type: 'MessageOutlined', text: '2' },
    ];

    const ebooks=ref();

    const level1 =  ref();
    let categorys;

    /**
     * 查询所有分类
     **/
    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (data.success) {
          categorys = data.content;
          console.log("原始数组：", categorys);

          level1.value = [];
          level1.value = Tool.array2Tree(categorys, 0);
          console.log("树形结构：", level1.value);
        } else {
          message.error(data.message);
        }
      });
    };


    const isShowWelcome = ref(true);
    let categoryId2 =0;






    const handleClick = (value: any) => {
      console.log("menu click", value)
      if (value.key === 'welcome') {
        isShowWelcome.value = true;
      } else {
        categoryId2=value.key;
        isShowWelcome.value = false;
        handelQueryEbook();
      }
      // isShowWelcome.value = value.key === 'welcome';
    };

    const handelQueryEbook = () => {
      axios.get("/ebook/list",{
        params: {
          page:1,
          //因为我们知道，电子书的个数不会超过1000，所以这里我们写死了
          size: 1000,
          categoryId2: categoryId2
        }
      }).then(resp=>{
        const data=resp.data;
        ebooks.value=data.content.list;
      })
    };

    onMounted(()=>{
      handleQueryCategory();
    });

    return {
      ebooks,
      pagination,
      actions,
      handleClick,
      level1,
      isShowWelcome
    }
  }
});
</script>


<style scoped>
  /*这个class的获取，是在页面上，通过元素查找找到的，*/
  /*在代码里面，antd把css都封装了，我们都看不到了*/
  .ant-avatar {
    width: 50px;
    height: 50px;
    line-height: 50px;
    border-radius: 8%;
    margin: 5px 0;
  }
</style>
