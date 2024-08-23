package com.example.GftApplication.dtos;

public record CustomerReadDTO(
        String name,
        Long document,
        String address
) {
}
