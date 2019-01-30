<template>
    <div class="main">
        <div class="right_c">
            <el-card shadow="never" style="text-align: left;margin: 2px auto;">
           <div slot="header" class="clearfix">
                <span><i class="el-icon-document"></i>{{title}}</span>
                <el-button  @click="to_public()" style="float: right; padding: 3px 0" type="text">返回</el-button>
                <br>
                <el-tag size="mini" type="success">{{typeName}}</el-tag>
                <el-tag size="mini" type="info">{{category}}</el-tag>
            </div>
            
            <div v-html="tdata">
            </div>
            </el-card>    
        </div>
        <div class="left_c">
            <el-card shadow="never" style="text-align: left;margin: 4px auto;">
                <div slot="header" class="clearfix">
                    <span>天气</span>
                </div>
                <iframe height="50" frameborder="0" scrolling="no" hspace="0" src="https://i.tianqi.com/index.php?c=code&amp;id=12&amp;icon=1&amp;num=2&amp;site=12"> </iframe>
            </el-card>
            <el-card shadow="never" style="text-align: left;margin: 10px auto;">
                <div slot="header" class="clearfix">
                    <span>简介</span>
                </div>
                <div>
                    <p>
                   做过很多个的博客了，发现自己做博客最大的问题就是，没服务器！虽然也会买一些云服务器，但是还是觉得好贵啊！
                    </p>
                    <p>
                    后来听说可以用github做博客，自己也折腾过hexo之类的东东，但始终用起来很不爽！毕竟是别人定制的东西，很多diy的地方都不是那么尽人意！
                    </p>
                    <p>
                    所以最后决定自己写一个，反正git的页面就是些静态页面呗！自己写个内容管理系统还不是手到擒来！后台用springboot很容易就搞定了，数据库在几番研究后使用了非常轻量的嵌入式数据库，h2！然后数据移植的问题也搞定了！用vue随意搭了个管理页面，写上几个模板，新鲜出炉的gitblog就做好了！
                    </p>
                    <p>
                    接下来，优化模板的样式，增加标签功能，把管理页面嵌入博客，加入评论！！！！
                    </p>
                </div>
                <a target="_blank" href="https://github.com/ro1992613/gitblog">查看更多</a>
            </el-card>   
        </div>
    </div>
</template>
<style scoped>
.main {
  margin: 0 auto;
  width: 1200px;
  height: auto;
  display: flex;
}

.right_c {
  flex: 12;
  padding: 5px;
  height: auto;
}
.left_c {
  flex: 4;
  padding: 5px;
  height: auto;
}
</style>
<script>
import "../cos-js-sdk-v5.min.js";
var Bucket = "cyp20190111-1251525641";
var Region = "ap-chengdu";
var key={
  SecretId: "AKIDNDEoAFsUFLL95otDNL5tGOK17PelmpQs",
  SecretKey: "Pq20vuqjaGn7DPPAg0A6nOllaS8alupB"
}
var private_key={
  SecretId: "AKID2qOioeydrqa5LMkEn6AzYsNddMtBsfXB",
  SecretKey: "Z6OVO1PYT9cqsxqUu6FApHbfU4PVheLo"
}

var cos = new COS(key);
export default {
  data() {
    return {
      tdata: "",
      title: "",
      typeName: "",
      category: ""
    };
  },
  methods: {
      to_public(){
          this.$router.back(-1);
      },
      to_private(){
          this.$router.push({path:"/view/private"});
      },
    initData() {
      var this_v = this;
      var key = this_v.$route.query.key;
      var params = {
        Bucket: Bucket /* 必须 */,
        Region: Region /* 必须 */
      };
      params.Key = key;

      if (key.indexOf("public/") >= 0) {
        key = key.replace("public/", "");
        this_v.$data.typeName = "公共";
      } else if (key.indexOf("private/") >= 0) {
        key = key.replace("private/", "");
        this_v.$data.typeName = "私有";
      }
      this_v.category = key.substring(0, key.indexOf("/"));
      this_v.$data.title = key.substring(key.indexOf("/") + 1, key.length);
      cos.getObject(params, function(err, data) {
        if (err) {
          console.log(err);
        } else {
          console.log(data);
          this_v.$data.tdata = data.Body;
        }
      });
    }
  },
  computed: {},
  mounted() {
    this.initData();
  }
};
</script>


