webpackJsonp([8],{Uhm3:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var o=n("I4FZ"),i=n.n(o),a={name:"editor",data:function(){return{editorContent:""}},methods:{getContent:function(){alert(this.$data.editorContent)}},mounted:function(){var t=this,e=new i.a("#editorElem");e.customConfig.uploadImgShowBase64=!0,e.customConfig.onchange=function(e){t.$data.editorContent=e},e.create()}},r={render:function(){var t=this.$createElement,e=this._self._c||t;return e("div",[e("div",{staticStyle:{"text-align":"left"},attrs:{id:"editorElem"}}),this._v(" "),e("button",{on:{click:this.getContent}},[this._v("查看内容")])])},staticRenderFns:[]},s=n("SZvu")(a,r,!1,null,null,null);e.default=s.exports}});
//# sourceMappingURL=8.8dc12df5b2132b5291d4.js.map