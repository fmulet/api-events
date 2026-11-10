package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
import com.gestion.eventos.api.repository.EventRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * EventService
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {

  private final EventRepository eventRepository;

  @Override
  @Transactional(readOnly = true)
  public List<Event> findAll() {
    return eventRepository.findAll();
  }

  @Override
  @Transactional
  public Event save(Event event) {
    return eventRepository.save(event);
  }

  @Override
  @Transactional(readOnly = true)
  public Event findById(Long id) {
    return eventRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Event eventToDelete = this.findById(id);
    eventRepository.delete(eventToDelete);
  }
}
