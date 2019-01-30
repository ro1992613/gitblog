<template>
  <el-card class="box-card">
    <div slot="header" style="text-align:left" class="clearfix">
      <span>
        <i class="el-icon-edit">新增博客</i>
      </span>
    </div>
    <div style="margin-bottom:10px;">
      <el-row>
        <el-col :span="4">
          <el-select :disabled="inputStatus.type" v-model="inputdata.type" @change="typeChange" placeholder="请选择类型">
            <el-option label="公共" value="public"></el-option>
            <el-option label="私有" value="private"></el-option>
          </el-select>
        </el-col>
        <el-col :span="4">
          <el-select :disabled="inputStatus.category" v-model="inputdata.category" placeholder="请选择">
            <el-option
              v-for="item in select_categry_list"
              :key="item.category"
              :label="item.category"
              :value="item.category"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="16">
          <el-input :disabled="inputStatus.title" v-model="title" placeholder="请输入标题"></el-input>
        </el-col>
      </el-row>
    </div>
    <br>
    <div style="min-height: 100%;margin-bottom:10px;">
      <div id="editorElem" style="text-align:left"></div>
    </div>
    <div>
      <el-button
        style="float: left;margin-bottom:10px;"
        type="primary"
        @click="saveEditorContent"
      >保存
        <i class="el-icon-upload el-icon--right"></i>
      </el-button>
    </div>
  </el-card>
</template>
<style>
.box-card {
  min-height: 98%;
}
.quill-editor {
  min-height: 700px;
}
.ql-container {
  min-height: 600px;
}
</style>
<script>
import E from "wangeditor";
import "./cos-js-sdk-v5.min.js";
var editor;
var Bucket = "cyp20190111-1251525641";
var Region = "ap-chengdu";
var cos = new COS({
  SecretId: "AKID2qOioeydrqa5LMkEn6AzYsNddMtBsfXB",
  SecretKey: "Z6OVO1PYT9cqsxqUu6FApHbfU4PVheLo"
});
export default {
  data() {
    return {
      inputStatus:{
        type:false,
        category:false,
        title:false
      },
      title: "",
      content: "",
      select_categry_list: [],
      inputdata: {
        type: "",
        category: ""
      },
      public_categry_list: [],
      private_categry_list: []
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
    saveEditorContent() {
      var thisv = this;
      var params = {
        Bucket: Bucket /* 必须 */,
        Region: Region /* 必须 */
      };

      if (thisv.$data.inputdata.type && thisv.$data.inputdata.category) {
        var key = thisv.$data.inputdata.type + "/";
        key = key + thisv.$data.inputdata.category + "/";
        key = key + thisv.$data.title;
        params.Key = key;
        params.Body = thisv.$data.content;
        cos.putObject(params, function(err, data) {
          console.log(err || data);
          if(err){
              thisv.$message(err);
          }else{
              thisv.$message("新增成功");
            thisv.$router.push({path:"/menu/tx_list"});
          }
        });
      }
    },
    initMyEditor(){
      var params = {
        Bucket: Bucket /* 必须 */,
        Region: Region /* 必须 */
      };
      if(this.$route.query.key){
        params.Key=this.$route.query.key;
        let type=this.$route.query.type;
        if(type=="private"){
          this.$data.select_categry_list = this.$data.private_categry_list;
        }else{
          this.$data.select_categry_list = this.$data.public_categry_list;
        }
        this.$data.inputdata.type = type;
        this.$data.inputdata.category =this.$route.query.category;
        this.$data.title=this.$route.query.title;

        this.$data.inputStatus.type=true;
        this.$data.inputStatus.category=true;
        this.$data.inputStatus.title=true;
        cos.getObject(params, function(err, data) {
            if(err) {
                console.log(err);
            } else {
                editor.txt.html(data.Body);
            }
        });
       
      }
    },
    getcategoryList() {
      var this_v = this;
      var params = {
        Bucket: Bucket /* 必须 */,
        Region: Region /* 必须 */
      };
      cos.getBucket(params, function(err, data) {
        let public_rs = [];
        let private_rs = [];
        if (err) {
          console.log(err);
        } else {
          data.Contents.map(v => {
            let key = v.Key;
            if (!key.endsWith("/")) {
              key = "";
            } else {
              if (key.indexOf("public/") >= 0) {
                key = key.replace("public/", "");
                if (key.indexOf("/") >= 0) {
                  public_rs.push({
                    category: key.substring(0, key.indexOf("/"))
                  });
                }
              } else if (key.indexOf("private/") >= 0) {
                key = key.replace("private/", "");
                if (key.indexOf("/") >= 0) {
                  private_rs.push({
                    category: key.substring(0, key.indexOf("/"))
                  });
                }
              }
            }
          });
        }
        this_v.$data.public_categry_list = public_rs;
        this_v.$data.private_categry_list = private_rs;
        this_v.select_categry_list = public_rs;
        this_v.$data.inputdata.type = "public";
        this_v.initMyEditor();
      });
    },
    typeChange() {
      var this_v = this;
      if (this_v.$data.inputdata.type == "private") {
        this_v.$data.select_categry_list = this_v.$data.private_categry_list;
      } else {
        this_v.$data.inputdata.type = "public";
        this_v.$data.select_categry_list = this_v.$data.public_categry_list;
      }
    }
  },
  computed: {},
  mounted() {
    var v = this;
    this.initUser();
    v.getcategoryList();
    editor = new E("#editorElem");
    editor.customConfig.uploadImgShowBase64 = true;
    editor.customConfig.onchange = html => {
      v.$data.content = html;
    };
    editor.create();
  }
};
</script>


