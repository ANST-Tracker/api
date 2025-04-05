package com.anst.sd.api.adapter.rest.task.comments;

import com.anst.sd.api.adapter.rest.task.comments.dto.CommentDtoMapper;
import com.anst.sd.api.adapter.rest.task.comments.dto.CommentInfoDto;
import com.anst.sd.api.app.api.task.comment.GetCommentsInbound;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/project/{projectId}/task/{simpleId}/comments")
@RequiredArgsConstructor
public class V1ReadCommentsController {
    private final GetCommentsInbound getCommentsInbound;
    private final JwtService jwtService;
    private final CommentDtoMapper commentDtoMapper;

    @Operation(summary = "Get all comments")
    @GetMapping
    public List<CommentInfoDto> getAll(@PathVariable UUID projectId, @PathVariable String simpleId) {
        return getCommentsInbound.get(jwtService.getJwtAuth().getUserId(), projectId, simpleId).stream()
                .map(commentDtoMapper::mapToDto)
                .toList();
    }
}
