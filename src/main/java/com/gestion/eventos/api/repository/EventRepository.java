package com.gestion.eventos.api.repository;

import com.gestion.eventos.api.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * EventRepository
 */

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
  Page<Event> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
