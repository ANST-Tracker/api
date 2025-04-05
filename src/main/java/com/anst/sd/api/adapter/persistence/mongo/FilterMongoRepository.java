package com.anst.sd.api.adapter.persistence.mongo;

import com.anst.sd.api.domain.filter.Filter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FilterMongoRepository extends MongoRepository<Filter, String> {
    Optional<Filter> findByIdAndUserId(String id, UUID userId);

    List<Filter> findByUserIdOrProjectId(UUID userId, UUID projectId);
}
