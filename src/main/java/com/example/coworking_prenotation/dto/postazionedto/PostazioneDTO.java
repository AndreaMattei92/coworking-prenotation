package com.example.coworking_prenotation.dto.postazionedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostazioneDTO {
   private Long id;
   private String nome;
   private String tipologia;
   private int capienza;
   private Boolean disponibile;

}
