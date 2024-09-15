package com.anst.sd.api.adapter.rest.device.dto;

import com.anst.sd.api.app.api.device.DeviceInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeviceDtoMapper {
    DeviceDto mapToDto(DeviceInfo deviceInfo);
}
