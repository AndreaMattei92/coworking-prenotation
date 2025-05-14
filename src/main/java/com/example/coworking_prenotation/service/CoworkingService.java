package com.example.coworking_prenotation.service;

import com.example.coworking_prenotation.dto.postazionedto.PostazioneDTO;
import com.example.coworking_prenotation.entity.Postazione;
import com.example.coworking_prenotation.entity.Prenotazione;
import com.example.coworking_prenotation.repository.PostazioneRepository;
import com.example.coworking_prenotation.repository.PrenotazioneRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class CoworkingService {

    private final PostazioneRepository postazioneRepository;
    private final PrenotazioneRepository prenotazioneRepository;

    public CoworkingService(PostazioneRepository postazioneRepository, PrenotazioneRepository prenotazioneRepository) {
        this.postazioneRepository = postazioneRepository;
        this.prenotazioneRepository = prenotazioneRepository;
    }


    public void createPostazione(PostazioneDTO postazioneDTO) {

        Postazione newPostazione = Postazione.builder()
                .nome(postazioneDTO.getNome())
                .tipologia(postazioneDTO.getTipologia())
                .capienza(postazioneDTO.getCapienza())
                .disponibile(true)
                .build();

        postazioneRepository.save(newPostazione);

    }

    public List<PostazioneDTO> getPostazioniDisponibili(
            String nome, String tipologia, Integer capienza,
            LocalDate data, LocalTime oraInizio, LocalTime oraFine) {

        List<Postazione> postazioni = postazioneRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (tipologia != null && !tipologia.isBlank()) {
                predicates.add(cb.equal(root.get("tipologia"), tipologia));
            }
            if (capienza != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("capienza"), capienza));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });

        return postazioni.stream().map(postazione -> {
            boolean disponibile = true;

            if (data != null && oraInizio != null && oraFine != null) {
                disponibile = verificaDisponibilita(postazione, data, oraInizio, oraFine);
            }

            PostazioneDTO dto = new PostazioneDTO();
            dto.setId(postazione.getId());
            dto.setNome(postazione.getNome());
            dto.setTipologia(postazione.getTipologia());
            dto.setCapienza(postazione.getCapienza());
            dto.setDisponibile(disponibile);
            return dto;
        }).toList();
    }
    public boolean verificaDisponibilita(Postazione postazione, LocalDate data, LocalTime oraInizio, LocalTime oraFine) {
        // Supponiamo tu abbia una entity Prenotazione con data, oraInizio, oraFine e postazione associata
        List<Prenotazione> prenotazioni = prenotazioneRepository.findByPostazioneAndData(postazione, data);

        for (Prenotazione p : prenotazioni) {
            // Se gli intervalli si sovrappongono
            if (!(oraFine.isBefore(p.getOraInizio()) || oraInizio.isAfter(p.getOraFine()))) {
                return false; // Occupata
            }
        }

        return true; // Disponibile
    }
}
