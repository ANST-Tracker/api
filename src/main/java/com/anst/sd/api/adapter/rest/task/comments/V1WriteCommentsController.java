package com.anst.sd.api.adapter.rest.task.comments;

import com.anst.sd.api.adapter.rest.dto.IdResponseDto;
import com.anst.sd.api.adapter.rest.task.comments.dto.CreateUpdateCommentDto;
import com.anst.sd.api.app.api.task.comment.CommentValidationException;
import com.anst.sd.api.app.api.task.comment.CreateCommentInbound;
import com.anst.sd.api.app.api.task.comment.DeleteCommentInbound;
import com.anst.sd.api.app.api.task.comment.UpdateCommentInbound;
import com.anst.sd.api.domain.task.Comment;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "CommentController")
@Slf4j
@RestController
@RequestMapping("project/{projectId}/task/{simpleId}/comments")
@RequiredArgsConstructor
public class V1WriteCommentsController {
    private final JwtService jwtService;
    private final CreateCommentInbound createCommentInbound;
    private final UpdateCommentInbound updateCommentInbound;
    private final DeleteCommentInbound deleteCommentInbound;

    @Operation(
            summary = "Create a new comment",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            useReturnTypeSchema = true)
            })
    @PostMapping
    public ResponseEntity<IdResponseDto> create(@PathVariable UUID projectId, @PathVariable String simpleId,
                                                @Valid @RequestBody CreateUpdateCommentDto request,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CommentValidationException();
        }
        Comment comment = createCommentInbound.create(request.getContent(), projectId,
                simpleId, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new IdResponseDto(comment.getId()));
    }

    @Operation(summary = "Update an existing comment")
    @PutMapping("/{id}")
    public ResponseEntity<IdResponseDto> update(@PathVariable UUID projectId, @PathVariable String simpleId,
                                                @PathVariable UUID id,
                                                @Valid @RequestBody CreateUpdateCommentDto request,
                                                BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CommentValidationException(simpleId);
        }
        Comment comment = updateCommentInbound.update(id ,request.getContent(), projectId,
                simpleId, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new IdResponseDto(comment.getId()));
    }

    @Operation(summary = "Delete an existing comment")
    @DeleteMapping("/{id}")
    public ResponseEntity<IdResponseDto> update(@PathVariable UUID projectId, @PathVariable String simpleId,
                                                @PathVariable UUID id
    ) {
        Comment comment = deleteCommentInbound.delete(id, projectId, simpleId, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(new IdResponseDto(comment.getId()));
    }
}
