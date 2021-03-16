package com.yyc.lock_demo.controller;

import com.yyc.lock_demo.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: aqs
 * @description:
 * @author: Anakin Yang
 * @create: 2021-01-19 20:35
 **/
@RestController
@RequestMapping("/")
public class DemoController {
    @Autowired
    DemoService demoService;


    @RequestMapping("index")
    public String index() {
        return "Hello";
    }


    @RequestMapping("descStockNoLock")
    public String descStockNoLock() {
        String s = demoService.decStockNoLock();
        return s;
    }

    @RequestMapping("descStockWithLock")
    public String descStockWithLock() {
        String s = demoService.decStockWithLock();
        return s;
    }

    @RequestMapping("decStockReentrantLock")
    public String decStockReentrantLock() {
        String s = demoService.decStockReentrantLock();
        return s;
    }

    @RequestMapping("redisson")
    public String redisson() {
        demoService.redisson();
        return "demo1";
    }
}
