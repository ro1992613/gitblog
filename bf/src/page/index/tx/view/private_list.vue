<template>
  <div class="main">
    
    <div class="right_c">
        <!-- <el-carousel trigger="click" height="350px">
            <el-carousel-item v-for="item in img_urls" :key="item">
                <img class="img_c" :src="item"/>
            </el-carousel-item>
        </el-carousel> -->
      <el-card shadow="never" style="text-align: left;margin: 2px auto;">
        <div slot="header" class="clearfix">
          <span>文章列表</span>
          <el-button style="float: right; padding: 3px 0" @click="initUser" type="text"><i class="el-icon-sort"></i></el-button>
          <el-button
            v-if="search_key"
            @click="setFilters()"
            style="float: right; padding: 3px 0"
            type="text"
          >重置条件
            <el-tag type="warning" size="mini">{{search_key}}</el-tag>
          </el-button>
        </div>
        <el-card
          shadow="never"
          style="text-align: left;margin: 10px auto;"
          v-for="item in show_list"
          :key="item"
        >
          <div slot="header" class="clearfix">
            <span>
            <el-button
              @click="findArticle(item.key)"
              style="color:#000;font-size: 20px"
              type="text"
            >
            <i class="el-icon-tickets"></i>
            {{item.title}}
            </el-button>
              
            </span>
            <el-button
              @click="findArticle(item.key)"
              style="float: right; padding: 10px 0"
              type="text"
            >
            <i class="el-icon-view"></i>
            </el-button>
          </div>
          <div>
            <el-button size="mini">{{item.date}}</el-button>
            <el-button size="mini" @click="setFilters(item.type)" type="success">{{item.typeName}}</el-button>
            <el-button size="mini" @click="setFilters(item.category)" type="info">{{item.category}}</el-button>
            <el-button size="mini" type="warning">{{item.size}}</el-button>
            <el-button size="mini" type="danger">{{item.tag}}</el-button>
          </div>
        </el-card>
      </el-card>
      <el-card shadow="never" style="text-align: left;margin: 2px auto;">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="page_total"
          :page-size="page_size"
          :current-page="page_index"
          @current-change="pageChange"
        ></el-pagination>
      </el-card>
    </div>
    <div class="left_c">
      <el-card shadow="never" style="text-align: left;margin: 4px auto;">
        <div slot="header" class="clearfix">
          <span>天气</span>
        </div>
        <iframe
          height="50"
          frameborder="0"
          scrolling="no"
          hspace="0"
          src="https://i.tianqi.com/index.php?c=code&amp;id=12&amp;icon=1&amp;num=2&amp;site=12"
        ></iframe>
      </el-card>
      <el-card shadow="never" style="text-align: left;margin: 10px auto;">
        <div slot="header" class="clearfix">
          <span>搜索</span>
        </div>
        <el-input placeholder="请输入内容" v-model="search_key">
          <el-button slot="append" icon="el-icon-search" @click="filterList()"></el-button>|
        </el-input>
      </el-card>
      <el-card shadow="never" style="text-align: left;margin: 10px auto;">
        <div slot="header" class="clearfix">
          <span>简介</span>
        </div>
        <div>
          <p>做过很多个的博客了，发现自己做博客最大的问题就是，没服务器！虽然也会买一些云服务器，但是还是觉得好贵啊！</p>
          <p>后来听说可以用github做博客，自己也折腾过hexo之类的东东，但始终用起来很不爽！毕竟是别人定制的东西，很多diy的地方都不是那么尽人意！</p>
          <p>所以最后决定自己写一个，反正git的页面就是些静态页面呗！自己写个内容管理系统还不是手到擒来！后台用springboot很容易就搞定了，数据库在几番研究后使用了非常轻量的嵌入式数据库，h2！然后数据移植的问题也搞定了！用vue随意搭了个管理页面，写上几个模板，新鲜出炉的gitblog就做好了！</p>
          <p>接下来，优化模板的样式，增加标签功能，把管理页面嵌入博客，加入评论！！！！</p>
        </div>
        <a target="_blank" href="https://github.com/ro1992613/gitblog">查看更多</a>
      </el-card>
      <!-- <el-card shadow="never" style="text-align: left;margin: 10px auto;">
        <div slot="header" class="clearfix">
          <span>随机列表</span>
          <el-button style="float: right; padding: 3px 0" type="text">刷新</el-button>
        </div>
      </el-card> -->
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
.img_c{
width:100%;
height:100%;
border-radius:5px;
margin-bottom: 5px;
}
</style>
<script>
import "../cos-js-sdk-v5.min.js";
var Bucket = "cyp20190111-1251525641";
var Region = "ap-chengdu";
var key = {
  SecretId: "AKIDNDEoAFsUFLL95otDNL5tGOK17PelmpQs",
  SecretKey: "Pq20vuqjaGn7DPPAg0A6nOllaS8alupB"
};
var private_key = {
  SecretId: "AKID2qOioeydrqa5LMkEn6AzYsNddMtBsfXB",
  SecretKey: "Z6OVO1PYT9cqsxqUu6FApHbfU4PVheLo"
};

