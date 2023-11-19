package com.anst.sd.api.adapter.rest.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponseDto {
    private String accessToken;
    private String refreshToken;
}