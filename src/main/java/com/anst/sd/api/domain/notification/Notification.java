package com.anst.sd.api.domain.notification;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@Document(collection = "notification")
public class Notification {
    @MongoId
    private String id;
    @Column
    private Boolean viewed;
    @Column
    private String recipientLogin;
    @Column
    private String recipientTelegramId;
    @Column
    private Instant creationDateTime;
    @Column
    @Enumerated(EnumType.STRING)
    private NotificationTemplate template;
    @Column
    private Map<String, String> params;

    @PrePersist
    public void prePersist() {
        this.setCreationDateTime(Instant.now());
    }
}
