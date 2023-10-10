package com.client;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustSelfSignedStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
//import java.net.http.HttpClient;
import java.security.*;
import java.security.cert.CertificateException;

@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClientApplication.class, args);
    }


    @Bean
    public RestTemplate getRestTemplate() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException {
        RestTemplate restTemplate = new RestTemplate();

        HttpComponentsClientHttpRequestFactory requestFactory = null;

        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();

        try {
            CloseableHttpClient httpClient = httpClientBuilder
                    .setConnectionManager(getHttpClientConnectionManager())
                    .build();
//        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(new SSLContextBuilder()
//                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
//                .loadKeyMaterial(keyStore, "password".toCharArray()).build(),
//                NoopHostnameVerifier.INSTANCE);
//
//        HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
//                .setMaxConnTotal(Integer.valueOf(5))
//                .setMaxConnPerRoute(Integer.valueOf(5))
//                .build();

        requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
//            requestFactory.setReadTimeout(Integer.valueOf(10000));
        requestFactory.setConnectTimeout(Integer.valueOf(10000));

        restTemplate.setRequestFactory(requestFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restTemplate;
    }

    private static HttpClientConnectionManager getHttpClientConnectionManager() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, UnrecoverableKeyException, CertificateException, IOException {
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setMaxConnTotal(Integer.valueOf(5))
                .setMaxConnPerRoute(Integer.valueOf(5))
                .setSSLSocketFactory(getSslConnectionSocketFactory())
                .build();
    }

    /**
     * 支持SSL
     *
     * @return SSLConnectionSocketFactory
     */
    private static SSLConnectionSocketFactory getSslConnectionSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, CertificateException, UnrecoverableKeyException {
//        TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
        KeyStore keyStore = KeyStore.getInstance("jks");
        ClassPathResource classPathResource = new ClassPathResource("gateway.jks");
        InputStream inputStream = classPathResource.getInputStream();
        keyStore.load(inputStream, "password".toCharArray());
        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .loadKeyMaterial(keyStore, "password".toCharArray())
                .build();
        return new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
    }

}
