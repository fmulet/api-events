package com.gestion.eventos.api.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Event;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.EventResponseDto;
import com.gestion.eventos.api.dto.SpeakerResponseDto;
import com.gestion.eventos.api.exception.ResourceNotFoundException;
import com.gestion.eventos.api.mapper.EventMapper;
import com.gestion.eventos.api.security.jwt.JwtAuthEntryPoint;
import com.gestion.eventos.api.security.jwt.JwtAuthenticationFilter;
import com.gestion.eventos.api.security.jwt.JwtGenerator;
import com.gestion.eventos.api.service.EventServiceImpl;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * EventControllerTest
 */
@WebMvcTest(
  controllers = EventController.class,
  excludeAutoConfiguration = {
    SecurityAutoConfiguration.class,
    UserDetailsServiceAutoConfiguration.class,
  },
  excludeFilters = @ComponentScan.Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = {
      JwtAuthenticationFilter.class,
      JwtGenerator.class,
      JwtAuthEntryPoint.class,
    }
  )
)
class EventControllerTest {

  @Autowired
  private MockMvc mockMvc;

  private EventServiceImpl eventService;
  private EventMapper eventMapper;

  private EventResponseDto eventResponseDto;
  private Event eventEntity;

  @TestConfiguration
  static class EventControllerTestConfig {

    @Bean
    @Primary
    EventServiceImpl eventService() {
      return mock(EventServiceImpl.class);
    }

    @Bean
    @Primary
    EventMapper eventMapper() {
      return mock(EventMapper.class);
    }
  }

  @BeforeEach
  void setUp(
    @Autowired EventServiceImpl eventServiceMock,
    @Autowired EventMapper eventMapperMock
  ) {
    this.eventService = eventServiceMock;
    this.eventMapper = eventMapperMock;

    reset(eventService, eventMapper);

    Category category = new Category(1L, "Conferencia", "Conferencia de Java");
    Speaker speaker1 = new Speaker(
      10L,
      "Juan Perez",
      "juan.perez@gmail.com",
      "Bio de Juan",
      new HashSet<>()
    );
    Speaker speaker2 = new Speaker(
      11L,
      "Maria Lopez",
      "maria.lopez@gmail.com",
      "Bio de Maria",
      new HashSet<>()
    );
    eventEntity = new Event();
    eventEntity.setId(1L);
    eventEntity.setName("Conferencia de Java");
    eventEntity.setDate(LocalDate.of(2023, 1, 1));
    eventEntity.setLocation("Online");
    eventEntity.setCategory(category);
    eventEntity.addSpeaker(speaker1);
    eventEntity.addSpeaker(speaker2);

    eventResponseDto = new EventResponseDto();
    eventResponseDto.setId(1L);
    eventResponseDto.setName("Conferencia de Java");
    eventResponseDto.setDate(LocalDate.of(2023, 1, 1));
    eventResponseDto.setLocation("Online");
    eventResponseDto.setCategoryId(10L);
    eventResponseDto.setCategoryName("Conferencia");

    SpeakerResponseDto speakerResponse1 = new SpeakerResponseDto(
      20L,
      "Juan Pérez",
      "juan.perez@example.com",
      "Bio de Juan"
    );
    SpeakerResponseDto speakerResponse2 = new SpeakerResponseDto(
      21L,
      "Maria Lopez",
      "maria.lopez@example.com",
      "Bio de Maria"
    );

    Set<SpeakerResponseDto> speakersDto = new HashSet<>();
    speakersDto.add(speakerResponse1);
    speakersDto.add(speakerResponse2);
    eventResponseDto.setSpeakersDtos(speakersDto);
  }

