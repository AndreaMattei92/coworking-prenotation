package com.example.coworking_prenotation.mapper;


import com.example.coworking_prenotation.dto.userdto.UserDTO;
import com.example.coworking_prenotation.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")  // Abilita l'integrazione con Spring
public interface UserMapper {

    // Istanza di UserMapper (creata automaticamente da MapStruct)
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // Metodo per mappare User in UserDTO
    UserDTO userToUserDTO(User user);

    // Metodo per mappare UserDTO in User
    User userDTOToUser(UserDTO userDTO);
}
