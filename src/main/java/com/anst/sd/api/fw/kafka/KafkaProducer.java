package com.anst.sd.api.fw.kafka;

import com.anst.sd.api.domain.user.UserCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(UserCode userCode) {
        try {
            String message = objectMapper.writeValueAsString(userCode);
            kafkaTemplate.send("tg", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}