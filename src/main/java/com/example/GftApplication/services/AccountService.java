package com.example.GftApplication.services;

import com.example.GftApplication.dtos.Account.AccountReaderDTO;
import com.example.GftApplication.dtos.Account.AccountUpdateDTO;
import com.example.GftApplication.entities.Account;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.exceptions.customs.UniqueConstraintViolationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface AccountService {

    void create(Long customerId) throws NotFoundException;

    AccountReaderDTO findById(Long id) throws NotFoundException;

    void updateAccount(Long id, AccountUpdateDTO accountUpdateDTO) throws NotFoundException;

    void softDelete(final Long id) throws NotFoundException;

    Page<AccountReaderDTO> getPagedFiltered(final Specification<Account> spec, final Pageable pageable);

    List<AccountReaderDTO> findAll();

}
