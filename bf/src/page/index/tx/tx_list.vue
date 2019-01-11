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
      <el-table-column align="center" prop="type" label="类型"></el-table-column>
      <el-table-column align="center" prop="category" label="目录"></el-table-column>
      <el-table-column align="center" prop="title" label="标题"></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button @click="deleteCategory(scope.row)" type="text" size="small">删除</el-button>
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
      inputdata: {
        type: "",
        category: ""
      }
    };
  },
  methods: {
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
    getList() {
      var this_v = this;
      var params = {
        Bucket: Bucket /* 必须 */,
        Region: Region /* 必须 */
      };
      cos.getBucket(params, function(err, data) {
        let rs = [];
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
            if (key.indexOf("public/") >= 0) {
              key = key.replace("public/", "");
              el.type = "公共";
              if (key.indexOf("/") >= 0) {
                el.category = key.substring(0, key.indexOf("/"));
                el.title=key.substring(key.indexOf("/")+1,key.length);
                rs.push(el);
              }
            } else if (key.indexOf("private/") >= 0) {
              key = key.replace("private/", "");
              el.type = "私有";
              if (key.indexOf("/") >= 0) {
                el.category = key.substring(0, key.indexOf("/"));
                el.title=key.substring(key.indexOf("/")+1,key.length);
                rs.push(el);
              }
            }
          });
        }
        this_v.$data.category_list = rs;
      });
    }
  },
  computed: {},
  mounted() {
    this.getList();
  }
};
</script>
