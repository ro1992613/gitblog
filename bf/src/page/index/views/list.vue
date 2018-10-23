<template>
    <el-card class="box-card">
      <div slot="header" style="text-align:left" class="clearfix">
        <span>博客列表</span>
        <el-button @click="createClick()" style="float: right; padding: 3px 0" icon="el-icon-edit" type="danger">开始生成</el-button>
    </div>
      <el-table :data="tableData" stripe style="width: 100%;" >
        <el-table-column align="center" prop="ID" label="id">
        </el-table-column>
        <el-table-column align="center"  prop="COLLECTION_NAME" label="合集">
        </el-table-column>
        <el-table-column align="center"  prop="CREATE_TIME" label="日期">
        </el-table-column>
        <el-table-column align="center"  prop="TITLE" label="标题">
        </el-table-column>
        <el-table-column align="center"  label="操作" width="100">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
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
    this.$http.post(this.baseUrl+'/api/blog/list',{}).then(function(resp){
      this.$data.tableData=resp.data;
    });
  },
  methods:{
    handleClick(row) {
      this.$router.push({path:"/menu/preview",query:{id:row.ID}});
    },
    deleteClick(row) {
      this.$http.post(this.baseUrl+'/api/blog/delete',{id:row.ID}).then(function(resp){
        this.$data.tableData=resp.data;
      });
    },
    createClick(){
      this.$http.post(this.baseUrl+'/api/blog/create',{}).then(function(resp){
        this.$message('完成生成工作');
      });
    }
  }
};
</script>
