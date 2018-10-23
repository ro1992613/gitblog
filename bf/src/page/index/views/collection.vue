<template>
    <el-card class="box-card">
      <div slot="header" style="text-align:left" class="clearfix">
        <span>博客列表</span>
        <el-button @click="createClick()" style="float: right; padding: 3px 0" icon="el-icon-edit" type="primary">新增</el-button>
    </div>
      <el-table :data="tableData" stripe style="width: 100%;" >
        <el-table-column align="center" prop="COLLECTION_ID" label="id">
        </el-table-column>
        <el-table-column align="center" prop="COLLECTION_NAME" label="名称">
        </el-table-column>
        <el-table-column align="center"  label="操作" width="100">
          <template slot-scope="scope">
            <el-button @click="deleteClick(scope.row)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
</template>
<style>

</style>

<script>
export default {
  data() {
    return {
      tableData:[]
    };
  },
  mounted() {
    this.$http.post(this.baseUrl+'/api/blog/collection_list',{}).then(function(resp){
      this.$data.tableData=resp.data;
    });
  },
  methods:{
    deleteClick(row) {
      this.$http.post(this.baseUrl+'/api/blog/delete_collection',{collection_id:row.COLLECTION_ID}).then(function(resp){
        this.$data.tableData=resp.data;
      });
    },
    createClick(){
        var this_v=this;
        this_v.$prompt('请输入类型').then(({ value }) => {
            this_v.$http.post(this_v.baseUrl+'/api/blog/add_collection',{collection_name:value}).then(function(resp){
                this_v.$http.post(this_v.baseUrl+'/api/blog/collection_list',{}).then(function(resp){
                    this_v.$data.tableData=resp.data;
                });
                this_v.$message({
                    type: 'success',
                    message: '成功'
                });
            });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消输入'
          });       
        });
    }
  }
};
</script>
