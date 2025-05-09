package com.example.coworking_prenotation.dto.prenotazionedto;

import lombok.Data;

@Data
public class PrenotazioneRequestDTO {
    private Long userId;
    private Long postazioneId;
}