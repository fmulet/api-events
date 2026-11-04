package com.gestion.eventos.api.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;

/**
 * EventMapper
 */
@Mapper(componentModel = "spring")
public interface EventMapper {
    @Mapping(target = "id", ignore = true)
    Event toEntity(EventRequestDto eventRequestDto);

    EventResponseDto toResponseDto(Event event);

    List<EventResponseDto> toEventResponseDtoList(List<Event> events);

    @Mapping(target = "id", ignore = true)
    void updateEventFromDto(EventRequestDto eventRequestDto, @MappingTarget Event event);
}
