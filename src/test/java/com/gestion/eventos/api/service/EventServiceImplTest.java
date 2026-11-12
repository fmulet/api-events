package com.gestion.eventos.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.EventRequestDto;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
import com.gestion.eventos.api.mapper.EventMapper;
import com.gestion.eventos.api.repository.EventRepository;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * EventServiceTest
 */
@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {

  @Mock
  private EventRepository eventRepository;

  @Mock
  private EventMapper eventMapper;

  @Mock
  private CategoryServiceImpl categoryService;

  @Mock
  private SpeakerServiceImpl speakerService;

  @InjectMocks
  private EventServiceImpl eventService;

  private Event event;
  private EventRequestDto eventRequestDto;
  private EventResponseDto eventResponseDto;
  private Category category;
  private Speaker speaker1;
  private Speaker speaker2;
  private Pageable pageable;

  @BeforeEach
  void setUp() {
    category = new Category(1L, "Conferencia", "Conferencia de Java");
    speaker1 = new Speaker(
      10L,
      "Juan Perez",
      "juan.perez@gmail.com",
      "Bio de Juan",
      new HashSet<>()
    );
    speaker2 = new Speaker(
      11L,
      "Maria Lopez",
      "maria.lopez@gmail.com",
      "Bio de Maria",
      new HashSet<>()
    );
    event = new Event();
    event.setId(1L);
    event.setName("Conferencia de Java");
    event.setDate(LocalDate.of(2023, 1, 1));
    event.setLocation("Online");
    event.setCategory(category);
    event.getSpeakers().add(speaker1);
    event.getSpeakers().add(speaker2);

    eventRequestDto = new EventRequestDto();
    eventRequestDto.setName("Conferencia de Java");
    eventRequestDto.setDate(LocalDate.of(2023, 1, 1));
    eventRequestDto.setLocation("Online");
    eventRequestDto.setCategoryId(1L);
    eventRequestDto.setSpeakersIds(new HashSet<>(Set.of(10L, 11L)));

    eventResponseDto = new EventResponseDto();
    eventResponseDto.setId(1L);
    eventResponseDto.setName("Conferencia de Java");
    eventResponseDto.setDate(LocalDate.of(2023, 1, 1));
    eventResponseDto.setLocation("Online");
    eventResponseDto.setCategoryId(1L);
    eventResponseDto.setCategoryName("Conferencia");

    pageable = PageRequest.of(0, 10);
  }

  @Test
  @DisplayName("Should return a event when id exists")
  void shouldReturnEventWhenIdExists() {
    when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
    Event foundEvent = eventService.findById(1L);
    assertNotNull(foundEvent);
    assertEquals(event.getId(), foundEvent.getId());
    verify(eventRepository, times(1)).findById(1L);
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when id does not exist")
  void shouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
    when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
    ResourceNotFoundException exception = assertThrows(
      ResourceNotFoundException.class,
      () -> eventService.findById(99L)
    );
    assertEquals("Event not found", exception.getMessage());
    verify(eventRepository, times(1)).findById(99L);
  }

  @Test
  @DisplayName("Should save an event successfully with category and speakers")
  void shouldSaveEventSuccessfullyWithCategoryAndSpeakers() {
    Event eventWithoutId = new Event();
    eventWithoutId.setName(eventRequestDto.getName());
    eventWithoutId.setDate(eventRequestDto.getDate());
    eventWithoutId.setLocation(eventRequestDto.getLocation());

    when(eventMapper.toEntity(any(EventRequestDto.class))).thenReturn(eventWithoutId);

    when(categoryService.findById(eventRequestDto.getCategoryId())).thenReturn(category);
    when(speakerService.findById(10L)).thenReturn(speaker1);
    when(speakerService.findById(11L)).thenReturn(speaker2);

    when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
      Event savedEvent = invocation.getArgument(0);
      savedEvent.setId(1L);
      return savedEvent;
    });

    Event savedEvent = eventService.save(eventRequestDto);

    assertNotNull(savedEvent);
    assertEquals(1L, savedEvent.getId());
    assertEquals(eventRequestDto.getName(), savedEvent.getName());
    assertEquals(category, savedEvent.getCategory());
    assertEquals(2, savedEvent.getSpeakers().size());

    assertTrue(savedEvent.getSpeakers().contains(speaker1));
    assertTrue(savedEvent.getSpeakers().contains(speaker2));

    verify(eventMapper, times(1)).toEntity(eventRequestDto);
    verify(categoryService, times(1)).findById(eventRequestDto.getCategoryId());
    verify(speakerService, times(1)).findById(10L);
    verify(speakerService, times(1)).findById(11L);
    verify(eventRepository, times(1)).save(any(Event.class));
  }

  @Test
  @DisplayName("Should save event successfully without speakers")
  void shouldSaveEventSuccessfullyWithoutSpeakers() {
    eventRequestDto.setSpeakersIds(null);

    Event eventWithoutId = new Event();
    eventWithoutId.setName(eventRequestDto.getName());
    eventWithoutId.setDate(eventRequestDto.getDate());
    eventWithoutId.setLocation(eventRequestDto.getLocation());

    when(eventMapper.toEntity(any(EventRequestDto.class))).thenReturn(eventWithoutId);

    when(categoryService.findById(eventRequestDto.getCategoryId())).thenReturn(category);

    when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> {
      Event savedEvent = invocation.getArgument(0);
      savedEvent.setId(1L);
      return savedEvent;
    });

    Event savedEvent = eventService.save(eventRequestDto);

    assertNotNull(savedEvent);
    assertEquals(1L, savedEvent.getId());
    assertEquals(eventRequestDto.getName(), savedEvent.getName());
    assertEquals(category, savedEvent.getCategory());
    assertTrue(savedEvent.getSpeakers().isEmpty());

    verify(eventMapper, times(1)).toEntity(eventRequestDto);
    verify(categoryService, times(1)).findById(eventRequestDto.getCategoryId());
    verify(speakerService, never()).findById(anyLong());
    verify(eventRepository, times(1)).save(any(Event.class));
  }

  @Test
  @DisplayName("Should throw ResourceNotFoundException when category not found on save")
  void shouldThrowResourceNotFoundExceptionWhenCategoryNotFoundOnSave() {
    Event eventWithoutId = new Event();

    when(eventMapper.toEntity(any(EventRequestDto.class))).thenReturn(eventWithoutId);

    when(categoryService.findById(anyLong())).thenThrow(
      new ResourceNotFoundException("Category not found")
    );

    ResourceNotFoundException thrown = assertThrows(
      ResourceNotFoundException.class,
      () -> {
        eventService.save(eventRequestDto);
      }
    );

    assertEquals("Category not found", thrown.getMessage());

    verify(eventRepository, never()).save(any(Event.class));
  }

  @Test
  @DisplayName("Should return page of events with name filter")
  void shouldReturnPageOfEventsWithoutNameFilter() {
    List<Event> events = Collections.singletonList(event);
    Page<Event> eventPage = new PageImpl<>(events, pageable, 1);

    when(eventRepository.findAll(pageable)).thenReturn(eventPage);

    when(eventMapper.toResponseDto(any(Event.class))).thenReturn(eventResponseDto);

    Page<EventResponseDto> response = eventService.findAll(null, pageable);

    assertNotNull(response);
    assertEquals(1, response.getTotalElements());
    assertEquals(1, response.getContent().size());
    assertEquals(eventResponseDto, response.getContent().get(0));

    verify(eventRepository, times(1)).findAll(pageable);
    verify(eventRepository, never()).findByNameContainingIgnoreCase(
      anyString(),
      any(Pageable.class)
    );
    verify(eventMapper, times(1)).toResponseDto(event);
  }

  @Test
  @DisplayName("Should return page of events with name filter")
  void shouldReturnPageOfEventsWithNameFilter() {
    String filterName = "Spring";
    List<Event> events = Collections.singletonList(event);
    Page<Event> eventPage = new PageImpl<>(events, pageable, 1);

    when(eventRepository.findByNameContainingIgnoreCase(filterName, pageable)).thenReturn(
      eventPage
    );

    when(eventMapper.toResponseDto(any(Event.class))).thenReturn(eventResponseDto);

    Page<EventResponseDto> response = eventService.findAll(filterName, pageable);

    assertNotNull(response);
    assertEquals(1, response.getTotalElements());
    assertEquals(1, response.getContent().size());
    assertEquals(eventResponseDto, response.getContent().get(0));

    verify(eventRepository, times(1)).findByNameContainingIgnoreCase(
      filterName,
      pageable
    );
    verify(eventRepository, never()).findAll(any(Pageable.class));
    verify(eventMapper, times(1)).toResponseDto(event);
  }
}
