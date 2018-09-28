package com.gitblog.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
@Component
public class SysInitListener implements ApplicationListener<ContextRefreshedEvent>{

    static final String initsql="CREATE TABLE IF NOT EXISTS BLOG("
            + "ID VARCHAR(255) NOT NULL PRIMARY KEY,"
            + "TITLE VARCHAR(255),"
            + "CREATE_TIME DATE,"
            + "CONTENT CLOB,"
            + "DESCRIPTION VARCHAR(255),"
            + "REMARK VARCHAR(255)"
            + ")";
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        jdbcTemplate.update(initsql);
        System.out.println("-----------------springboot 初始化完成！--------------------------");
    }

}
