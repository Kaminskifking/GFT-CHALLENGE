package com.example.GftApplication.dtos;

public record CustomerReadDTO(
        Long id,
        String name,
        Long document,
        String address
) {
}
