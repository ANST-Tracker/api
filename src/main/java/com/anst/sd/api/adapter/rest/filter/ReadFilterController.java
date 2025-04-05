package com.anst.sd.api.adapter.rest.filter;

import com.anst.sd.api.adapter.rest.filter.dto.FilterDto;
import com.anst.sd.api.adapter.rest.filter.dto.FilterDtoMapper;
import com.anst.sd.api.app.api.filter.GetFiltersInbound;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class ReadFilterController {
    private final FilterDtoMapper filterDtoMapper;
    private final GetFiltersInbound getFiltersInbound;
    private final JwtService jwtService;

    @Operation(
        summary = "Get filters",
        responses = {
            @ApiResponse(
                responseCode = "200",
                useReturnTypeSchema = true)
        })
    @GetMapping("/all")
    public ResponseEntity<List<FilterDto>> getFilters() {
        return ResponseEntity.ok(getFiltersInbound.get(jwtService.getJwtAuth().getUserId(), null).stream()
            .map(filterDtoMapper::mapToDto)
            .toList());
    }

    @Operation(
        summary = "Get filters by project",
        responses = {
            @ApiResponse(
                responseCode = "200",
                useReturnTypeSchema = true)
        })
    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<List<FilterDto>> getFiltersByProject(@PathVariable UUID projectId) {
        return ResponseEntity.ok(getFiltersInbound.get(jwtService.getJwtAuth().getUserId(), projectId).stream()
            .map(filterDtoMapper::mapToDto)
            .toList());
    }
}
