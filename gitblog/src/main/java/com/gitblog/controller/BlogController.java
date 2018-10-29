package com.gitblog.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gitblog.create.BlogCreater;

import cn.hutool.core.io.FileUtil;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    BlogCreater blogCreater;
    
    @PostMapping("/list")
    public List<Map<String,Object>> list() {
        String sql="select * from blog order by create_time desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    @PostMapping("/collection_list")
    public List<Map<String,Object>> collection_list() {
        String sql="select collection_id,collection_name from collection";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    
    @PostMapping("/delete_collection")
    public List<Map<String,Object>> delete_collection(@RequestBody Map<String, String> params) {
        String collection_id=params.get("collection_id");
        String sql="delete from collection where collection_id=?";
        jdbcTemplate.update(sql,collection_id);
        sql="select collection_id,collection_name from collection";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    
    @PostMapping("/find")
    public Map<String,Object> find(@RequestBody Map<String, String> params) {
        String id=params.get("id");
        String sql="select * from blog where id=?";
        Map<String,Object> data=jdbcTemplate.queryForMap(sql,id);
        return data;
    }
    @PostMapping("/delete")
    public List<Map<String,Object>> delete(@RequestBody Map<String, String> params) {
        String id=params.get("id");
        String sql="delete from blog where id=?";
        jdbcTemplate.update(sql,id);
        sql="select * from blog order by create_time desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
    
    @PostMapping("/add")
    public int add(@RequestBody Map<String, String> params) {
        int code=0;
        String title=params.get("title");
        String content=params.get("content");
        String id=System.currentTimeMillis()+"";
        String description=params.get("description");
        String collection_name=params.get("collection_name");
        String collection_id=params.get("collection_id");
        Date date=new Date();
        String sql="insert into blog(id,title,content,create_time,description,collection_id,collection_name) values(?,?,?,?,?,?,?)";
        try {
            code=jdbcTemplate.update(sql, id,title,content,date,description,collection_id,collection_name);
            System.out.println(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
    @PostMapping("/update_article")
    public int update_article(@RequestBody Map<String, String> params) {
        int code=0;
        String title=params.get("title");
        String content=params.get("content");
        String article_id=params.get("article_id");
        String description=params.get("description");
        String collection_name=params.get("collection_name");
        String collection_id=params.get("collection_id");
        String sql="update blog set title=?,content=?,description=?,collection_id=?,collection_name=? where id=?";
        try {
            code=jdbcTemplate.update(sql,title,content,description,collection_id,collection_name,article_id);
            System.out.println(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
    
    @PostMapping("/create")
    public String create() {
        String sql="select * from blog order by create_time desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        blogCreater.createBlog(list);
        blogCreater.createIndex(list);
        return FileUtil.getAbsolutePath("classpath:templates/assets/");
    }
    
    @PostMapping("/add_collection")
    public int add_collection(@RequestBody Map<String, String> params) {
        int code=0;;
        String collection_name=params.get("collection_name");
        String collection_id=System.currentTimeMillis()+"";
        String sql="insert into collection(collection_id,collection_name) values(?,?)";
        try {
            code=jdbcTemplate.update(sql,collection_id,collection_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
    
    @PostMapping("/update_collection")
    public List<Map<String,Object>> update_collection(@RequestBody Map<String, String> params) {
        String article_id=params.get("article_id");
        String collection_id=params.get("collection_id");
        String collection_name=params.get("collection_name");
        String sql="update blog set collection_id=?,collection_name=? where id=?";
        try {
            jdbcTemplate.update(sql,collection_id,collection_name,article_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql="select id,title,description,create_time from blog order by create_time desc";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        return list;
    }
}
