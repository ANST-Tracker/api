package com.anst.sd.api.adapter.telegram;

import com.anst.sd.api.adapter.KafkaMessageMapper;
import com.anst.sd.api.adapter.telegram.dto.CreateUserCodeDto;
import com.anst.sd.api.adapter.telegram.dto.CreateUserCodeDtoMapper;
import com.anst.sd.api.app.api.security.SendCodeOutbound;
import com.anst.sd.api.domain.security.UserCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class CreateUserCodeMessageSupplier implements SendCodeOutbound {
    private static final String TOPIC = "TG_SEND_CODE_RQ";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaMessageMapper<CreateUserCodeDto> kafkaMessageMapper;
    private final CreateUserCodeDtoMapper createUserCodeDtoMapper;

    @Override
    public void send(UserCode userCode) {
        log.info("Sending code {} to telegramId {}", userCode.getCode(), userCode.getTelegramId());
        CreateUserCodeDto dto = createUserCodeDtoMapper.mapToDto(userCode);
        kafkaTemplate.send(TOPIC, kafkaMessageMapper.mapToKafkaMessage(dto));
    }
}