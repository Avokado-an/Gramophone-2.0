package com.anton.gramophone.entity.dto;

import lombok.Data;

@Data
public class AuthenticationDto {
    private String email;
    private String password;
}
