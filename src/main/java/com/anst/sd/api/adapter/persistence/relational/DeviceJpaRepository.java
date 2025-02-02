package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.security.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeviceJpaRepository extends JpaRepository<Device, UUID> {
}
