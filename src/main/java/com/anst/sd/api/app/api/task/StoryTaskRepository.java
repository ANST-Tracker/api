package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.StoryTask;

public interface StoryTaskRepository {
    StoryTask getBySimpleId(String simpleId);
}
