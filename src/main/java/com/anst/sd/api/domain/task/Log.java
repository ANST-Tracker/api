package com.anst.sd.api.domain.task;

import com.anst.sd.api.domain.BusinessEntity;
import com.anst.sd.api.domain.TimeEstimation;
import com.anst.sd.api.domain.user.User;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "logs")
@Getter
@Setter
public class Log extends BusinessEntity {
    @Column
    private String comment;
    @ManyToOne(optional = false)
    @JoinColumn(name = "task_id")
    private AbstractTask task;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @Type(JsonBinaryType.class)
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "time_estimation", columnDefinition = "jsonb")
    private TimeEstimation timeEstimation;
}
