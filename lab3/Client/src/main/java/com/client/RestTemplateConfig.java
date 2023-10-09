//package com.client;
//import org.apache.hc.client5.http.classic.HttpClient;
//import org.apache.hc.client5.http.impl.classic.HttpClients;
//import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
//import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
//import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
//import org.apache.hc.core5.ssl.SSLContextBuilder;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.http.client.ClientHttpRequestFactory;
//import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
//import org.springframework.http.converter.StringHttpMessageConverter;
//import org.springframework.web.client.RestTemplate;
//
//import javax.net.ssl.SSLContext;
//import java.io.IOException;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.security.*;
//import java.security.cert.CertificateException;
//
///**
// * @author liyijia
// * @create 2023-10-2023/10/8
// *
// */
//@Configuration
//public class RestTemplateConfig {
//
//    public String keyStore = "gateway.jks";
//    public String keyStoreType = "jks";
//    public String password = "passwd";
//
//    @Bean
//    public RestTemplate restTemplate() {
//        RestTemplate restTemplate = new RestTemplate();
//        KeyStore keyStore;
//        HttpComponentsClientHttpRequestFactory requestFactory = null;
//
//        try {
//            keyStore = KeyStore.getInstance(this.keyStoreType);
//            ClassPathResource classPathResource = new ClassPathResource(this.keyStore);
//            InputStream inputStream = classPathResource.getInputStream();
//            keyStore.load(inputStream, "passwd".toCharArray());
//
//            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
//                    new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy())
//                    .loadKeyMaterial(keyStore, this.password.toCharArray()).build(),
//                    NoopHostnameVerifier.INSTANCE);
//
//            HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
//                    .setMaxConnTotal(Integer.valueOf(5))
//                    .setMaxConnPerRoute(Integer.valueOf(5))
//                    .build();
//
//            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//            requestFactory.setReadTimeout(Integer.valueOf(10000));
//            requestFactory.setConnectTimeout(10000);
//
//            restTemplate.setRequestFactory(requestFactory);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return restTemplate;
//    }
//
//
//}