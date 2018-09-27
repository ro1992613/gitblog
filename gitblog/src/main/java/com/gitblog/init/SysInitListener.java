package com.gitblog.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
@Component
public class SysInitListener implements ApplicationListener<ContextRefreshedEvent>{

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println("-----------------springboot 初始化完成！--------------------------");
    }

}
