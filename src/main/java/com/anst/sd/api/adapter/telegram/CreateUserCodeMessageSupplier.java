package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.adapter.MessageConverter;
import com.anst.sd.api.app.api.usercode.CreateUserCodeOutBound;
import com.anst.sd.api.domain.user.UserCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class CreateUserCodeMessageSupplier implements CreateUserCodeOutBound {
    private static final String TOPIC = "TG_SEND_CODE_RQ";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final MessageConverter<UserCode> messageConverter;

    @Override
    public void sendMessage(UserCode userCode) {
        log.info("Object {} trying to send Kafka", userCode);
        String message = messageConverter.serialize(userCode);
        kafkaTemplate.send(TOPIC, message);
    }
}