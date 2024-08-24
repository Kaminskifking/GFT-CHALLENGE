package com.example.GftApplication.dtos.Login;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank
    private String document;
    @NotBlank
    private String password;

}