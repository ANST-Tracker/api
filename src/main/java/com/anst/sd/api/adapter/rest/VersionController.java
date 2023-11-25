package com.anst.sd.api.adapter.rest;

import com.anst.sd.api.adapter.rest.dto.CurrentVersionResponseDto;
import com.anst.sd.api.app.api.GetPropertyInBound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VersionController {
    private final GetPropertyInBound getPropertyInBound;

    @Operation(
            summary = "Get current version of the application",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Current version received successfully")
            })
    @GetMapping("/version")
    public ResponseEntity<CurrentVersionResponseDto> getCurrentVersion() {
        return ResponseEntity.ok(new CurrentVersionResponseDto(getPropertyInBound.getProperty("version")));
    }
}