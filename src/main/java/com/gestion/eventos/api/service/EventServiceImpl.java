package com.gestion.eventos.api.service;

import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
import com.gestion.eventos.api.mapper.EventMapper;
import com.gestion.eventos.api.repository.EventRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * EventService
 */
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements IEventService {

  private final EventRepository eventRepository;
  private final EventMapper eventMapper;
  private final CategoryServiceImpl categoryService;
  private final SpeakerServiceImpl speakerServiceImpl;

  @Override
  @Transactional(readOnly = true)
  public Page<EventResponseDto> findAll(String name, Pageable pageable) {
    Page<Event> eventsPage;
    if (name != null && !name.trim().isEmpty()) {
      eventsPage = eventRepository.findByNameContainingIgnoreCase(name, pageable);
    } else {
      eventsPage = eventRepository.findAll(pageable);
    }
    List<EventResponseDto> dtos = eventsPage
      .getContent()
      .stream()
      .map(eventMapper::toResponseDto)
      .toList();
    return new PageImpl<>(dtos, pageable, eventsPage.getTotalElements());
  }

  @Override
  @Transactional
  public Event save(final EventRequestDto event) {
    final Event eventToSave = eventMapper.toEntity(event);
    eventToSave.setCategory(categoryService.findById(event.getCategoryId()));
    if (event.getSpeakersIds() != null && !event.getSpeakersIds().isEmpty()) {
      final Set<Speaker> speakers = event
        .getSpeakersIds()
        .stream()
        .map(speakerServiceImpl::findById)
        .collect(Collectors.toSet());
      speakers.forEach(eventToSave::addSpeaker);
    }
    return eventRepository.save(eventToSave);
  }

  @Override
  @Transactional(readOnly = true)
  public Event findById(final Long id) {
    return eventRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
  }

  @Override
  @Transactional
  public void deleteById(final Long id) {
    final Event eventToDelete = this.findById(id);
    eventRepository.delete(eventToDelete);
  }

  @Override
  public Event update(final Long id, final EventRequestDto event) {
    final Event eventToUpdate = eventRepository
      .findById(id)
      .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    eventMapper.updateEventFromDto(event, eventToUpdate);

    if (!eventToUpdate.getCategory().getId().equals(event.getCategoryId())) {
      eventToUpdate.setCategory(categoryService.findById(event.getCategoryId()));
    }

    Set<Speaker> updatedSpeakers;

    if (event.getSpeakersIds() != null && !event.getSpeakersIds().isEmpty()) {
      updatedSpeakers = event
        .getSpeakersIds()
        .stream()
        .map(speakerServiceImpl::findById)
        .collect(Collectors.toSet());
    } else {
      updatedSpeakers = new HashSet<>();
    }
    new HashSet<>(eventToUpdate.getSpeakers()).forEach(currentSpeaker -> {
      if (!updatedSpeakers.contains(currentSpeaker)) {
        eventToUpdate.removeSpeaker(currentSpeaker);
      }
    });
    updatedSpeakers.forEach(newSpeaker -> {
      if (!eventToUpdate.getSpeakers().contains(newSpeaker)) {
        eventToUpdate.addSpeaker(newSpeaker);
      }
    });

    return eventRepository.save(eventToUpdate);
  }

  @Transactional(readOnly = true)
  public List<Event> getAllEventsAndTheirDetailsProblematic() {
    List<Event> events = eventRepository.findAll();
    events.forEach(event -> {
      event.getSpeakers().size();
      event.getSpeakers().stream().map(Speaker::getName).collect(Collectors.toSet());
      event.getCategory().getName();
      event.getAttendees().size();
    });
    return events;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Event> getAllEventsAndTheirDetailsOptimizedWithJoinFetch() {
    List<Event> events = eventRepository.findAllWithCategoryAndSpeakers();
    events.forEach(event -> {
      System.out.println("Event: " + event.getName());
      System.out.println("Category: " + event.getCategory().getName());
      System.out.println(
        "Speakers: " +
          event
            .getSpeakers()
            .stream()
            .map(Speaker::getName)
            .collect(Collectors.joining(", "))
      );
    });
    return events;
  }
}
