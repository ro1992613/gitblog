<template>
<el-card class="box-card">
  <div slot="header" style="text-align:left" class="clearfix">
      <span>新增博客</span>
  </div>
  <div style="margin-bottom:10px;">
    <el-input v-model="title" placeholder="请输入标题"></el-input>
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
      description:''
    }
  },
  methods: {
    saveEditorContent(){
        this.$http.post(this.baseUrl+'/api/blog/add',{
          'title':this.$data.title,
          'content':this.$data.content,
          'description':this.$data.description
        }).then(function(resp){
          this.$router.push({path:"/menu/list"});
        });
    }
  },
  computed: {
  },
  mounted() {
    var v=this;
    var editor = new E("#editorElem");
    editor.customConfig.uploadImgShowBase64 = true
    editor.customConfig.onchange = html => {
      v.$data.content = html;
    };
    editor.create();
  }
}
</script>


