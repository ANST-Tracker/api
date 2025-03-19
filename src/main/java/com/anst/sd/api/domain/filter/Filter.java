package com.anst.sd.api.domain.filter;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

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
    private String name;
    @Column
    @Indexed
    private UUID projectId;
    @Column
    @Indexed
    private UUID userId;
    @Column
    private FilterPayload payload;
}
