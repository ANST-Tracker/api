package com.anst.sd.api.app.impl.security;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class CodeGenerationDelegate {
    private CodeGenerationDelegate() {
    }

    public static String generate() {
        log.info("Generating a random five-digit code");
        SecureRandom random = new SecureRandom();
        int number = random.nextInt(90000) + 10000;
        return Integer.toString(number);
    }
}