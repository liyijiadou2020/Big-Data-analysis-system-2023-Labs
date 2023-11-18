package com.lab4.controller;
import com.lab4.dto.Student;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author liyijia
 * @create 2023-11-2023/11/18
 */

@RestController
public class StudentController {

    @RequestMapping(value = "/data/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Student findById(
            @PathVariable long id, HttpServletRequest request, HttpServletResponse response)
    {
        // 获取请求头 "Test" 的值
        String testHeader = request.getHeader("Test");

        // 如果 "Test" 请求头存在，将它的值添加到响应头
        if (testHeader != null){
            response.addHeader("Test", testHeader);
        }

        return new Student(id, RandomStringUtils.randomAlphanumeric(10));
    }

}
