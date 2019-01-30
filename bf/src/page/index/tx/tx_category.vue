<template>
  <el-card class="box-card">
    <div slot="header" class="clearfix">
      <span>目录</span>
      <el-button
        @click="dialogFormVisible = true"
        style="float: right; padding: 3px 0"
        type="text"
      >新增</el-button>
    </div>
    <el-table :data="category_list" style="width: 100%">
      <el-table-column type="index" :index="indexMethod" label="序号" width="50"></el-table-column>
      <el-table-column align="center" prop="type" label="类型"></el-table-column>
      <el-table-column align="center" prop="category" label="目录"></el-table-column>
      <el-table-column align="center" prop="key" label="键值"></el-table-column>
      <el-table-column label="操作" width="100">
        <template slot-scope="scope">
          <el-button @click="deleteCategory(scope.row)" type="text" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="收货地址" :visible.sync="dialogFormVisible">
      
      <el-input v-model="inputdata.category" placeholder="请输入目录名称">
          <el-select slot="prepend" style="width:150px;" v-model="inputdata.type" placeholder="请选择类型">
            <el-option label="公共" value="public"></el-option>
            <el-option label="私有" value="private"></el-option>
            </el-select>
      </el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addCategory">确 定</el-button>
      </div>
    </el-dialog>
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
      dialogFormVisible: false,
      category_list: [],
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
    addCategory() {
      var thisv = this;
      if (thisv.$data.inputdata.type && thisv.$data.inputdata.category) {
        var key = thisv.$data.inputdata.type + "/";
        key = key + thisv.$data.inputdata.category + "/";
        cos.putObject(
          {
            Bucket: Bucket /* 必须 */,
            Region: Region /* 必须 */,
            Key: key /* 必须 */,
            Body: ""
          },
          function(err, data) {
            if (err) {
              thisv.$message(err);
            } else {
              thisv.$message("添加成功");
              thisv.$data.dialogFormVisible = false;
              thisv.$data.inputdata.type = "";
              thisv.$data.inputdata.category = "";
              thisv.getList();
            }
          }
        );
      } else {
        this.$message("数据不完整");
      }
    },
    getList() {
      this.initUser();
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
            if(!key.endsWith("/")){
                key="";
            }
            let el = {};
            el.key = key;
            if (key.indexOf("public/") >= 0) {
              key = key.replace("public/", "");
              el.type = "公共";
              if (key.indexOf("/") >= 0) {
                el.category = key.substring(0, key.indexOf("/"));
                rs.push(el);
              }
            } else if (key.indexOf("private/") >= 0) {
              key = key.replace("private/", "");
              el.type = "私有";
              if (key.indexOf("/") >= 0) {
                el.category = key.substring(0, key.indexOf("/"));
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
