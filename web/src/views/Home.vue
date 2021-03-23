<template>

  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
              mode="inline"
              :default-selected-keys="['1']"
              :default-open-keys="['sub1']"
              :style="{ height: '100%', borderRight: 0 }"
      >
        <a-sub-menu key="sub1">
          <span slot="title">subnav 1</span>
          <a-menu-item key="1">
            option1
          </a-menu-item>
          <a-menu-item key="2">
            option2
          </a-menu-item>
          <a-menu-item key="3">
            option3
          </a-menu-item>
          <a-menu-item key="4">
            option4
          </a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub2">
          <span slot="title">subnav 2</span>
          <a-menu-item key="5">
            option5
          </a-menu-item>
          <a-menu-item key="6">
            option6
          </a-menu-item>
          <a-menu-item key="7">
            option7
          </a-menu-item>
          <a-menu-item key="8">
            option8
          </a-menu-item>
        </a-sub-menu>
        <a-sub-menu key="sub3">
          <span slot="title">subnav 3</span>
          <a-menu-item key="9">
            option9
          </a-menu-item>
          <a-menu-item key="10">
            option10
          </a-menu-item>
          <a-menu-item key="11">
            option11
          </a-menu-item>
          <a-menu-item key="12">
            option12
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >

      <a-list item-layout="vertical" size="large" :pagination="pagination" :data-source="listData">
        <div slot="footer"><b>ant design vue</b> footer part</div>
        <a-list-item slot="renderItem" key="item.title" slot-scope="item, index">
<!--          <template v-for="{ type, text } in actions" slot="actions">-->
<!--            <span :key="type">-->
<!--              <a-icon :type="type" style="margin-right: 8px" />-->
<!--              {{ text }}-->
<!--            </span>-->
<!--          </template>-->
          <img
                  slot="extra"
                  width="272"
                  alt="logo"
                  src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png"
          />
          <a-list-item-meta :description="item.description">
            <a slot="title" :href="item.href">{{ item.title }}</a>
            <a-avatar slot="avatar" :src="item.avatar" />
          </a-list-item-meta>
          {{ item.content }}
        </a-list-item>
      </a-list>


    </a-layout-content>
  </a-layout>

</template>


<script lang="ts">
  import { defineComponent ,onMounted , ref} from 'vue';
  import axios from 'axios'


  const listData: any = [];
  for (let i = 0; i < 23; i++) {
    listData.push({
      href: 'https://www.antdv.com/',
      title: `ant design vue part ${i}`,
      avatar: 'https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png',
      description:
              'Ant Design, a design language for background applications, is refined by Ant UED Team.',
      content:
              'We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.',
    });
  }

export default defineComponent({
  name: 'Home',
  components: {
  },
  setup() {
    console.log("setup")
    //ref()是一个响应式的数据
    //还有一个方法，是使用reactive，但是太麻烦了，我不想用
    const ebooks=ref();

    onMounted(()=>{
      console.log("onMounted");
      axios.get("http://localhost:8880/ebook/list?name=Spring").then(resp=>{
        console.log(resp)
        const data=resp.data;
        ebooks.value=data;
      })
    })


    return {
      //最后要把响应式数据ebooks返回，前端才可以拿到
      ebooks,
      listData,
      pagination: {
        onChange: (page: any) => {
          console.log(page);
        },
        pageSize: 3,
      },
      actions: [
        { type: 'star-o', text: '156' },
        { type: 'like-o', text: '156' },
        { type: 'message', text: '2' },
      ],
    }
  }


});
</script>
