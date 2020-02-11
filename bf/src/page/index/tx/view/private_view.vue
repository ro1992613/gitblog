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
                      静态页面+腾讯云的对象存储！基本上够用了！
                    </p>
                    <p>
                      兴趣的话可以看下面的链接
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


