package com.anst.sd.api.adapter.rest.tag.read;

import com.anst.sd.api.adapter.rest.tag.dto.TagDtoMapper;
import com.anst.sd.api.adapter.rest.tag.dto.TagInfoDto;
import com.anst.sd.api.app.api.tag.GetTagsInBound;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/tag")
@RestController
@RequiredArgsConstructor
public class V1ReadTagController {
    private final JwtService jwtService;
    private final GetTagsInBound getTagsInBound;
    private final TagDtoMapper tagDtoMapper;

    @Operation(
            summary = "Get all tags for the authenticated user",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tags retrieved successfully",
                            useReturnTypeSchema = true)
            })
    @GetMapping("/list")
    public ResponseEntity<List<TagInfoDto>> getTags() {
        List<Tag> tags = getTagsInBound.get(jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(tagDtoMapper.mapToDto(tags));
    }
}
