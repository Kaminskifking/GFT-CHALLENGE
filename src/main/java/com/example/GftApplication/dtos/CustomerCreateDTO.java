package com.example.GftApplication.dtos;

public record CustomerCreateDTO(
        String name,
        Long document,
        String address,
        String password
) {
}
