package com.gitblog.create;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;

@PropertySource("classpath:application.properties")
@Component
public class BlogCreater {

    
    @Value("${gitblog.savepath}")
    private String savepath;
    
    public void createBlog(List<Map<String,Object>> list) {
        String srcPath=FileUtil.getAbsolutePath("classpath:templates/assets/");
        FileUtil.copy(srcPath, savepath, true);
        FileUtil.copy(FileUtil.getAbsolutePath("classpath:templates/index.html"), savepath+"/index.html", true);
        File template=FileUtil.file("templates/blog.html");
        try {
            for(Map<String,Object> data:list) {
                Document doc=Jsoup.parse(template, "utf-8");
                Element title = doc.select("span#gitblog_title").first(); // <div></div>
                title.html(data.get("TITLE").toString()); 
                
                Element content = doc.select("div#gitblog_content").first(); // <div></div>
                content.html(data.get("CONTENT").toString()); 
                
                Element date = doc.select("span#gitblog_date").first(); // <div></div>
                date.html(data.get("CREATE_TIME").toString()); 
                FileUtil.writeString(doc.toString(), savepath+"/"+data.get("id")+".html", "utf-8");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void createIndex(List<Map<String,Object>> list) {
        List<Map<String,Object>> result=new ArrayList<>();
        for(Map<String,Object> data:list) {
           Map<String,Object> map=new HashMap<>();
           Date date=(Date) data.get("create_time");
           map.put("id", data.get("id"));
           map.put("title", data.get("title"));
           map.put("create_time", DateUtil.format(date, "yyyy-MM-dd"));
           map.put("description", data.get("description"));
           map.put("remark", data.get("remark"));
           result.add(map);
        }
        JSON json=JSONUtil.parse(result);
        StringBuffer str=new StringBuffer("var data=");
        str.append(json.toString());
        FileUtil.writeString(str.toString(),savepath+"/store.js", "utf-8");
    }
}
