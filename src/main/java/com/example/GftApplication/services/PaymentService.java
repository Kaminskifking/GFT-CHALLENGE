package com.example.GftApplication.services;

import com.example.GftApplication.dtos.Account.AccountReaderDTO;
import com.example.GftApplication.dtos.CustomerReadDTO;
import com.example.GftApplication.dtos.Payment.PaymentDTO;
import com.example.GftApplication.dtos.Payment.PaymentReadDTO;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.entities.Payment;
import com.example.GftApplication.exceptions.customs.BadRequestExceptions;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PaymentService {

    void create(Long accountId, PaymentDTO paymentDTO, String customerDocument) throws BadRequestExceptions, NotFoundException;

    List<PaymentReadDTO> findAll();

    PaymentReadDTO findById(Long id) throws NotFoundException;

    Page<PaymentReadDTO> getPagedFiltered(final Specification<Payment> spec, final Pageable pageable);

    void softDelete(Long id) throws NotFoundException;

}
