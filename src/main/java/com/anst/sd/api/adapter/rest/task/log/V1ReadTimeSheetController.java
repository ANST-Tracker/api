package com.anst.sd.api.adapter.rest.task.log;

import com.anst.sd.api.adapter.rest.task.log.dto.LogDtoMapper;
import com.anst.sd.api.adapter.rest.task.log.dto.TimeSheetDto;
import com.anst.sd.api.app.api.task.log.GetTimeSheetLogsByProjectInBound;
import com.anst.sd.api.app.api.task.log.GetTimeSheetLogsInBound;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Tag(name = "TimeSheetController")
@Slf4j
@RestController
@RequestMapping("/time-sheet")
@RequiredArgsConstructor
public class V1ReadTimeSheetController {
    private final JwtService jwtService;
    private final GetTimeSheetLogsInBound getTimeSheetLogsInBound;
    private final GetTimeSheetLogsByProjectInBound getTimeSheetLogsByProjectInBound;
    private final LogDtoMapper logDtoMapper;

    @Operation(
            summary = "Get time sheet about project",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/project/{projectId}")
    public List<TimeSheetDto> getTimeSheetByProject(
            @PathVariable UUID projectId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
            ) {
        return getTimeSheetLogsByProjectInBound.get(projectId, jwtService.getJwtAuth().getUserId(), startDate, endDate)
                .stream()
                .map(logDtoMapper::mapToDto)
                .toList();
    }

    @Operation(
            summary = "Get time sheet about projects",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/all-projects")
    public List<TimeSheetDto> getTimeSheetAllProjects(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return getTimeSheetLogsInBound.get(jwtService.getJwtAuth().getUserId(), startDate, endDate)
                .stream()
                .map(logDtoMapper::mapToDto)
                .toList();
    }
}
