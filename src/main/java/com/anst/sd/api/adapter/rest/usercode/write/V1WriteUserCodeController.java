package com.anst.sd.api.adapter.rest.usercode.write;

import com.anst.sd.api.app.api.user.GetUserInBound;
import com.anst.sd.api.app.api.usercode.SendMessageProducerInBound;
import com.anst.sd.api.app.api.usercode.SendUserCodeToTelegramInBound;
import com.anst.sd.api.domain.user.User;
import com.anst.sd.api.domain.user.UserCode;
import com.anst.sd.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class V1WriteUserCodeController {
    private final JwtService jwtService;
    private final GetUserInBound getUserInBound;
    private final SendUserCodeToTelegramInBound sendUserCodeToTelegramInBound;
    private final SendMessageProducerInBound sendMessageProducerInBound;

    @PostMapping("/send-code")
    public ResponseEntity<UserCode> send() {
        User user = getUserInBound.get(jwtService.getJwtAuth().getUserId());
        UserCode source = sendUserCodeToTelegramInBound.create(
                String.valueOf(user.getId()),
                String.valueOf(user.getTelegramId()));

        sendMessageProducerInBound.sendMessage(source);
        return ResponseEntity.ok(null);
    }

}
