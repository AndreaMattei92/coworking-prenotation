package com.example.coworking_prenotation.dto.postazionedto;

public record PostazioneDTO(
    Long id,
    String nome,
    String descrizione,
    Boolean disponibile
) {}