  @Test
  @DisplayName("Should return event by id")
  @WithMockUser(username = "testUser", roles = "USER")
  void shouldReturnEventById() throws Exception {
    when(eventService.findById(anyLong())).thenReturn(eventEntity);
    when(eventMapper.toResponseDto(any(Event.class))).thenReturn(eventResponseDto);

    mockMvc
      .perform(
        MockMvcRequestBuilders.get("/api/v1/events/{eventId}", 1L).accept(
          MediaType.APPLICATION_JSON
        )
      )
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Conferencia de Java"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2023-01-01"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.location").value("Online"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.categoryName").value("Conferencia"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.speakersDtos.length()").value(2))
      .andExpect(MockMvcResultMatchers.jsonPath("$.speakersDtos[2]").doesNotExist())
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.speakersDtos[?(@.name == 'Juan Pérez')].name"
        ).value(hasItem("Juan Pérez"))
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.speakersDtos[?(@.name == 'Juan Pérez')].email"
        ).value(hasItem("juan.perez@example.com"))
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.speakersDtos[?(@.name == 'Juan Pérez')].bio"
        ).value(hasItem("Bio de Juan"))
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.speakersDtos[?(@.name == 'Maria Lopez')].name"
        ).value(hasItem("Maria Lopez"))
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.speakersDtos[?(@.name == 'Maria Lopez')].email"
        ).value(hasItem("maria.lopez@example.com"))
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.speakersDtos[?(@.name == 'Maria Lopez')].bio"
        ).value(hasItem("Bio de Maria"))
      );

    verify(eventService, times(1)).findById(1L);
    verify(eventMapper, times(1)).toResponseDto(eventEntity);
  }

  @Test
  @DisplayName("should return 404 when event does not exist")
  @WithMockUser(username = "testuser", roles = "USER")
  void shouldReturnNotFoundWhenEventDoesNotExist() throws Exception {
    when(eventService.findById(anyLong())).thenThrow(
      new ResourceNotFoundException("Event not found")
    );

    mockMvc
      .perform(
        MockMvcRequestBuilders.get("/api/events/{id}", 99L).accept(
          MediaType.APPLICATION_JSON
        )
      )
      .andExpect(MockMvcResultMatchers.status().isNotFound())
      .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("404"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Event not found"));

    verify(eventService, times(1)).findById(99L);
    verify(eventMapper, never()).toResponseDto(any(Event.class));
  }

  @Test
  @DisplayName("should return all events paged and filtered")
  @WithMockUser(username = "testuser", roles = "USER")
  void shouldReturnAllEventsPagedAndFiltered() throws Exception {
    SpeakerResponseDto speakerResponseA = new SpeakerResponseDto(
      1L,
      "Juan Pérez",
      "juan.perez@example.com",
      "Bio de Juan"
    );
    Set<SpeakerResponseDto> speakersA = new HashSet<>(Set.of(speakerResponseA));
    EventResponseDto eventResponse2 = new EventResponseDto();
    eventResponse2.setId(2L);
    eventResponse2.setName("Evento B");
    eventResponse2.setDate(LocalDate.of(2023, 9, 10));
    eventResponse2.setLocation("Virtual");
    eventResponse2.setCategoryName("Tecnología");
    eventResponse2.setCategoryId(10L);
    eventResponse2.setSpeakersDtos(speakersA);

    EventResponseDto eventResponse3 = new EventResponseDto();
    eventResponse3.setId(3L);
    eventResponse3.setName("Evento C");
    eventResponse3.setDate(LocalDate.of(2023, 9, 11));
    eventResponse3.setLocation("Virtual");
    eventResponse3.setCategoryName("Tecnología");
    eventResponse3.setCategoryId(10L);
    eventResponse3.setSpeakersDtos(speakersA);

    List<EventResponseDto> eventResponseList = List.of(eventResponse2, eventResponse3);

    Pageable pageable = PageRequest.of(0, 10);

    Page<EventResponseDto> eventResponseDtoPage = new PageImpl<>(
      eventResponseList,
      pageable,
      eventResponseList.size()
    );

    when(eventService.findAll(eq("Evento C"), any(Pageable.class))).thenReturn(
      eventResponseDtoPage
    );

    mockMvc
      .perform(
        MockMvcRequestBuilders.get("/api/v1/events")
          .param("page", "0")
          .param("size", "10")
          .param("name", "Evento")
          .accept(MediaType.APPLICATION_JSON)
      )

      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[0]").exists())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[1]").exists())
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[2]").doesNotExist())

      .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value(2))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Evento B"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].date").value("2023-09-10"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].location").value("Virtual"))
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.content[0].categoryName").value("Tecnología")
      )
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].categoryId").value(10))
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.content[0].speakers.length()").value(1)
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.content[0].speakers[?(@.name=='Juan Pérez')].id"
        ).value(20)
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.content[0].speakers[?(@.name=='Juan Pérez')].name"
        ).value("Juan Pérez")
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.content[0].speakers[?(@.name=='Juan Pérez')].email"
        ).value("juan.perez@example.com")
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.content[0].speakers[?(@.name=='Juan Pérez')].bio"
        ).value("Bio de Juan")
      )

      .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].id").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("Evento A"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].date").value("2023-09-09"))
      .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].location").value("Virtual"))
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.content[1].categoryName").value("Tecnología")
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath("$.content[1].speakers.length()").value(1)
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.content[1].speakers[?(@.name=='Juan Pérez')].id"
        ).value(20)
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.content[1].speakers[?(@.name=='Juan Pérez')].name"
        ).value("Juan Pérez")
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.content[1].speakers[?(@.name=='Juan Pérez')].email"
        ).value("juan.perez@example.com")
      )
      .andExpect(
        MockMvcResultMatchers.jsonPath(
          "$.content[1].speakers[?(@.name=='Juan Pérez')].bio"
        ).value("Bio de Juan")
      )

      .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageNumber").value(0))
      .andExpect(MockMvcResultMatchers.jsonPath("$.pageable.pageSize").value(10))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2))
      .andExpect(MockMvcResultMatchers.jsonPath("$.totalPages").value(1))
      .andExpect(MockMvcResultMatchers.jsonPath("$.last").value(true));

    verify(eventService, times(1)).findAll(eq("Evento"), any(Pageable.class));
    verify(eventService, never()).findById(anyLong());
  }
}
