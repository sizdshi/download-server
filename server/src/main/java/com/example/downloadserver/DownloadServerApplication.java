package com.example.downloadserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.example.downloadserver.mapper")
public class DownloadServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DownloadServerApplication.class, args);
	}

}
