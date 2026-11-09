package com.gestion.eventos.api.repository;

import com.gestion.eventos.api.domain.Speaker;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * SpeakerRepository
 */

@Repository
public interface SpeakerRepository extends JpaRepository<Speaker, Long> {
  Optional<Speaker> findByEmail(String email);

  boolean existsByEmail(String email);
}
