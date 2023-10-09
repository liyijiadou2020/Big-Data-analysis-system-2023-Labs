package com.example.springboot_04;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liyijia
 * @create 2023-10-2023/10/8
 */

@RestController
@RequestMapping("/server")
public class ServerController {

    @RequestMapping( value = "/data", method = RequestMethod.GET)
    public String getData(){
        String string = "<h1>Returning data from sever.</h1>";
        System.out.println(string);
        return string;
    } // https://localhost/server/data to visit

}
