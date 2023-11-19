package com.anst.sd.api.app.impl;

import com.anst.sd.api.app.api.GetPropertyInBound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetPropertyUseCase implements GetPropertyInBound {
    private final Properties properties;

    @Override
    public String getProperty(String propertyName) {
        log.debug("Got info with propertyName {}", propertyName);
        return this.properties.getProperty(propertyName);
    }
}
