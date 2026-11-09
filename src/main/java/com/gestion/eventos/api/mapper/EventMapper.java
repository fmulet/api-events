package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.dto.EventSummaryDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * EventMapper
 */
@Mapper(componentModel = "spring")
public interface EventMapper {
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "speakers", ignore = true)
  @Mapping(target = "attendees", ignore = true)
  Event toEntity(EventRequestDto eventRequestDto);

  EventResponseDto toResponseDto(Event event);

  List<EventResponseDto> toEventResponseDtoList(List<Event> events);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "category", ignore = true)
  @Mapping(target = "speakers", ignore = true)
  @Mapping(target = "attendees", ignore = true)
  void updateEventFromDto(
    EventRequestDto eventRequestDto,
    @MappingTarget Event event
  );

  EventSummaryDto toSummaryDto(Event event);
  List<EventSummaryDto> toSummaryDtoList(List<Event> events);
}
