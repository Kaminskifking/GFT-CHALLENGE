package com.example.GftApplication.dtos.Payment;

import jakarta.validation.constraints.NotNull;


public record PaymentDTO(
        @NotNull
        String documentRecipient,
        @NotNull
        Long idAccountRecipient,
        @NotNull
        Double balanceTransfer
) {
}
