package com.gitblog.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
@Order(1)
@Component
@WebFilter(urlPatterns = "/api/*", filterName = "crossDomainFilter")
public class CrossDomainFilter implements Filter{

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) res;      
        response.setHeader("Access-Control-Allow-Origin", "*");    
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");      
        response.setHeader("Access-Control-Max-Age", "3600");       
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Content-Type");       
        response.setHeader("Access-Control-Allow-Credentials", "true");
        chain.doFilter(req, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("开启跨域！过滤器启动！");
    }

}
