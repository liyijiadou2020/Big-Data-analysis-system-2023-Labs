package com.lab4.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @REFERENCE：https://blog.51cto.com/lscl/5959675
 * https://blog.csdn.net/weixin_41607429/article/details/124073165
 *
 */
@EnableZuulProxy   // 开启 Zuul 网关代理
@SpringBootApplication
public class MyZuulGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyZuulGatewayApplication.class, args);
    }

}

// 访问：http://localhost:8080