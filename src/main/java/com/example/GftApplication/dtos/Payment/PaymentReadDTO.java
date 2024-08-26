package com.example.GftApplication.dtos.Payment;

import com.example.GftApplication.entities.Account;

public record PaymentReadDTO(
        Long id,
        Account accountPayer,
        Account accountRecipient,
        Double transferValue
) {
}
