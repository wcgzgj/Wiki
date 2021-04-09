/**
 * Vuex 的作用，就是定义全局响应式变量
 * 可以在整个项目中使用
 * (类似于 spring)
 */
import { createStore } from 'vuex'

const store= createStore({
  //定义变量
  state: {
    user: {}
  },
  /**
   * 对变量进行同步操作
   * 外部使用的时候，使用 commit()方法
   */
  mutations: {
    setUser(state,user) {
      state.user=user;
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
