package com.example.GftApplication.services.impl;

import com.example.GftApplication.dtos.Payment.PaymentDTO;
import com.example.GftApplication.dtos.Payment.PaymentReadDTO;
import com.example.GftApplication.entities.Account;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.entities.Payment;
import com.example.GftApplication.enums.AccountStatus;
import com.example.GftApplication.exceptions.customs.BadRequestExceptions;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.mappers.PaymentMapper;
import com.example.GftApplication.repositories.AccountRepository;
import com.example.GftApplication.repositories.PaymentRepository;
import com.example.GftApplication.services.NotificationService;
import com.example.GftApplication.services.PaymentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public void create(Long accountId, PaymentDTO paymentDTO, String customerDocument) throws BadRequestExceptions, NotFoundException {
        Account accountPayer = accountRepository.findByAccountIdAndCustomerDocument(accountId, customerDocument)
                .orElseThrow(() -> new NotFoundException("Account not found for account ID: " + accountId + " and customer document: " + customerDocument));


        Account accountRecipient = accountRepository.findByAccountIdAndCustomerDocument(paymentDTO.idAccountRecipient(), paymentDTO.documentRecipient())
                .orElseThrow(() -> new NotFoundException("Account not found for account ID: " + accountId + " and customer document: " + customerDocument));


        if (accountPayer.getStatus() == AccountStatus.INACTIVE || accountRecipient.getStatus() == AccountStatus.INACTIVE) {
            throw new BadRequestExceptions("Your or the recipient's account is inactive, contact bank support");
        }

        if (accountPayer.getBalance() < paymentDTO.balanceTransfer()) {
            throw new BadRequestExceptions("You do not have enough balance for this transfer");
        }

        Payment payment = Payment.builder()
                .accountPayer(accountPayer)
                .accountRecipient(accountRecipient)
                .transferValue(paymentDTO.balanceTransfer())
                .build();

        accountPayer.setBalance(accountPayer.getBalance() - paymentDTO.balanceTransfer());
        accountRecipient.setBalance(accountRecipient.getBalance() + paymentDTO.balanceTransfer());

        accountRepository.save(accountPayer);
        accountRepository.save(accountRecipient);
        paymentRepository.save(payment);

        try {
            notificationService.create(accountPayer, accountRecipient, paymentDTO.balanceTransfer(), payment);
        } catch (BadRequestExceptions exceptions) {
            throw new BadRequestExceptions("Creation error in external api for notification");
        }
    }

    @Override
    public List<PaymentReadDTO> findAll() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::paymentToPaymentReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentReadDTO findById(Long id) throws NotFoundException {
        final var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        return paymentMapper.paymentToPaymentReadDTO(payment);
    }

    @Override
    public Page<PaymentReadDTO> getPagedFiltered(Specification<Payment> spec, Pageable pageable) {
        Page<Payment> paymentPageable = paymentRepository.findAll(spec, pageable);
        return paymentPageable.map(paymentMapper::paymentToPaymentReadDTO);
    }

    @Override
    public void softDelete(Long id) throws NotFoundException {
        final var payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Payment not found"));

        payment.setDeletedAt(LocalDateTime.now());
        paymentRepository.save(payment);
    }
}
