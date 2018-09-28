package com.gitblog.create;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import cn.hutool.core.io.FileUtil;

@PropertySource("classpath:application.properties")
@Component
public class BlogCreater {

    
    @Value("${gitblog.savepath}")
    private String savepath;
    
    public void createBlog(List<Map<String,Object>> list) {
        String srcPath=FileUtil.getAbsolutePath("classpath:templates/assets/");
        FileUtil.copy(srcPath, savepath, true);
        File template=FileUtil.file("templates/blog.html");
        try {
            int i=1;
            for(Map<String,Object> data:list) {
                Document doc=Jsoup.parse(template, "utf-8");
                Element title = doc.select("span#gitblog_title").first(); // <div></div>
                title.html(data.get("TITLE").toString()); 
                
                Element content = doc.select("div#gitblog_content").first(); // <div></div>
                content.html(data.get("CONTENT").toString()); 
                
                Element date = doc.select("span#gitblog_date").first(); // <div></div>
                date.html(data.get("CREATE_TIME").toString()); 
                FileUtil.writeString(doc.toString(), savepath+"/"+i+".html", "utf-8");
                i++;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
