package com.gitblog.create;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
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
        if(!srcPath.toLowerCase().contains(".jar")) {
            createBlogNormal(list);
        }else {
            try {
                createBlogFromJar(list);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
    
    public void createBlogNormal(List<Map<String,Object>> list) {
        String srcPath=FileUtil.getAbsolutePath("classpath:templates/assets/");
        FileUtil.copy(srcPath, savepath, true);
        FileUtil.copy(FileUtil.getAbsolutePath("classpath:templates/index.html"), savepath+"/index.html", true);
        FileUtil.copy(FileUtil.getAbsolutePath("classpath:templates/collection.html"), savepath+"/collection.html", true);
        FileUtil.copy(FileUtil.getAbsolutePath("classpath:templates/collection_list.html"), savepath+"/collection_list.html", true);
        srcPath=FileUtil.getAbsolutePath("classpath:templates/admin/");
        FileUtil.copy(srcPath, savepath, true);
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
    public void createBlogFromJar(List<Map<String,Object>> list) throws IOException {
        SelfFileUtil.loadRecourseFromJarByFolder("/templates/assets", savepath+"/assets");
        SelfFileUtil.loadRecourseFromJarByFolder("/templates/admin", savepath+"/admin");
        copyFileFromJar("templates/index.html", savepath+"/index.html");
        copyFileFromJar("templates/collection.html", savepath+"/collection.html");
        copyFileFromJar("templates/collection_list.html", savepath+"/collection_list.html");

        for(Map<String,Object> data:list) {
            InputStream in = FileUtil.class.getClassLoader().getResourceAsStream("templates/blog.html");
            try {
                Document doc=Jsoup.parse(in, "utf-8","");
                Element title = doc.select("span#gitblog_title").first(); // <div></div>
                title.html(data.get("TITLE").toString()); 
                
                Element content = doc.select("div#gitblog_content").first(); // <div></div>
                content.html(data.get("CONTENT").toString()); 
                
                Element date = doc.select("span#gitblog_date").first(); // <div></div>
                date.html(data.get("CREATE_TIME").toString()); 
                FileUtil.writeString(doc.toString(), savepath+"/"+data.get("id")+".html", "utf-8");
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                in.close();
            }
        }
    }
    
    public void createIndex(List<Map<String,Object>> list) {
        List<Map<String,Object>> result=new ArrayList<>();
        List<Map<String,Object>> collection=new ArrayList<>();
        Set<String> set=new HashSet<String>();
        for(Map<String,Object> data:list) {
           Map<String,Object> map=new HashMap<>();
           Date date=(Date) data.get("create_time");
           map.put("id", data.get("id"));
           map.put("title", data.get("title"));
           map.put("collection_id", data.get("collection_id"));
           map.put("collection_name", data.get("collection_name"));
           map.put("create_time", DateUtil.format(date, "yyyy-MM-dd"));
           map.put("description", data.get("description"));
           map.put("remark", data.get("remark"));
           if(!set.contains(data.get("collection_id"))) {
               if(data.get("collection_id")!=null && !data.get("collection_id").equals("")) {
                   set.add(data.get("collection_id").toString());
                   Map<String,Object> col=new HashMap<>();
                   col.put("collection_id", data.get("collection_id"));
                   col.put("collection_name", data.get("collection_name"));
                   collection.add(col);
               }
           }
           result.add(map);
        }
        JSON json=JSONUtil.parse(result);
        StringBuffer str=new StringBuffer("var data=");
        str.append(json.toString());
        str.append(";var collection=");
        json=JSONUtil.parse(collection);
        str.append(json.toString());
        str.append(";");
        FileUtil.writeString(str.toString(),savepath+"/store.js", "utf-8");
    }
    
    /**
     * 复制path目录下所有文件  
     * @param path  文件目录 不能以/开头
     * @param newpath 新文件目录
     */
    public static void BatCopyFileFromJar(String path,String newpath) {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            //获取所有匹配的文件
            Resource[] resources = resolver.getResources(path+"/*");
            for(int i=0;i<resources.length;i++) {
                Resource resource=resources[i];

                try {
                    //以jar运行时，resource.getFile().isFile() 无法获取文件类型，会报异常，抓取异常后直接生成新的文件即可；以非jar运行时，需要判断文件类型，避免如果是目录会复制错误，将目录写成文件。
                    if(resource.getFile().isFile()) {
                        makeFile(newpath+"/"+resource.getFilename());
                        InputStream stream = resource.getInputStream();
                        write2File(stream, newpath+"/"+resource.getFilename()); 
                    }
                }catch (Exception e) {
                    makeFile(newpath+"/"+resource.getFilename());
                    InputStream stream = resource.getInputStream();
                    write2File(stream, newpath+"/"+resource.getFilename());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 复制单个文件 从classpath中读取文件复制
     * @param path  不能以/开头   指向文件不能是目录
     * @param newpath   指向文件不能是目录
     */
    public static void copyFileFromJar(String path,String newpath) {
        try {
            //创建新文件
            makeFile(newpath);
            //获取文件流
            InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(path);
            //将流写入新文件
            write2File(in, newpath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 创建文件
     * @param path  全路径 指向文件
     * @return
     */
    public static boolean makeFile(String path) {  
        File file = new File(path);  
        if(file.exists()) {   
            return false;  
        }  
        if (path.endsWith(File.separator)) {  
            return false;  
        }  
        if(!file.getParentFile().exists()) {   
            if(!file.getParentFile().mkdirs()) {  
                return false;  
            }  
        }  
        try {  
            if (file.createNewFile()) {  
                return true;  
            } else {  
                return false;  
            }  
        } catch (IOException e) {  
            e.printStackTrace(); 
            return false;  
        }  
    }  
    /**
     * 输入流写入文件
     * 
     * @param is
     *            输入流
     * @param filePath
     *            文件保存目录路径
     * @throws IOException
     */
    public static void write2File(InputStream is, String filePath) throws IOException {
        OutputStream os = new FileOutputStream(filePath);
        int len = 8192;
        byte[] buffer = new byte[len];
        while ((len = is.read(buffer, 0, len)) != -1) {
            os.write(buffer, 0, len);
        }
        os.close();
        is.close();
    }
}
