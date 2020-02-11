<template>
    <el-container>
        <el-header style="padding:0;">
            <el-menu  router="true" style="magine:0;background: #f8f8f8;" mode="horizontal">
                <el-menu-item  route="/view/all" index="/view/all" style="background: #f8f8f8;">
                    <span style="font-size:20px;">GITBLOG</span>
                </el-menu-item>
                <el-menu-item route="/" index="/" style="background: #f8f8f8;">
                    <span style="font-size:20px;">后台</span>
                </el-menu-item>
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
