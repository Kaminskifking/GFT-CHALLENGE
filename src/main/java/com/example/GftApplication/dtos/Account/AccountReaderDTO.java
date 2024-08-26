package com.example.GftApplication.dtos.Account;

import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.enums.AccountStatus;

public record AccountReaderDTO(
        Long id,
        String agency,
        Double balance,
        AccountStatus status,
        Customer customer
) {
}
