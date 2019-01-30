<template>
<el-container>
  <el-header style="background-color: #0c79b1;padding-top:8px;">
    <span><i class="el-icon-setting"></i> GitBlog</span>
  </el-header>
  <el-container>
    <el-aside style="width:200px;">
        <el-menu router="true">
          <el-menu-item route="/view/all" index="/view/all">
              <i class="el-icon-menu"></i>
              <span slot="title">新的首页</span>
          </el-menu-item>
          <el-submenu index="/menu/list">
            <template slot="title">
              <i class="el-icon-menu"></i>旧的博客（需后台）
            </template>
          <el-menu-item route="/menu/list" index="/menu/list">
              <i class="el-icon-menu"></i>
              <span slot="title">博客列表</span>
          </el-menu-item>
          <el-menu-item route="/menu/collection" index="/menu/collection">
              <i class="el-icon-menu"></i>
              <span slot="title">合集管理</span>
          </el-menu-item>
          <el-menu-item route="/menu/addText" index="/menu/addText">
              <i class="el-icon-menu"></i>
              <span slot="title">新增博客</span>
          </el-menu-item>

          </el-submenu>
          <el-submenu index="/menu/tx_list">
            <template slot="title">
              <i class="el-icon-menu"></i>腾讯云后台
            </template>
            <el-menu-item route="/menu/tx_list" index="/menu/tx_list">
              <i class="el-icon-menu"></i>
              <span slot="title">列表</span>
          </el-menu-item>
            <el-menu-item route="/menu/tx_category" index="/menu/tx_category">
              <i class="el-icon-menu"></i>
              <span slot="title">目录</span>
          </el-menu-item>
          <el-menu-item route="/menu/tx_editor" index="/menu/tx_editor">
              <i class="el-icon-menu"></i>
              <span slot="title">编辑器</span>
          </el-menu-item>
          
          </el-submenu>
        </el-menu>
    </el-aside>
    <el-main>
        <router-view></router-view>
    </el-main>
  </el-container>
</el-container>
</template>
<style scoped>
.el-header {
  background-color: #b3c0d1;
  color: #333;
  text-align: left;
  font-size: 35px;
  font-weight: bolder;
}

.el-aside {
  color: #333;
  text-align: left;
  width: 100px;
}

.el-main {
  background-color: #e9eef3;
  color: #333;
  text-align: center;
  height: 100%;
}

.el-container {
  height: 100%;
  width: 100%;
}
</style>