var cos = new COS(key);
export default {
  data() {
    return {
      category_list: [],
      filter_list: [],
      show_list: [],
      img_urls:[],
      page_size: 10,
      page_index: 1,
      page_total: 0,
      search_key: ""
    };
  },
  methods: {
    initUser(){
        var tempuser=window.prompt("提示","输入密码吧！");
        if(tempuser==this.sysuser){
            window.localStorage.setItem("user",tempuser);
            this.getList();
        }
    },
    findArticle(key) {
      this.$router.push({ path: "/view/all_view", query: { key: key } });
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
            if (key.endsWith("/")) {
              key = "";
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
                el.title = key.substring(key.indexOf("/") + 1, key.length);
                rs.push(el);
              }
            } else if (key.indexOf("private/") >= 0 && window.localStorage.getItem("user") && window.localStorage.getItem("user")==this_v.sysuser) {
              key = key.replace("private/", "");
              el.type = "private";
              el.typeName = "私有";
              if (key.indexOf("/") >= 0) {
                el.category = key.substring(0, key.indexOf("/"));
                el.title = key.substring(key.indexOf("/") + 1, key.length);
                rs.push(el);
              }
            }
          });
        }
        rs.sort((a,b)=>{
          return b.index-a.index;
        });
        this_v.$data.category_list = rs;
        this_v.$data.filter_list = rs;
        this_v.initList();
      });
    },
    setFilters(search_key) {
      this.$data.search_key = search_key;
      this.filterList();
    },
    filterList() {
      if (this.$data.search_key) {
        let rs = [];
        this.$data.category_list.map(v => {
          console.log(v);
          if (v.key.indexOf(this.$data.search_key) > -1) {
            console.log("push" + v);
            rs.push(v);
          }
        });
        this.$data.filter_list = rs;
      } else {
        this.$data.filter_list = this.$data.category_list;
      }
      this.initList();
    },
    initList() {
      var page_size = this.$data.page_size;
      var page_total = this.$data.filter_list.length;
      var page_index = this.$data.page_index;
      this.$data.page_total = this.$data.filter_list.length;
      this.$data.show_list = [];
      for (
        var i = page_size * (page_index - 1);
        i < page_size * page_index && i < this.$data.page_total;
        i++
      ) {
        this.$data.show_list.push(this.$data.filter_list[i]);
      }
    },
    pageChange(val) {
      this.$data.page_index = val;
      this.initList();
    }
  },
  computed: {},
  mounted() {
      this.$data.img_urls=[];
    for(var i=0;i<=5;i++){
        let url="https://uploadbeta.com/api/pictures/random/?key=BingEverydayWallpaperPicture&s=";
        this.$data.img_urls.push(url+i);
    }
    this.getList();
  }
};
</script>


