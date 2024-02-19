package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.adapter.MessageConverter;
import com.anst.sd.api.app.api.usercode.CreateUserCodeOutBound;
import com.anst.sd.api.domain.user.UserCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class CreateUserCodeMessageSupplier implements CreateUserCodeOutBound {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageConverter<UserCode> messageConverter;
    private static final String TOPIC = "TG_SEND_CODE_RQ";

    @Override
    public void sendMessage(UserCode clazz) {
        log.info("Object UserCode trying to send Kafka");
        String message = messageConverter.serialize(clazz);
        kafkaTemplate.send(TOPIC, message);
    }
}