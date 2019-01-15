<template>
    <el-container>
        <el-header style="padding:0">
            <el-menu  router="true" style="magine:0" mode="horizontal">
                <el-menu-item route="/" index="/">
                    <span slot="title">返回</span>
                </el-menu-item>
                <el-menu-item  route="/view/all" index="/view/all">全部</el-menu-item>
                <el-menu-item  route="/view/public" index="/view/public">公开</el-menu-item>
            </el-menu>
        </el-header>
        <el-main>
            <router-view></router-view>
        </el-main>
    </el-container>
</template>
<style scoped>

</style>
<script>
export default {
    
}
</script>
