package com.example.coworking_prenotation.controller;

import com.example.coworking_prenotation.dto.postazionedto.PostazioneDTO;
import com.example.coworking_prenotation.entity.Postazione;
import com.example.coworking_prenotation.entity.Prenotazione;
import com.example.coworking_prenotation.service.CoworkingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class CoworkingController {

    private final CoworkingService coworkingService;


    public CoworkingController(CoworkingService coworkingService) {
        this.coworkingService = coworkingService;
    }

    @PostMapping("/creaPostazioni")
    public ResponseEntity<String> creaPostazioni(
            @RequestBody PostazioneDTO postazioneDTO) {
        coworkingService.createPostazione(postazioneDTO);

        return ResponseEntity.ok("La postazione è stata creata con successo: " + postazioneDTO.getNome());
    }

    @PostMapping("/creaTantePostazioni")
    public ResponseEntity<String> creaTantePostazioni(
            @RequestBody List<PostazioneDTO> postazioniDTO) {

        postazioniDTO.forEach(coworkingService::createPostazione);

        return ResponseEntity.ok("Sono state create " + postazioniDTO.size() + " postazioni con successo.");
    }

    @GetMapping("/postazioni")
    public ResponseEntity<List<PostazioneDTO>> getPostazioniDisponibili(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String tipologia,
            @RequestParam(required = false) Integer capienza,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime oraInizio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime oraFine
    ) {
        List<PostazioneDTO> result = coworkingService.getPostazioniDisponibili(
                nome, tipologia, capienza, data, oraInizio, oraFine
        );
        return ResponseEntity.ok(result);
    }


}
