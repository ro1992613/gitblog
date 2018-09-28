<template>
<el-container>
  <el-header style="background-color: #0c79b1;padding-top:8px;">
    <span><i class="el-icon-setting"></i> GitBlog</span>
  </el-header>
  <el-container>
    <el-aside style="width:200px;">
        <el-menu router="true">
          <el-menu-item route="/menu/list" index="/menu/list">
              <i class="el-icon-menu"></i>
              <span slot="title">博客列表</span>
          </el-menu-item>
          <el-menu-item route="/menu/addText" index="/menu/addText">
              <i class="el-icon-menu"></i>
              <span slot="title">新增博客</span>
          </el-menu-item>
        </el-menu>
    </el-aside>
    <el-main>
        <router-view></router-view>
    </el-main>
  </el-container>
</el-container>
</template>
<style>
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
