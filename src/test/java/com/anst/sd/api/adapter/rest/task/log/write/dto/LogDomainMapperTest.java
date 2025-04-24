package com.anst.sd.api.adapter.rest.task.log.write.dto;

import com.anst.sd.api.AbstractUnitTest;
import com.anst.sd.api.adapter.rest.task.log.dto.CreateUpdateLogDto;
import com.anst.sd.api.adapter.rest.task.log.dto.LogDomainMapper;
import com.anst.sd.api.domain.task.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogDomainMapperTest extends AbstractUnitTest {
    private LogDomainMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(LogDomainMapper.class);
    }

    @Test
    void mapToDto() {
        CreateUpdateLogDto dto = readFromFile("/LogDomainMapperTest/logDto.json", CreateUpdateLogDto.class);

        Log log = mapper.mapToDomain(dto);

        assertEquals(dto.getComment(), log.getComment());
        assertEquals(dto.getDate(), log.getDate());
        assertEquals(dto.getTimeEstimation().getAmount(), log.getTimeEstimation().getAmount());
        assertEquals(dto.getTimeEstimation().getTimeUnit(), log.getTimeEstimation().getTimeUnit());
    }
}