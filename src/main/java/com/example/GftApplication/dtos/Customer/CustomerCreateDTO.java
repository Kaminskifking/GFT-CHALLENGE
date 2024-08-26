package com.example.GftApplication.dtos.Customer;

import com.example.GftApplication.annotation.ValidPassword;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CustomerCreateDTO(
        @NotNull(message = "Name cannot be empty")
        @Length(min = 3, message = "Name must be at least 3 characters long")
        String name,

        @NotNull(message = "Document cannot be empty")
        String document,

        @NotNull(message = "Address cannot be empty")
        @Length(min = 6, message = "Address must be at least 6 characters long")
        String address,

        @NotNull(message = "Password cannot be empty")
        @ValidPassword
        String password
) {
}
