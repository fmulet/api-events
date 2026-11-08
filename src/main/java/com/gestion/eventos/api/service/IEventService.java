package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Event;
import java.util.List;

/**
 * IEventService
 */
public interface IEventService {
  List<Event> findAll();

  Event save(Event event);

  Event findById(Long id);

  void deleteById(Long id);
}
