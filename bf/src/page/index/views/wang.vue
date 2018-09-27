<template>
    <div>
    <div id="editorElem" style="text-align:left"></div>
   <button v-on:click="getContent">查看内容</button>
    </div>
</template>
<script>
import E from 'wangeditor'
export default {
  name: "editor",
  data() {
    return {
      editorContent: ""
    };
  },
  methods: {
    getContent: function() {
      alert(this.$data.editorContent);
    }
  },
  mounted() {
    var v=this;
    var editor = new E("#editorElem");
    editor.customConfig.uploadImgShowBase64 = true
    editor.customConfig.onchange = html => {
      v.$data.editorContent = html;
    };
    editor.create();
  }
};
</script>
