package com.anst.sd.api.adapter.rest.task.read;

import com.anst.sd.api.adapter.rest.task.dto.TaskFilterDomainMapper;
import com.anst.sd.api.adapter.rest.task.dto.TaskFilterDto;
import com.anst.sd.api.adapter.rest.task.dto.TaskRegistryDto;
import com.anst.sd.api.adapter.rest.task.dto.TaskRegistryDtoMapper;
import com.anst.sd.api.app.api.filter.FilterValidationException;
import com.anst.sd.api.app.api.task.FindTasksByFilterInbound;
import com.anst.sd.api.domain.filter.Filter;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/task")
public class ReadTaskController {
    private final TaskRegistryDtoMapper taskRegistryDtoMapper;
    private final JwtService jwtService;
    private final FindTasksByFilterInbound findTasksByFilterInbound;
    private final TaskFilterDomainMapper taskFilterDomainMapper;

    @Operation(
        summary = "Find tasks by filter",
        responses = {
            @ApiResponse(
                responseCode = "200",
                useReturnTypeSchema = true)
        })
    @PostMapping("/find-by-filter")
    public ResponseEntity<List<TaskRegistryDto>> findByFilter(
        @Valid @RequestBody TaskFilterDto taskFilterDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new FilterValidationException();
        }
        Filter filter = taskFilterDomainMapper.mapToDomain(taskFilterDto);
        filter.setUserId(jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(findTasksByFilterInbound.find(filter).stream()
            .map(taskRegistryDtoMapper::mapToDto)
            .toList()
        );
    }
}
