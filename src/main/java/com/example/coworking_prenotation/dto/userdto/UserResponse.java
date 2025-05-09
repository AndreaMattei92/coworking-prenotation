package com.example.coworking_prenotation.dto.userdto;

public record UserResponse(
    Long id,
    String firstname,
    String lastname,
    String email
) {}
