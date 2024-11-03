package com.anst.sd.api.fw.spring;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    @Bean
    public ObjectMapper objectMapper() {
        var objectMapper = new ObjectMapper();
        addLocalDateTimeModule(objectMapper);
        addLocalDateModule(objectMapper);
        addInstantModule(objectMapper);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper.coercionConfigDefaults().setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
        return objectMapper;
    }

    private void addLocalDateTimeModule(ObjectMapper objectMapper) {
        SimpleModule localDateTimeSerialization = new SimpleModule();
        localDateTimeSerialization.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
        localDateTimeSerialization.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        objectMapper.registerModule(localDateTimeSerialization);
    }

    private void addInstantModule(ObjectMapper objectMapper) {
        SimpleModule instantSerialization = new SimpleModule();
        instantSerialization.addSerializer(Instant.class, new InstantSerializer());
        instantSerialization.addDeserializer(Instant.class, new InstantDeserializer());
        objectMapper.registerModule(instantSerialization);
    }

    private void addLocalDateModule(ObjectMapper objectMapper) {
        SimpleModule localDateSerialization = new SimpleModule();
        localDateSerialization.addSerializer(LocalDate.class, new LocalDateSerializer());
        localDateSerialization.addDeserializer(LocalDate.class, new LocalDateDeserializer());
        objectMapper.registerModule(localDateSerialization);
        objectMapper.registerModule(new SimpleModule()
                .addKeyDeserializer(LocalDate.class, new LocalDateKeyDeserializer()));
    }

    static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);

        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            gen.writeString(value.format(fmt));
        }
    }

    static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(DATE_FORMAT);

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext context) throws IOException {
            return LocalDateTime.parse(p.getValueAsString(), fmt);
        }
    }

    static class InstantSerializer extends JsonSerializer<Instant> {
        @Override
        public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(String.valueOf(value.toEpochMilli()));
        }
    }

    static class InstantDeserializer extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser p, DeserializationContext context) throws IOException {
            return Instant.ofEpochMilli(p.getLongValue());
        }
    }

    static class LocalDateSerializer extends JsonSerializer<LocalDate> {
        private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);

        @Override
        public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.format(fmt));
        }
    }

    static class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
        private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);

        @Override
        public LocalDate deserialize(JsonParser p, DeserializationContext context) throws IOException {
            return LocalDate.parse(p.getValueAsString(), fmt);
        }
    }

    static class LocalDateKeyDeserializer extends KeyDeserializer {
        private static final DateTimeFormatter LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT);

        @Override
        public LocalDate deserializeKey(String key, DeserializationContext context) {
            return LocalDate.parse(key, LOCAL_DATE_FORMATTER);
        }
    }
}