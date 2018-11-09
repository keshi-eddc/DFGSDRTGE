package com.eddc.weixinlink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.eddc.weixinlink.dao")
public class WeixinLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeixinLinkApplication.class, args);
    }
}
