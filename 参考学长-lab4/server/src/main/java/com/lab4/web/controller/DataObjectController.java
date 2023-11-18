package com.lab4.web.controller;

import com.lab4.web.dto.DataObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class DataObjectController {
    @RequestMapping(value = "/data/{id}", method = RequestMethod.GET)
    @ResponseBody
    public DataObject findById(
            @PathVariable long id, HttpServletRequest req, HttpServletResponse res)
    {
        if (req.getHeader("Test") != null) {
            res.addHeader("Test", req.getHeader("Test"));
        }
        return new DataObject(id, RandomStringUtils.randomAlphanumeric(10));
    }
}
