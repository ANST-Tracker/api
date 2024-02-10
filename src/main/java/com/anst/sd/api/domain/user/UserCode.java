package com.anst.sd.api.domain.user;

import com.anst.sd.api.domain.DomainObject;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Entity(name = "user_code")
@Table(name = "user_codes")
public class UserCode extends DomainObject {
    @Column(unique = true, nullable = false)
    private String userId;
    @Column(unique = true, nullable = false)
    private String telegramId;
    @Column(nullable = false)
    private String code;
}
