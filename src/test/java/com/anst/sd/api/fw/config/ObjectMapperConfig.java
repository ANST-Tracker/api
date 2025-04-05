package com.anst.sd.api.fw.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class ObjectMapperConfig {
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void objectMapper() {
        objectMapper.setDefaultPrettyPrinter(new MyDefaultPrettyPrinter());
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    protected static class MyDefaultPrettyPrinter extends DefaultPrettyPrinter {
        private static final long serialVersionUID = 9003705494148916471L;

        public MyDefaultPrettyPrinter() {
            this._objectIndenter = new DefaultIndenter("  ", System.lineSeparator());
            this._arrayIndenter = _objectIndenter;
        }

        @Override
        public DefaultPrettyPrinter createInstance() {
            return new ObjectMapperConfig.MyDefaultPrettyPrinter();
        }

        @Override
        public void writeObjectFieldValueSeparator(JsonGenerator jg) throws IOException {
            jg.writeRaw(": ");
        }
    }
}
