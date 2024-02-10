package com.anst.sd.api.domain.user;

import com.anst.sd.api.domain.DomainObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder(toBuilder = true)
public class UserCode {
    @Id
    private String id;
    @Column(unique = true, nullable = false)
    private String userId;
    @Column(unique = true, nullable = false)
    private String telegramId;
    @Column(nullable = false)
    private String code;
}
