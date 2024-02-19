package com.anst.sd.api.app.impl.usercode;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
@Log4j2
public class CodeGenerationDelegate {

    public String generate() {
        log.info("Generating a random five-digit code");
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(90000) + 10000;
        return Integer.toString(number);
    }
}