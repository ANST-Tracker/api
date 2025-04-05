package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.filter.Filter;
import com.anst.sd.api.domain.task.AbstractTask;

import java.util.List;

public interface FindTasksByFilterInbound {
    List<AbstractTask> find(Filter filter);
}
