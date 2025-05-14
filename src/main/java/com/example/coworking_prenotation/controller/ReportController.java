package com.example.coworking_prenotation.controller;

import com.example.coworking_prenotation.entity.Prenotazione;
import com.example.coworking_prenotation.service.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {


    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // per data
    @GetMapping("/prenotazioni/data")
    public long countByData(@RequestParam LocalDate data) {
        return reportService.countPrenotazioniByData(data);
    }

    // per utente
    @GetMapping("/prenotazioni/utente")
    public long countByUtente(@RequestParam Long userId) {
        return reportService.countPrenotazioniByUser(userId);
    }

    // prenotazioni per postazione
    @GetMapping("/prenotazioni/postazione")
    public long countByPostazione(@RequestParam Long postazioneId) {
        return reportService.countPrenotazioniByPostazione(postazioneId);
    }

    // tutti gli utilizzi di una postazione
    @GetMapping("/prenotazioni/postazione/utenti")
    public List<Prenotazione> countPrenotazioneByPostazione(@RequestParam Long postazioneId) {
        return reportService.getPrenotazioniByPostazione(postazioneId);
    }

}
