package com.example.GftApplication.dtos.Customer;

import com.example.GftApplication.annotation.ValidPassword;
import org.hibernate.validator.constraints.Length;

public record CustomerUpdateDTO(
        @Length(min = 3, message = "Name must be at least 4 characters long")
        String name,

        @Length(min = 6, message = "Address must be at least 6 characters long")
        String address,

        @ValidPassword
        String password
) {
}
