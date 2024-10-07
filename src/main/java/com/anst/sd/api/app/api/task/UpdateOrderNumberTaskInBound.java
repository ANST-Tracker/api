package com.anst.sd.api.app.api.task;

import com.anst.sd.api.domain.task.Task;

public interface UpdateOrderNumberTaskInBound {
    Task updateOrderNumber(Long userId, Long taskId, double orderNumber);
}
