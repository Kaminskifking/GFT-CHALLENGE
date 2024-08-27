package com.example.GftApplication.dtos.Customer;

public record CustomerReadDTO(
        Long id,
        String name,
        String document,
        String address
) {
}
