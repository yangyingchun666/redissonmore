package com.yyc.web.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Copyright (C), 2019,
 * @FileName: FeignConfig
 * @Author: Dayee xieZW
 * @Date: 2019/11/26 10:36
 * @Description:
 * @History:
 */
@Configuration
public class FeignConfig {

    /**
     * - NONE：不记录任何日志信息，这是默认值。 - BASIC：仅记录请求的方法，URL以及响应状态码和执行时间 - HEADERS：在BASIC的基础上，额外记录了请求和响应的头信息 -
     * FULL：记录所有请求和响应的明细，包括头信息、请求体、元数据。
     *
     * @return
     */
    @Bean
    Logger.Level feignLoggerLevel() {

        return Logger.Level.FULL;
    }
}
