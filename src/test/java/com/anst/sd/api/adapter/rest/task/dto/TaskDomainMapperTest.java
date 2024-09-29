package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.domain.task.Task;
import org.junit.jupiter.api.Test;

class TaskDomainMapperTest extends AbstractUnitTest {
    private TaskDomainMapper mapper = new TaskDomainMapperImpl_();

    @Test
    void mapToDomain__createTaskDto() {
        CreateTaskDto source = readFromFile("/TaskDomainMapperTest/task.json", CreateTaskDto.class);

        Task domain = mapper.mapToDomain(source);

        assertEqualsToFile("/TaskDomainMapperTest/taskDomain.json", domain);
    }

    @Test
    void mapToDomain__updateTaskDto() {
        UpdateTaskDto source = readFromFile("/TaskDomainMapperTest/task.json", UpdateTaskDto.class);

        Task domain = mapper.mapToDomain(source);

        assertEqualsToFile("/TaskDomainMapperTest/taskDomain.json", domain);
    }
}
