package com.example.coworking_prenotation.controller;

import com.example.coworking_prenotation.dto.postazionedto.PostazioneDTO;
import com.example.coworking_prenotation.dto.prenotazionedto.PrenotazioneRequestDTO;
import com.example.coworking_prenotation.dto.prenotazionedto.PrenotazioneResponseDTO;
import com.example.coworking_prenotation.service.PrenotationService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/prenotazioni")
public class PrenotationController {

    private final PrenotationService prenotationService;

    public PrenotationController(PrenotationService prenotationService) {
        this.prenotationService = prenotationService;
    }

    @PostMapping("/prenotazione")
    public ResponseEntity<PrenotazioneResponseDTO> createPrenotazione(
            @RequestBody PrenotazioneRequestDTO prenotazioneRequestDTO
    ) {
        return prenotationService.createPrenotation(prenotazioneRequestDTO);
    }

    @GetMapping("/prenotazione{id}")
    public List<PrenotazioneResponseDTO> getDettaglioPrenotazione(
            @PathVariable Long id ) {

        return prenotationService.getDettaglioPrenotazione(id) ;
    }

    @GetMapping("/allprenotazioni")
    public List<PrenotazioneResponseDTO> getAllPrenotazioni() {
        return prenotationService.getAllPrenotazioni();
    }

    @PutMapping("/modificaPrenotazione{id}")
    public ResponseEntity<String> modificaPrenotazione(
            @PathVariable Long id,
            @RequestBody PrenotazioneRequestDTO prenotazioneRequestDTO
    ) {
       prenotationService.modificaPrenotazione(id, prenotazioneRequestDTO);
        return ResponseEntity.ok("Prenotazione modificata con successo");
    }

    @DeleteMapping("/cancellaPrenotazione{id}")
    public ResponseEntity<String> cancellaPrenotazione(
            @PathVariable Long id
    ) {
        prenotationService.cancellaPrenotazione(id);
        return ResponseEntity.ok("Prenotazione cancellata con successo");
    }

    @GetMapping("/disponibilita")
    public ResponseEntity<List<PostazioneDTO>> getDisponibilita(
            @RequestParam Long postazioneId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalTime oraInizio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalTime oraFine
    ) {
        List<PostazioneDTO> disponibilita = prenotationService.getDisponibilita(postazioneId, data, oraInizio, oraFine);
        return ResponseEntity.ok(disponibilita);
    }

}
