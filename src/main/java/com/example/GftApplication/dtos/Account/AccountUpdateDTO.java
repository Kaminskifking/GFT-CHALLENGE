package com.example.GftApplication.dtos.Account;

import com.example.GftApplication.enums.AccountStatus;

import java.math.BigDecimal;

public record AccountUpdateDTO(
        Double balance,
        AccountStatus status
) {
}
