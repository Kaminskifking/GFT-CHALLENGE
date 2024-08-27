package com.example.GftApplication.unit.service;

import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.mappers.PaymentMapper;
import com.example.GftApplication.repositories.AccountRepository;
import com.example.GftApplication.repositories.PaymentRepository;
import com.example.GftApplication.services.NotificationService;
import com.example.GftApplication.services.PaymentService;
import com.example.GftApplication.services.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    private PaymentService sut;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @BeforeEach
    void setup() {
        this.sut = new PaymentServiceImpl(accountRepository, notificationService, paymentRepository, paymentMapper);
    }

    @Test
    @DisplayName("Should save new payment")
    void should_save_new_payment() throws NotFoundException {

    }
}
