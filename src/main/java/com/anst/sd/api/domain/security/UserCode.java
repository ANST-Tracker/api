package com.anst.sd.api.domain.security;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "user_code")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Accessors(chain = true)
public class UserCode {
    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String telegramId;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private Instant expirationTime;

    public UserCode(String telegramId) {
        this.telegramId = telegramId;
    }
}
