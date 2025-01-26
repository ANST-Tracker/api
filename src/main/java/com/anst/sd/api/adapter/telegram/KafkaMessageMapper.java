package com.anst.sd.api.adapter.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class KafkaMessageMapper<T> {
    private final ObjectMapper objectMapper;

    public String mapToKafkaMessage(T clazz) {
        try {
            return objectMapper.writeValueAsString(clazz);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize the object, incorrect object data");
            throw new RuntimeException(e.getMessage());
        }
    }
}