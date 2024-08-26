package com.example.GftApplication.services;

import com.example.GftApplication.entities.Account;
import com.example.GftApplication.entities.Payment;

public interface NotificationService {
    void create(Account accountPayer, Account accountRecipient, Double amount, Payment payment);
}
