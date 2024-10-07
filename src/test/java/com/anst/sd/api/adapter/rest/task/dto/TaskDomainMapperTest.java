package com.anst.sd.api.adapter.rest.task.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.task.write.dto.CreateTaskDto;
import com.anst.sd.api.adapter.rest.task.write.dto.UpdateTaskDto;
import com.anst.sd.api.domain.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class TaskDomainMapperTest extends AbstractUnitTest {
    private TaskDomainMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TaskDomainMapper.class);
    }

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
