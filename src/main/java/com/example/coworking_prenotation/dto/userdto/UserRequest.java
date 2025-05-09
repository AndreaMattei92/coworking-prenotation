package com.example.coworking_prenotation.dto.userdto;

public record UserRequest(
    String firstname,
    String lastname,
    String email,
    String password
) {}
