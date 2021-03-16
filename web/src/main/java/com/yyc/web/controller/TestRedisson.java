package com.yyc.web.controller;

import com.yyc.web.client.DemoFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: redissonmore
 * @description:
 * @author: Anakin Yang
 * @create: 2021-03-17 00:29
 **/
@RestController
@RequestMapping("/")
public class TestRedisson {

    @Autowired
    DemoFeignClient demoFeignClient;


    @GetMapping("testRedisson")
    public String testFeign(){
        String s = demoFeignClient.saveRecord();
        return "ok";
    }
}
