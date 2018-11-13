package com.eddc.weixinlink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.eddc.weixinlink.dao")
@EnableScheduling
public class WeixinLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinLinkApplication.class, args);
    }
}
