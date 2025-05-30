package com.anst.sd.api.adapter.rest.tag.write;

import com.anst.sd.api.adapter.rest.dto.IdResponseDto;
import com.anst.sd.api.adapter.rest.tag.dto.TagDtoMapper;
import com.anst.sd.api.adapter.rest.tag.dto.TagInfoDto;
import com.anst.sd.api.adapter.rest.tag.write.dto.CreateTagDto;
import com.anst.sd.api.adapter.rest.tag.write.dto.TagDomainMapper;
import com.anst.sd.api.app.api.tag.CreateTagInBound;
import com.anst.sd.api.app.api.tag.DeleteTagInBound;
import com.anst.sd.api.app.api.tag.TagValidationException;
import com.anst.sd.api.domain.tag.Tag;
import com.anst.sd.api.security.app.impl.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@io.swagger.v3.oas.annotations.tags.Tag(name = "TagController")
@Slf4j
@RequestMapping("/tag")
@RestController
@RequiredArgsConstructor
public class V1WriteTagController {
    private final CreateTagInBound createTagInBound;
    private final DeleteTagInBound deleteTagInBound;
    private final JwtService jwtService;
    private final TagDomainMapper tagDomainMapper;
    private final TagDtoMapper tagDtoMapper;

    @Operation(
            summary = "Create a new tag",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tag created successfully",
                            useReturnTypeSchema = true)
            })
    @PostMapping
    public ResponseEntity<TagInfoDto> createTag(
            @Valid @RequestBody CreateTagDto request,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new TagValidationException();
        }
        Tag tag = tagDomainMapper.mapToDomain(request);
        Tag result = createTagInBound.create(tag, jwtService.getJwtAuth().getUserId());
        return ResponseEntity.ok(tagDtoMapper.mapToDto(result));
    }

    @Operation(
            summary = "Delete a tag",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Tag deleted successfully",
                            useReturnTypeSchema = true)
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<IdResponseDto> deleteTag(@Parameter(description = "Tag ID") @PathVariable UUID id) {
        Tag tag = deleteTagInBound.delete(jwtService.getJwtAuth().getUserId(), id);
        return ResponseEntity.ok(new IdResponseDto(tag.getId()));
    }
}
