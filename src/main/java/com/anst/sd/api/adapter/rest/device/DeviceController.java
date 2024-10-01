package com.anst.sd.api.adapter.rest.device;

import com.anst.sd.api.adapter.rest.device.dto.DeviceDto;
import com.anst.sd.api.adapter.rest.device.dto.DeviceDtoMapper;
import com.anst.sd.api.adapter.rest.dto.IdResponseDto;
import com.anst.sd.api.app.api.device.DeleteDeviceInbound;
import com.anst.sd.api.app.api.device.DeviceInfo;
import com.anst.sd.api.app.api.device.GetDevicesInbound;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/device")
@RequiredArgsConstructor
public class DeviceController {
  private final JwtService jwtService;
  private final GetDevicesInbound getDevicesInbound;
  private final DeleteDeviceInbound deleteDeviceInbound;
  private final DeviceDtoMapper deviceDtoMapper;

  @Operation(
          summary = "Get list of devices",
          responses = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Devices retrieved successfully",
                          useReturnTypeSchema = true)
          })
  @GetMapping("/list")
  public ResponseEntity<List<DeviceDto>> getDevices() {
    List<DeviceInfo> devices = getDevicesInbound.get(jwtService.getJwtAuth().getUserId());
    return ResponseEntity.ok(devices.stream()
            .map(deviceDtoMapper::mapToDto)
            .toList());
  }

  @Operation(
          summary = "Delete device by ID",
          responses = {
                  @ApiResponse(
                          responseCode = "200",
                          description = "Device deleted successfully",
                          useReturnTypeSchema = true)
          })
  @DeleteMapping("/{id}")
  public ResponseEntity<IdResponseDto> deleteDevice(
          @Parameter(description = "Device ID") @PathVariable Long id) {
    Long deviceId = deleteDeviceInbound.delete(jwtService.getJwtAuth().getUserId(), id);
    return ResponseEntity.ok(new IdResponseDto(deviceId));
  }
}
