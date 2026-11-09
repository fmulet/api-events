package com.gestion.eventos.api.mapper;

import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.SpeakerDto;
import org.mapstruct.Mapper;

/**
 * SpeakerMapper
 */
@Mapper(componentModel = "spring")
public interface SpeakerMapper {
  SpeakerDto toDto(Speaker speaker);

  Speaker toEntity(SpeakerDto speakerDto);
}
