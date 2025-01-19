package com.anst.sd.api.fw.spring;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {
    "com.anst.sd.api.adapter.*"
})
public class FeignConfiguration {
}
