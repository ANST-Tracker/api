package com.anst.sd.api.adapter.rest.task.read;

import com.anst.sd.api.app.api.task.GetAvailableStatusesInBound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/dictionary")
@RestController
@RequiredArgsConstructor
public class V1ReadDictionaryController {
    private final GetAvailableStatusesInBound getAvailableStatusesForFullCycle;

    @GetMapping("/statuses/full")
    public Map<String, List<String>> getFullCycleStatuses() {
        return getAvailableStatusesForFullCycle.getAvailableStatusesForFullCycle();
    }

    @GetMapping("/statuses/short")
    public Map<String, List<String>> getShortCycleStatuses() {
        return getAvailableStatusesForFullCycle.getAvailableStatusesForShortCycle();
    }
}
