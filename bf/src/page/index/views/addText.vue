<template>
<el-card class="box-card">
  <div slot="header" style="text-align:left" class="clearfix">
      <span><i class="el-icon-edit">新增博客</i></span>
  </div>
  <div style="margin-bottom:10px;">
    <el-row>
      <el-col :span="4">
        <el-select v-model="collection_id" placeholder="请选择">
          <el-option
            v-for="item in collection_list"
            :key="item.COLLECTION_ID"
            :label="item.COLLECTION_NAME"
            :value="item.COLLECTION_ID">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="20">
        <el-input v-model="title" placeholder="请输入标题"></el-input>
      </el-col>
    </el-row>
  </div>
  <div style="height:100px;">
    <el-input type="textarea" :rows="3" placeholder="请输入描述" v-model="description" clearable>
    </el-input>
  </div>
  <div style="min-height: 100%;margin-bottom:10px;">
    <div id="editorElem" style="text-align:left"></div>
  </div>
  <div>
    <el-button style="float: left;margin-bottom:10px;" type="primary" @click="saveEditorContent">保存<i class="el-icon-upload el-icon--right"></i></el-button>
  </div>
</el-card>
</template>
<style>
.box-card{
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
import E from 'wangeditor'
export default {
  data () {
    return {
      title:'',
      content: '',
      description:'',
      collection_id:'',
      collection_name:'',
      collection_list:[]
    }
  },
  methods: {
    saveEditorContent(){
        var this_v=this;
        if(!this_v.$data.collection_name){
          this_v.$data.collection_list.map(function(v){
            if(this_v.$data.collection_id==v.COLLECTION_ID){
              this_v.$data.collection_name=v.COLLECTION_NAME;
            }
          });
        }
        this.$http.post(this.baseUrl+'/api/blog/add',{
          'title':this.$data.title,
          'content':this.$data.content,
          'collection_id':this.$data.collection_id,
          'collection_name':this.$data.collection_name,
          'description':this.$data.description
        }).then(function(resp){
          this.$router.push({path:"/menu/list"});
        });
    },
    test(){
      console.log(this.$data.collection);
    }
  },
  computed: {
  },
  mounted() {
    var v=this;
    v.$http.post(this.baseUrl+'/api/blog/collection_list',{}).then(function(resp){
      v.$data.collection_list=resp.data;
    });
    var editor = new E("#editorElem");
    editor.customConfig.uploadImgShowBase64 = true
    editor.customConfig.onchange = html => {
      v.$data.content = html;
    };
    editor.create();
  }
}
</script>


