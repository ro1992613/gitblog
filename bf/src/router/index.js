import Vue from 'vue'
import Router from 'vue-router'
import MainContainer from '../page/index/index.vue';
import base from '../base/base';
import  VueResource  from 'vue-resource'

Vue.use(VueResource) 
Vue.use(Router);
Vue.use(base);

const index={
  path: '/',
  name: 'index',
  component: MainContainer
};

const menu={
  path: '/menu',
  name: 'menu',
  component:MainContainer,
  children:[
    {
      path:'addText',
      name:'addText',
      component: (resolve) => require(['../page/index/views/addText.vue'], resolve)
    },
    {
      path:'list',
      name:'list',
      component: (resolve) => require(['../page/index/views/list.vue'], resolve)
    },
    {
      path:'collection',
      name:'collection',
      component: (resolve) => require(['../page/index/views/collection.vue'], resolve)
    },
    {
      path:'preview',
      name:'preview',
      component: (resolve) => require(['../page/index/views/preview.vue'], resolve)
    },
    {
      path:'wang',
      name:'wang',
      component: (resolve) => require(['../page/index/views/wang.vue'], resolve)
    },
    {
      path:'tx_category',
      name:'tx_category',
      component: (resolve) => require(['../page/index/tx/tx_category.vue'], resolve)
    },
    {
      path:'tx_list',
      name:'tx_list',
      component: (resolve) => require(['../page/index/tx/tx_list.vue'], resolve)
    },
    {
      path:'tx_editor',
      name:'tx_editor',
      component: (resolve) => require(['../page/index/tx/tx_editor.vue'], resolve)
    }
  ]
}

export default new Router({
  routes: [
    index,
    menu
  ]
})
