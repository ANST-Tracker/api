package com.anst.sd.api.adapter.rest.task;

import com.anst.sd.api.adapter.rest.task.dto.TaskFilterDto;
import com.anst.sd.api.adapter.rest.task.dto.TaskRegistryDto;
import com.anst.sd.api.adapter.rest.task.dto.TaskRegistryDtoMapper;
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
            throw new RuntimeException();
        }
        return ResponseEntity.ok(List.of());
    }
}
