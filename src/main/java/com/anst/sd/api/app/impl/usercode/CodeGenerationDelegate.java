package com.anst.sd.api.app.impl.usercode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class CodeGenerationDelegate {

    public String generate() {
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(90000) + 10000;
        return Integer.toString(number);
    }
}