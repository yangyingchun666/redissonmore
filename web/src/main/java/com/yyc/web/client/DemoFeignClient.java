
package com.yyc.web.client;

import com.yyc.web.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Description todo
 * @Author hfwang
 * @Date 2019/12/19 20:41
 **/
@FeignClient(value = "demo1", fallback = DemoFeignClientFallback.class, configuration = FeignConfig.class)
public interface DemoFeignClient {

    @GetMapping("/redisson")
    String saveRecord();
}
