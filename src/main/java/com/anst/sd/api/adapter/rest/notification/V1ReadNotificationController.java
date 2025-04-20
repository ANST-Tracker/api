package com.anst.sd.api.adapter.rest.notification;

import com.anst.sd.api.adapter.rest.notification.dto.NotificationDtoMapper;
import com.anst.sd.api.adapter.rest.notification.dto.NotificationInfoDto;
import com.anst.sd.api.app.api.notification.GetNotificationsInBound;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "NotificationController")
@Slf4j
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class V1ReadNotificationController {
    private final GetNotificationsInBound getNotificationsInBound;
    private final JwtService jwtService;
    private final NotificationDtoMapper notificationDtoMapper;

    @Operation(
            summary = "Get notifications",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true
                    )
            }
    )
    @GetMapping
    ResponseEntity<List<NotificationInfoDto>> getNotifications() {
        return ResponseEntity.ok(getNotificationsInBound.getNotifications(jwtService.getJwtAuth().getUserId())
                .stream().map(notificationDtoMapper::mapToDto)
                .toList());
    }
}
