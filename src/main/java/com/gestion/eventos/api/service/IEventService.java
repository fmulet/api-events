package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * IEventService
 */
public interface IEventService {
  Page<EventResponseDto> findAll(String name, Pageable pageable);

  Event save(EventRequestDto event);

  Event findById(Long id);

  Event update(Long id, EventRequestDto event);

  void deleteById(Long id);

  List<Event> getAllEventsAndTheirDetailsProblematic();
  List<Event> getAllEventsAndTheirDetailsOptimizedWithJoinFetch();
}
