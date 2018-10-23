package com.gitblog.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
@Component
public class SysInitListener implements ApplicationListener<ContextRefreshedEvent>{

    static final String init_blog="CREATE TABLE IF NOT EXISTS BLOG("
            + "ID VARCHAR(255) NOT NULL PRIMARY KEY,"
            + "TITLE VARCHAR(255),"
            + "CREATE_TIME DATE,"
            + "CONTENT CLOB,"
            + "DESCRIPTION VARCHAR(255),"
            + "COLLECTION_ID VARCHAR(255),"
            + "COLLECTION_NAME VARCHAR(255),"
            + "REMARK VARCHAR(255)"
            + ")";
    static final String init_collection="CREATE TABLE IF NOT EXISTS COLLECTION("
            + "COLLECTION_ID VARCHAR(255) NOT NULL PRIMARY KEY,"
            + "COLLECTION_NAME VARCHAR(255))";
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        jdbcTemplate.update(init_blog);
        jdbcTemplate.update(init_collection);
        System.out.println("-----------------springboot 初始化完成！--------------------------");
    }

}
