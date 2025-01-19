package com.anst.sd.api.domain.filter;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "filter")
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Accessors(chain = true)
public class Filter {
    @Id
    private String id;
    @Column
    @Indexed
    private String projectId;
    @Column
    @Indexed
    private String userId;
    @Column
    @JdbcTypeCode(SqlTypes.JSON)
    private FilterPayload payload;
}
