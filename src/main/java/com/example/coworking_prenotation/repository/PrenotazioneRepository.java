package com.example.coworking_prenotation.repository;

import com.example.coworking_prenotation.entity.Postazione;
import com.example.coworking_prenotation.entity.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {



    Optional<Prenotazione> findByUserId(Long userId);

    List<Prenotazione> findByPostazioneAndData(Postazione postazione, LocalDate data);

    // Prenotazioni per data
    long countByData(LocalDate data);

    // Prenotazioni per utente
    long countByUserId(Long Id);

    // Prenotazioni per postazione
    long countByPostazioneId(Long postazioneId);

    // Tutte le prenotazioni di una postazione (per report di utilizzo)
    List<Prenotazione> findByPostazioneId(Long postazioneId);

    List<Prenotazione> findByDataAndOraInizio(LocalDate data, LocalTime oraInizio);
}
