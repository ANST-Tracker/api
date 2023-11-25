package com.anst.sd.api.app.impl;

import com.anst.sd.api.app.api.GetPropertyInBound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
public class GetPropertyUseCase implements GetPropertyInBound {
    private final PropertiesReader reader;

    public GetPropertyUseCase(@Value("${pom.propertiesFileName}") String pomPropertiesFile) throws IOException {
        reader = new PropertiesReader(pomPropertiesFile);
    }

    @Override
    public String getProperty(String propertyName) {
        log.debug("Got info with propertyName {}", propertyName);
        return reader.getProperty(propertyName);
    }
}
