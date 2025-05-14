package com.example.coworking_prenotation.dto.prenotazionedto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
public class PrenotazioneResponseDTO {
    private Long id;
    private Long userId;
    private Long postazioneId;
    private LocalDate data;
    private LocalTime oraInizio;
    private LocalTime oraFine;
}