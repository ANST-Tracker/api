package com.anst.sd.api.fw.spring;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.anst.sd.api.domain")
public class JpaConfiguration {
}