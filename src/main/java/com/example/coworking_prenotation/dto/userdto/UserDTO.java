package com.example.coworking_prenotation.dto.userdto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String role;

}