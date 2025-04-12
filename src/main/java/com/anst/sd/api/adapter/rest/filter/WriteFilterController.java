package com.anst.sd.api.adapter.rest.filter;

import com.anst.sd.api.adapter.rest.dto.StringIdResponseDto;
import com.anst.sd.api.adapter.rest.filter.dto.CreateFilterDto;
import com.anst.sd.api.adapter.rest.filter.dto.FilterDomainMapper;
import com.anst.sd.api.adapter.rest.filter.dto.UpdateFilterDto;
import com.anst.sd.api.app.api.filter.CreateFilterInbound;
import com.anst.sd.api.app.api.filter.DeleteFilterInbound;
import com.anst.sd.api.app.api.filter.FilterValidationException;
import com.anst.sd.api.app.api.filter.UpdateFilterInbound;
import com.anst.sd.api.domain.filter.Filter;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "FilterController")
@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class WriteFilterController {
    private final JwtService jwtService;
    private final CreateFilterInbound createFilterInbound;
    private final UpdateFilterInbound updateFilterInbound;
    private final DeleteFilterInbound deleteFilterInbound;
    private final FilterDomainMapper filterDomainMapper;

    @Operation(
        summary = "Create filter",
        responses = {
            @ApiResponse(
                responseCode = "200",
                useReturnTypeSchema = true)
        })
    @PostMapping
    public ResponseEntity<StringIdResponseDto> createFilter(
        @Valid @RequestBody CreateFilterDto request,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FilterValidationException();
        }
        Filter filter = filterDomainMapper.mapToDomain(request);
        Filter result = createFilterInbound.create(filter, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new StringIdResponseDto(result.getId()));
    }

    @Operation(
        summary = "Update filter",
        responses = {
            @ApiResponse(
                responseCode = "200",
                useReturnTypeSchema = true)
        })
    @PutMapping("/{id}")
    public ResponseEntity<StringIdResponseDto> updateFilter(
        @PathVariable String id,
        @Valid @RequestBody UpdateFilterDto request,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FilterValidationException(id);
        }
        Filter filter = filterDomainMapper.mapToDomain(request);
        Filter result = updateFilterInbound.update(id, filter, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new StringIdResponseDto(result.getId()));
    }

    @Operation(
        summary = "Delete filter",
        responses = {
            @ApiResponse(
                responseCode = "200",
                useReturnTypeSchema = true)
        })
    @DeleteMapping("/{id}")
    public ResponseEntity<StringIdResponseDto> deleteFilter(@PathVariable String id) {
        Filter result = deleteFilterInbound.delete(id, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new StringIdResponseDto(result.getId()));
    }
}
