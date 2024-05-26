package com.anst.sd.api.adapter.rest.task.write;

import com.anst.sd.api.app.api.task.CreateTaskInBound;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/internal/task")
@RestController
@RequiredArgsConstructor
public class V1WriteTaskInternalController {
    private final CreateTaskInBound createTaskInBound;

    @Operation(
            summary = "Create a new task",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Task created successfully",
                            useReturnTypeSchema = true)
            })
    @PostMapping("/{userTelegramId}/{name}")
    public void createTask(@PathVariable String userTelegramId, @PathVariable String name) {
        createTaskInBound.create(userTelegramId, name);
    }
}
