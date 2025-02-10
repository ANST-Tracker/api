package com.anst.sd.api.adapter.rest.task.read;

import com.anst.sd.api.adapter.rest.task.dto.SimpleDictionaryDto;
import com.anst.sd.api.adapter.rest.task.dto.SimpleDictionaryDtoMapper;
import com.anst.sd.api.app.api.task.GetAvailableStatusesInBound;
import com.anst.sd.api.domain.task.SimpleDictionary;
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
@RequestMapping("/dictionaries")
@RequiredArgsConstructor
public class V1ReadDictionariesController {
    private final GetAvailableStatusesInBound getAvailableStatusesInBound;
    private final SimpleDictionaryDtoMapper simpleDictionaryDtoMapper;

    @GetMapping("/{taskId}/appropriate-statuses")
    public List<SimpleDictionaryDto> getAppropriateStatuses(@PathVariable UUID taskId) {
        List<SimpleDictionary> listOfDictionaries = getAvailableStatusesInBound.getAppropriateStatuses(taskId);
        return simpleDictionaryDtoMapper.toDto(listOfDictionaries);
    }
}
