package com.gestion.eventos.api.controller;

import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.dto.SpeakerRequestDto;
import com.gestion.eventos.api.dto.SpeakerResponseDto;
import com.gestion.eventos.api.mapper.SpeakerMapper;
import com.gestion.eventos.api.service.ISpeakerService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpeakerController
 */

@RestController
@RequestMapping("/api/v1/speakers")
@RequiredArgsConstructor
public class SpeakerController {

  private final ISpeakerService speakerService;
  private final SpeakerMapper speakerMapper;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<SpeakerResponseDto> createSpeaker(
    @Valid @RequestBody SpeakerRequestDto speakerRequestDto
  ) {
    Speaker createdSpeaker = speakerService.save(speakerRequestDto);
    SpeakerResponseDto speakerResponseDto = speakerMapper.toDto(createdSpeaker);
    return new ResponseEntity<>(speakerResponseDto, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<SpeakerResponseDto> getSpeakerById(@PathVariable Long id) {
    Speaker speaker = speakerService.findById(id);
    SpeakerResponseDto speakerResponseDto = speakerMapper.toDto(speaker);
    return new ResponseEntity<>(speakerResponseDto, HttpStatus.OK);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
  public ResponseEntity<List<SpeakerResponseDto>> getAllSpeakers() {
    List<Speaker> speakers = speakerService.findAll();
    List<SpeakerResponseDto> speakerResponseDto = speakerMapper.toResponseDtoList(
      speakers
    );
    return new ResponseEntity<>(speakerResponseDto, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<SpeakerResponseDto> updateSpeaker(
    @PathVariable Long id,
    @Valid @RequestBody SpeakerRequestDto speakerRequestDto
  ) {
    Speaker updatedSpeaker = speakerService.update(id, speakerRequestDto);
    SpeakerResponseDto speakerResponseDto = speakerMapper.toDto(updatedSpeaker);
    return new ResponseEntity<>(speakerResponseDto, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN')")
  public ResponseEntity<Void> deleteSpeaker(@PathVariable Long id) {
    speakerService.deleteById(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
