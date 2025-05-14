package com.example.coworking_prenotation.dto.prenotazionedto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PrenotazioneRequestDTO {
    private Long userId;
    private Long postazioneId;
    private LocalDate data;
    private LocalTime oraInizio;
    private LocalTime oraFine;
}