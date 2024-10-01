package com.anst.sd.api.adapter.persistence.relational;

import com.anst.sd.api.domain.security.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceJpaRepository extends JpaRepository<Device, Long> {
  List<Device> findAllByUserId(Long userId);

  Optional<Device> findFirstByIdAndUserId(Long id, Long userId);
}
