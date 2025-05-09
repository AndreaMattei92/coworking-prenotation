package com.example.coworking_prenotation.mapper;

import com.example.coworking_prenotation.dto.postazionedto.PostazioneDTO;
import com.example.coworking_prenotation.entity.Postazione;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostazioneMapper {

    PostazioneMapper INSTANCE = Mappers.getMapper(PostazioneMapper.class);

    PostazioneDTO toPostazioneDTO(Postazione postazione);

    Postazione toPostazione(PostazioneDTO postazioneDTO);


}
