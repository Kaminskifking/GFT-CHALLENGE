package com.example.GftApplication.services.impl;

import com.example.GftApplication.dtos.Notification.NotificationExternalApiDTO;
import com.example.GftApplication.entities.Account;
import com.example.GftApplication.entities.Notification;
import com.example.GftApplication.entities.Payment;
import com.example.GftApplication.exceptions.customs.BadRequestExceptions;
import com.example.GftApplication.repositories.NotificationRepository;
import com.example.GftApplication.services.NotificationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@AllArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public void create(Account accountPayer, Account accountRecipient, Double amount, Payment payment) {
        String urlTransferCompleted = "https://run.mocky.io/v3/a7720747-5c4c-41b6-958c-5abaf03fee26";
        String urlTransferReceived = "https://run.mocky.io/v3/3ddd26a8-0ddf-476e-8678-a7adfa0fd59d";

        ResponseEntity<NotificationExternalApiDTO> responseTransferCompleted = restTemplate.getForEntity(urlTransferCompleted, NotificationExternalApiDTO.class);
        ResponseEntity<NotificationExternalApiDTO> responseTransferReceived = restTemplate.getForEntity(urlTransferReceived, NotificationExternalApiDTO.class);


        Notification notificationForPayer = Notification.builder()
                .notification(
                        Objects.requireNonNull(responseTransferCompleted.getBody()).message() + " to " + accountRecipient.getCustomer().getName()
                                + " in the amount of " + amount)
                .account(accountPayer)
                .payment(payment)
                .build();

        Notification notificationForRecipient = Notification.builder()
                .notification(
                        Objects.requireNonNull(responseTransferReceived.getBody()).message() + " from " + accountPayer.getCustomer().getName()
                                + " in the amount of " + amount)
                .account(accountRecipient)
                .payment(payment)
                .build();

        notificationRepository.save(notificationForPayer);
        notificationRepository.save(notificationForRecipient);
    }
}
