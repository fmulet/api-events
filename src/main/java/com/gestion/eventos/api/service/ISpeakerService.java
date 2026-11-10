package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.SpeakerRequestDto;
import java.util.List;

/**
 * SpeakerService
 */
public interface ISpeakerService {
  List<Speaker> findAll();

  Speaker save(SpeakerRequestDto speaker);

  Speaker findById(Long id);

  Speaker update(Long id, SpeakerRequestDto speakerRequestDto);

  void deleteById(Long id);
}
