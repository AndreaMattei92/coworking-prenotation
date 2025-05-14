package com.example.coworking_prenotation.service;

import com.example.coworking_prenotation.entity.Prenotazione;
import com.example.coworking_prenotation.repository.PrenotazioneRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final PrenotazioneRepository prenotazioneRepository;


    public ReportService(PrenotazioneRepository prenotazioneRepository) {
        this.prenotazioneRepository = prenotazioneRepository;
    }

    // per data
    public long countPrenotazioniByData(LocalDate data) {
        return prenotazioneRepository.countByData(data);
    }

    // per utente
    public long countPrenotazioniByUser(Long userId) {
        return prenotazioneRepository.countByUserId(userId);
    }

    // prenotazioni per postazione
    public long countPrenotazioniByPostazione(Long postazioneId) {
        return prenotazioneRepository.countByPostazioneId(postazioneId);
    }

    // tutti gli utilizzi di una postazione
    public List<Prenotazione> getPrenotazioniByPostazione(Long postazioneId) {
        return prenotazioneRepository.findByPostazioneId(postazioneId);
    }

}
