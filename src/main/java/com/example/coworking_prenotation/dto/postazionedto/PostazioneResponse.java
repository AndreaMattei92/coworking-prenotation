package com.example.coworking_prenotation.dto.postazionedto;

public record PostazioneResponse(
    Long id,
    String nome,
    String descrizione,
    Boolean disponibile
) {}
