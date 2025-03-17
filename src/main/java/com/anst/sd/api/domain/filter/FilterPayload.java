package com.anst.sd.api.domain.filter;

import com.anst.sd.api.domain.task.TaskPriority;
import com.anst.sd.api.domain.task.TaskStatus;
import com.anst.sd.api.domain.task.TaskType;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FilterPayload {
    private String nameFragment;
    private List<TaskStatus> statuses;
    private List<TaskType> types;
    private List<TaskPriority> priorities;
    private List<Integer> storyPoints;
    private List<UUID> assigneeIds;
    private List<UUID> reviewerIds;
    private List<UUID> creatorIds;
    private List<UUID> tagIds;
}
