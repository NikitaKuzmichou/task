package com.nikita.kuzmichou.task.entities.value.dto;

import com.nikita.kuzmichou.task.entities.value.Value;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ValueMapper {

    ValueDto toValueDto(Value value);

    Value toValue(ValueDto valueDto);
}
