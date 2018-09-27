package com.gitblog.gitblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.*")
public class GitblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(GitblogApplication.class, args);
	}
}
