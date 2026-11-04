package com.gestion.eventos.api.service;

import java.util.List;

import com.gestion.eventos.api.domain.Event;

/**
 * IEventService
 */
public interface IEventService {
    List<Event> findAll();

    Event save(Event event);

    Event findById(Long id);

    void deleteById(Long id);
}
