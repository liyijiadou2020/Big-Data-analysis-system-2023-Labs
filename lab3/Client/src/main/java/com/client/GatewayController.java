package com.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

/**
 * @author liyijia
 * @create 2023-10-2023/10/9
 */

@RestController
@RequestMapping(value="/gateway")
public class GatewayController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    Environment env;

    @RequestMapping(value = "/data", method = RequestMethod.GET)
    public String getData() {
        System.out.println("Returning data from gateway");
        return "Hello from gateway";
    } // https://localhost/gateway/data

    @RequestMapping(value = "/server-data", method = RequestMethod.GET)
    public String getServerData() {
        System.out.println("Returning data from server through gateway");
        try {
            String msEndpoint = env.getProperty("endpoint.server");
            System.out.println("Endpoint name : [" + msEndpoint + "]");
            return restTemplate.getForObject(new URI(Objects.requireNonNull(msEndpoint)), String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "<h1>Exception occurred</h1>";
    }// https://localhost:8080/gateway/server-data

} // https://localhost:8080
