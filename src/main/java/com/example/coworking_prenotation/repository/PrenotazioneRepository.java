package com.example.coworking_prenotation.repository;

import com.example.coworking_prenotation.entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    Optional<Prenotazione> findByUserId(Long userId);
}
