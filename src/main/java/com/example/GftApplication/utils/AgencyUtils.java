package com.example.GftApplication.utils;

import com.example.GftApplication.repositories.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@AllArgsConstructor
public class AgencyUtils {

    private final AccountRepository accountRepository;

    public String generateUniqueAgency() {
        String agency;
        do {
            agency = generateRandomAgency();
        } while (accountRepository.existsByAgency(agency));
        return agency;
    }

    private static String generateRandomAgency() {
        Random random = new Random();
        int number = 1000 + random.nextInt(9000);
        return String.format("%04d", number);
    }
}