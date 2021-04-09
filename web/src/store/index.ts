/**
 * Vuex 的作用，就是定义全局响应式变量
 * 可以在整个项目中使用
 * (类似于 spring)
 */
import { createStore } from 'vuex'

declare let SessionStorage: any;
// 自定义全局的 key 值，供 SessionStorage 使用
const USER = "USER";

const store= createStore({
  //定义变量
  state: {
    /**
     * 每次刷新的时候，都会尝试从 SessionStorage 中获取
     * 如果能获取，说明之前存过，那就赋给 vuex 中的全局变量 user
     * 如果获取为空，说明之前没有存过，那就为全局变量 user 进行初始化
     */
    user: SessionStorage.get(USER) || {}
  },
  /**
   * 对变量进行同步操作
   * 外部使用的时候，使用 commit()方法
   */
  mutations: {
    setUser(state,user) {
      state.user=user;
      //USER 是 key，user 是 value
      SessionStorage.set(USER,user);
    }
  },
  //对变量进行异步操作
  actions: {
  },

  modules: {
  }
});

//最后导出 store，可以在外部被使用
export default store;
