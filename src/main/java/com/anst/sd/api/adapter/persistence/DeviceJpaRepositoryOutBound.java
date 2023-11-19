package com.anst.sd.api.adapter.persistence;

import com.anst.sd.api.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeviceJpaRepositoryOutBound extends JpaRepository<Device, Long> {
    Optional<Device> findByDeviceToken(UUID token);
}
