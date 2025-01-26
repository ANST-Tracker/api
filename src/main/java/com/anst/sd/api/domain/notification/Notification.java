package com.anst.sd.api.domain.notification;

import com.anst.sd.api.domain.DomainObject;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
@Document(collection = "notification")
public class Notification extends DomainObject {
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
