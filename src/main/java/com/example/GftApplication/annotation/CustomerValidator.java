package com.example.GftApplication.annotation;

import com.example.GftApplication.dtos.CustomerCreateDTO;

public class CustomerValidator {

    private final DocumentValidator documentValidator = new DocumentValidator();

    public void validateCustomerCreateDto(CustomerCreateDTO dto) {
        if (dto.name() == null || dto.name().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (dto.document() == null) {
            throw new IllegalArgumentException("Document cannot be empty");
        }

        if (dto.password() == null || dto.password().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        if (!documentValidator.isValidDocument(String.valueOf(dto.document()))) {
            throw new IllegalArgumentException("Invalid CPF or CNPJ");
        }
    }
}
