package com.example.coworking_prenotation.dto.postazionedto;

public record PostazioneRequest(
    String nome,
    String descrizione,
    Boolean disponibile
) {}
