
package com.yyc.web.client;

import org.springframework.stereotype.Service;

/**
 * @author leihtg
 * @date 2020/1/16 16:30
 */
@Service
public class DemoFeignClientFallback implements DemoFeignClient {

    @Override
    public String saveRecord() {
        return null;
    }
}
