package com.anst.sd.api.domain.user;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder(toBuilder = true)
@Accessors(chain = true)
public class UserCode {
    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String userId;
    @Column(unique = true, nullable = false)
    private String telegramId;
    @Column(nullable = false)
    private String code;

    @Override
    public String toString() {
        return "UserCode{" +
                "userId='" + userId + '\'' +
                ", telegramId='" + telegramId + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
