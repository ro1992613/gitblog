<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>文章列表</span>
      <el-button
        @click="addText"
        style="float: right; padding: 3px 0"
        type="text"
      >新增</el-button>
    </div>
    <el-table :data="category_list" style="width: 100%">
      <el-table-column type="index" :index="indexMethod" label="序号" width="50"></el-table-column>
      <el-table-column align="center" prop="typeName" label="类型" sortable></el-table-column>
      <el-table-column align="center" prop="category" label="目录" :filters="category_filters" :filter-method="filterHandler" sortable></el-table-column>
      <el-table-column align="center" prop="title" label="标题" sortable></el-table-column>
      <el-table-column align="center" prop="date" label="日期" sortable></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button @click="deleteCategory(scope.row)" type="text" size="small">删除</el-button>
          <el-button @click="editText(scope.row)" type="text" size="small">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>
<script>
import "./cos-js-sdk-v5.min.js";
var Bucket = "cyp20190111-1251525641";
var Region = "ap-chengdu";
var cos = new COS({
  SecretId: "AKID2qOioeydrqa5LMkEn6AzYsNddMtBsfXB",
  SecretKey: "Z6OVO1PYT9cqsxqUu6FApHbfU4PVheLo"
});
export default {
  data() {
    return {
      category_list: [],
      category_filters:[],
      inputdata: {
        type: "",
        category: ""
      }
    };
  },
  methods: {
    initUser(){
      var user = window.localStorage.getItem("user");
      if(user && user == this.sysuser){
        return;
      }else {
        var tempuser=window.prompt("提示","输入密码吧！");
        if(tempuser==this.sysuser){
          window.localStorage.setItem("user",tempuser);
          return;
        }else{
          this.$router.push({path:"/"});
        }
      }
    },
    deleteCategory(row) {
      var thisv = this;
      cos.deleteObject(
        {
          Bucket: Bucket /* 必须 */,
          Region: Region /* 必须 */,
          Key: row.key /* 必须 */
        },
        function(err, data) {
          if (err) {
            thisv.$message(err);
          } else {
            thisv.$message("删除成功");
            thisv.getList();
          }
        }
      );
    },
    addText() {
      this.$router.push({path:"/menu/tx_editor"});
    },
    editText(row){
      this.$router.push({path:"/menu/tx_editor", query: { key: row.key,category:row.category,type:row.type,title:row.title }});
    },
    getList() {
      var this_v = this;
      var params = {
        Bucket: Bucket /* 必须 */,
        Region: Region /* 必须 */
      };
      cos.getBucket(params, function(err, data) {
        let rs = [];
        let filters = [];
        let set=new Set();
        if (err) {
          console.log(err);
        } else {
          data.Contents.map(v => {
            let key = v.Key;
            if(key.endsWith("/")){
                key="";
            }
            let el = {};
            el.key = key;
            el.size = v.Size;
            el.tag = v.ETag;
            el.date = v.LastModified;
            el.index = new Date(v.LastModified).getTime()/1000;
            if (key.indexOf("public/") >= 0) {
              key = key.replace("public/", "");
              el.type = "public";
              el.typeName = "公共";
              if (key.indexOf("/") >= 0) {
                el.category = key.substring(0, key.indexOf("/"));
                el.title=key.substring(key.indexOf("/")+1,key.length);
                rs.push(el);
              }
            } else if (key.indexOf("private/") >= 0) {
              key = key.replace("private/", "");
              el.type = "private";
              el.typeName = "私有";
              if (key.indexOf("/") >= 0) {
                el.category = key.substring(0, key.indexOf("/"));
                el.title=key.substring(key.indexOf("/")+1,key.length);
                rs.push(el);
              }
            }
            if(el.category && !set.has(el.category)){
              set.add(el.category)
              filters.push({'text':el.category,'value':el.category})
            }
          });
        }
        rs.sort((a,b)=>{
          return b.index-a.index;
        });
        this_v.$data.category_filters= filters;
        this_v.$data.category_list = rs;
      });
    },
    filterHandler(value, row, column) {
      const property = column['property'];
      return row[property] === value;
    }
  },
  computed: {},
  mounted() {
    this.initUser();
    this.getList();
  }
};
</script>
