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
      path:'preview',
      name:'preview',
      component: (resolve) => require(['../page/index/views/preview.vue'], resolve)
    },
    {
      path:'wang',
      name:'wang',
      component: (resolve) => require(['../page/index/views/wang.vue'], resolve)
    }
  ]
}

export default new Router({
  routes: [
    index,
    menu
  ]
})
