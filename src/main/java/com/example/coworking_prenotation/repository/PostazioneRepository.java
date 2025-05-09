package com.example.coworking_prenotation.repository;

import com.example.coworking_prenotation.entity.Postazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostazioneRepository extends JpaRepository<Postazione, Long> {
}
