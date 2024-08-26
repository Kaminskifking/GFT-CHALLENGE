package com.example.GftApplication.services.impl;

import com.example.GftApplication.dtos.Account.AccountReaderDTO;
import com.example.GftApplication.dtos.Account.AccountUpdateDTO;
import com.example.GftApplication.entities.Account;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.enums.AccountStatus;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.mappers.AccountMapper;
import com.example.GftApplication.repositories.AccountRepository;
import com.example.GftApplication.repositories.CustomerRepository;
import com.example.GftApplication.services.AccountService;
import com.example.GftApplication.utils.AgencyUtils;
import jakarta.persistence.EntityNotFoundException;
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
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;
    private final AgencyUtils agencyUtils;

    @Override
    public void create(Long customerId) throws NotFoundException {
        Customer customer = customerRepository
                .findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found"));


        Account account = Account.builder()
                .customer(customer)
                .status(AccountStatus.ACTIVE)
                .agency(agencyUtils.generateUniqueAgency())
                .balance((double) 0)
                .build();

        accountRepository.save(account);
    }

    @Override
    public AccountReaderDTO findById(Long id) throws NotFoundException {
        final var account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        return accountMapper.accountToAccountReadDTO(account);
    }

    @Override
    public void updateAccount(Long id, AccountUpdateDTO accountUpdateDTO) throws NotFoundException {
        final var accountForUpdate = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        accountMapper.updateAccountFromDto(accountUpdateDTO, accountForUpdate);

        accountRepository.save(accountForUpdate);
    }

    @Override
    public void softDelete(Long id) throws NotFoundException {
        final var account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        account.setDeletedAt(LocalDateTime.now());
        accountRepository.save(account);
    }

    @Override
    public Page<AccountReaderDTO> getPagedFiltered(final Specification<Account> spec, final Pageable pageable) {
        Page<Account> accountPageable = accountRepository.findAll(spec, pageable);

        return accountPageable.map(accountMapper::accountToAccountReadDTO);
    }

    @Override
    public List<AccountReaderDTO> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::accountToAccountReadDTO)
                .collect(Collectors.toList());
    }
}
