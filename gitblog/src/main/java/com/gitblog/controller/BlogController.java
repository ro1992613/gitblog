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
        String sql="select id,title,description,create_time from blog";
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
        sql="select id,title,description,create_time from blog";
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
        Date date=new Date();
        String sql="insert into blog(id,title,content,create_time,description) values(?,?,?,?,?)";
        try {
            code=jdbcTemplate.update(sql, id,title,content,date,description);
            System.out.println(code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }
    @PostMapping("/create")
    public String create() {
        String sql="select * from blog";
        List<Map<String,Object>> list=jdbcTemplate.queryForList(sql);
        blogCreater.createBlog(list);
        blogCreater.createIndex(list);
        return FileUtil.getAbsolutePath("classpath:templates/assets/");
    }
}
