package com.gestion.eventos.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gestion.eventos.api.domain.Event;

/**
 * EventRepository
 */

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

}
