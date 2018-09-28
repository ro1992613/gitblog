<template>
    <el-card class="box-card">
        <div slot="header" class="clearfix">
            <router-link :to="{ path: '/menu/list'}"><a><i class="el-icon-back" style="float:left;"></i></a></router-link>
            <span style="font-weight: bolder;"><i class="el-icon-tickets">{{title}}</i></span>
        </div>
        <div class="preview_class" v-html='content'></div>
    </el-card>
</template>
<style>
.preview_class{
    overflow: scroll
}
.preview_class::-webkit-scrollbar {
    display: none;
}
</style>

<script>
export default {
    data() {
        return {
            title:'',
            content:''
        };
    },
    mounted() {
        var id=this.$route.query.id;
        this.$http.post(this.baseUrl+'/api/blog/find',{
            'id':id
        }).then(function(resp){
            console.log(resp.data);
            this.$data.title=resp.data.TITLE;
            this.$data.content=resp.data.CONTENT;
        });
    },
}
</script>
