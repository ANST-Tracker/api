package com.anst.sd.api.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.concurrent.TimeUnit;

@Converter(autoApply = true)
public class TimeUnitConverter implements AttributeConverter<TimeUnit, String> {
    @Override
    public String convertToDatabaseColumn(TimeUnit attribute) {
        return attribute != null ? attribute.name() : null;
    }

    @Override
    public TimeUnit convertToEntityAttribute(String dbData) {
        return dbData != null ? TimeUnit.valueOf(dbData) : null;
    }
}
