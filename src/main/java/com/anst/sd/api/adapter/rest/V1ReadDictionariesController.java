package com.anst.sd.api.adapter.rest;

import com.anst.sd.api.adapter.rest.dto.SimpleDictionaryDto;
import com.anst.sd.api.adapter.rest.task.dto.SimpleDictionaryDtoMapper;
import com.anst.sd.api.app.api.task.GetAvailableStatusesInBound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "DictionaryController")
@Slf4j
@RestController
@RequestMapping("/dictionaries")
@RequiredArgsConstructor
public class V1ReadDictionariesController {
    private final GetAvailableStatusesInBound getAvailableStatusesInBound;
    private final SimpleDictionaryDtoMapper simpleDictionaryDtoMapper;

    @Operation(
            summary = "Get possible next statuses for a task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Next statuses retrieved successfully",
                            useReturnTypeSchema = true
                    )
            }
    )
    @GetMapping("/{simpleId}/appropriate-statuses")
    public List<SimpleDictionaryDto> getAppropriateStatuses(@PathVariable String simpleId) {
        return getAvailableStatusesInBound.getAppropriateStatuses(simpleId)
                .stream()
                .map(simpleDictionaryDtoMapper::toDto)
                .toList();
    }
}