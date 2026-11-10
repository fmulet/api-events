package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.SpeakerRequestDto;
import com.gestion.eventos.api.dto.SpeakerResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * SpeakerMapper
 */
@Mapper(componentModel = "spring")
public interface SpeakerMapper {
  SpeakerResponseDto toDto(Speaker speaker);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "events", ignore = true)
  Speaker toEntity(SpeakerRequestDto speakerDto);

  List<SpeakerResponseDto> toResponseDtoList(List<Speaker> speakers);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "events", ignore = true)
  void updateSpeakerFromDto(SpeakerRequestDto speakerDto, @MappingTarget Speaker speaker);
}
