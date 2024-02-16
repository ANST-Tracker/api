package com.anst.sd.api.app.impl.usercode;

import com.anst.sd.api.adapter.MessageConverter;
import com.anst.sd.api.app.api.usercode.SendMessageProducerInBound;
import com.anst.sd.api.domain.user.UserCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class SendMessageProducerDelegate implements SendMessageProducerInBound {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageConverter<UserCode> messageConverter;
    @Value("${spring.kafka.topicName}")
    private String topicName;

    @Override
    public void sendMessage(UserCode clazz) {
        String message = messageConverter.serialize(clazz);
        kafkaTemplate.send(topicName, message);
    }
}