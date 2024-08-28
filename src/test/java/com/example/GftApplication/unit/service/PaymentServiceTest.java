package com.example.GftApplication.unit.service;

import com.example.GftApplication.dtos.Payment.PaymentCreateDTO;
import com.example.GftApplication.dtos.Payment.PaymentReadDTO;
import com.example.GftApplication.entities.Account;
import com.example.GftApplication.entities.Payment;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.mappers.PaymentMapper;
import com.example.GftApplication.repositories.AccountRepository;
import com.example.GftApplication.repositories.PaymentRepository;
import com.example.GftApplication.resolver.account.AccountResolver;
import com.example.GftApplication.resolver.payment.PaymentCreateDTOResolver;
import com.example.GftApplication.resolver.payment.PaymentReaderDTOResolver;
import com.example.GftApplication.resolver.payment.PaymentResolver;
import com.example.GftApplication.services.NotificationService;
import com.example.GftApplication.services.PaymentService;
import com.example.GftApplication.services.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@ExtendWith(AccountResolver.class)
@ExtendWith(PaymentCreateDTOResolver.class)
@ExtendWith(PaymentReaderDTOResolver.class)
@ExtendWith(PaymentResolver.class)
public class PaymentServiceTest {

    private PaymentService sut;

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private NotificationService notificationServiceMock;

    @Mock
    private PaymentRepository paymentRepositoryMock;

    @Mock
    private PaymentMapper paymentMapperMock;

    @BeforeEach
    void setup() {
        this.sut = new PaymentServiceImpl(accountRepositoryMock, notificationServiceMock, paymentRepositoryMock, paymentMapperMock);
    }

    @Test
    @DisplayName("Should save new payment")
    void should_save_new_payment(Account accountPayer, PaymentCreateDTO paymentCreateDTO, Account accountRecipient) throws NotFoundException {
        final var paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        final var accountCaptor = ArgumentCaptor.forClass(Account.class);

        when(this.accountRepositoryMock.findByAccountIdAndCustomerDocument(accountPayer.getId(),
                accountPayer.getCustomer().getDocument())).thenReturn(Optional.of(accountPayer));
        when(this.accountRepositoryMock.findByAccountIdAndCustomerDocument(paymentCreateDTO.idAccountRecipient(),
                paymentCreateDTO.documentRecipient())).thenReturn(Optional.of(accountRecipient));

        this.sut.create(accountPayer.getId(),paymentCreateDTO ,accountPayer.getCustomer().getDocument());

        verify(accountRepositoryMock, times(2)).save(accountCaptor.capture());
        verify(paymentRepositoryMock).save(paymentCaptor.capture());


        List<Account> savedAccounts = accountCaptor.getAllValues();
        final var paymentCap = paymentCaptor.getValue();

        assertTrue(savedAccounts.contains(accountPayer));
        assertTrue(savedAccounts.contains(accountRecipient));

        assertEquals(accountPayer, paymentCap.getAccountPayer());
        assertEquals(accountRecipient, paymentCap.getAccountRecipient());
        assertEquals(paymentCreateDTO.balanceTransfer(), paymentCap.getTransferValue());
    }

    @Test
    @DisplayName("Should return a list of payer")
    void should_return_a_list_of_payer(Payment payment)  {
        List<Payment> paymentReadDTOList = List.of(payment);

        when(this.paymentRepositoryMock.findAll()).thenReturn(paymentReadDTOList);

        this.sut.findAll();

        verify(this.paymentRepositoryMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Should paginated filtered payments")
    void should_paginated_filtered_payments() {
        Specification<Payment> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Pageable pageable = PageRequest.of(0, 10);

        List<Payment> fakePayments = new ArrayList<>();

        Page<Payment> fakePaymentPage = new PageImpl<>(fakePayments, pageable, 0);

        when(paymentRepositoryMock.findAll(spec, pageable)).thenReturn(fakePaymentPage);

        Page<PaymentReadDTO> result = this.sut.getPagedFiltered(spec, pageable);

        verify(paymentRepositoryMock).findAll(spec, pageable);

        assertEquals(fakePaymentPage.map(paymentMapperMock::paymentToPaymentReadDTO), result);
    }

    @Test
    @DisplayName("Should find payment with success")
    void should_find_payment_by_id(Payment payment) throws NotFoundException {

        when(this.paymentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(payment));

        this.sut.findById(anyLong());

        verify(this.paymentRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should delete payment")
    void should_delete_payment(Payment payment) throws NotFoundException {
        final var paymentCaptor = ArgumentCaptor.forClass(Payment.class);

        when(this.paymentRepositoryMock.findById(anyLong())).thenReturn(Optional.of(payment));

        this.sut.softDelete(anyLong());

        verify(this.paymentRepositoryMock).save(paymentCaptor.capture());

        final var paymentCap = paymentCaptor.getValue();
        assertEquals(payment.getDeletedAt(), paymentCap.getDeletedAt());
    }
}
