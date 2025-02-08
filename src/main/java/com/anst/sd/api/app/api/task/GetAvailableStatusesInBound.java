package com.anst.sd.api.app.api.task;

import java.util.List;
import java.util.Map;

public interface GetAvailableStatusesInBound {
    Map<String, List<String>> getAvailableStatusesForFullCycle();
    Map<String, List<String>> getAvailableStatusesForShortCycle();
}
