package com.example.GftApplication.unit.service;

import com.example.GftApplication.dtos.Account.AccountReaderDTO;
import com.example.GftApplication.dtos.Account.AccountUpdateDTO;
import com.example.GftApplication.entities.Account;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.mappers.AccountMapper;
import com.example.GftApplication.repositories.AccountRepository;
import com.example.GftApplication.repositories.CustomerRepository;
import com.example.GftApplication.resolver.account.AccountResolver;
import com.example.GftApplication.resolver.account.AccountUpdateDTOResolver;
import com.example.GftApplication.resolver.customer.CustomerResolver;
import com.example.GftApplication.services.AccountService;
import com.example.GftApplication.services.impl.AccountServiceImpl;
import com.example.GftApplication.utils.AgencyUtils;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(AccountResolver.class)
@ExtendWith(CustomerResolver.class)
@ExtendWith(AccountUpdateDTOResolver.class)
public class AccountServiceTest {

    private AccountService sut;

    @Mock
    private  AccountRepository accountRepositoryMock;

    @Mock
    private  AccountMapper accountMapperMock;

    @Mock
    private  CustomerRepository customerRepositoryMock;

    @Mock
    private  AgencyUtils agencyUtilsMock;


    @BeforeEach
    void setup() {
        this.sut = new AccountServiceImpl(accountRepositoryMock, accountMapperMock, customerRepositoryMock, agencyUtilsMock);
    }

    @Test
    @DisplayName("Should save new account")
    void should_save_new_account(Account account, Customer customer) throws NotFoundException {
        final var accountCaptor = ArgumentCaptor.forClass(Account.class);

        when(this.customerRepositoryMock.findById(customer.getId())).thenReturn(Optional.of(customer));
        when(this.agencyUtilsMock.generateUniqueAgency()).thenReturn(account.getAgency());

        this.sut.create(customer.getId());

        verify(accountRepositoryMock).save(accountCaptor.capture());

        final var accountCap = accountCaptor.getValue();

        assertEquals(account.getAgency(), accountCap.getAgency());
        assertEquals(account.getStatus(), accountCap.getStatus());
        assertEquals(account.getCustomer(), accountCap.getCustomer());
    }


    @Test
    @DisplayName("Should find account with success")
    void should_find_account_by_id(Account account) throws NotFoundException {

        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(account));

        this.sut.findById(anyLong());

        verify(this.accountRepositoryMock, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Should update account with correct values")
    void should_update_account_with_correct_values(AccountUpdateDTO accountUpdateDTO, Account account) throws NotFoundException {
        final var accountCaptor = ArgumentCaptor.forClass(Account.class);

        when(this.accountRepositoryMock.findById(account.getId())).thenReturn(Optional.of(account));
        doAnswer(invocation -> {
            Account dtoAccount = invocation.getArgument(1);
            dtoAccount.setBalance(accountUpdateDTO.balance());
            dtoAccount.setStatus(accountUpdateDTO.status());
            return null;
        }).when(this.accountMapperMock).updateAccountFromDto(accountUpdateDTO, account);

        this.sut.updateAccount(account.getId(), accountUpdateDTO);

        verify(this.accountRepositoryMock, times(1)).findById(account.getId());
        verify(this.accountMapperMock).updateAccountFromDto(accountUpdateDTO, account);
        verify(this.accountRepositoryMock).save(accountCaptor.capture());

        Account accountCap = accountCaptor.getValue();
        assertEquals(accountUpdateDTO.balance(), accountCap.getBalance());
        assertEquals(accountUpdateDTO.status(), accountCap.getStatus());
    }

    @Test
    @DisplayName("Should paginated filtered accounts")
    void should_paginated_filtered_accounts() {
        Specification<Account> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Pageable pageable = PageRequest.of(0, 10);

        List<Account> fakeAccounts = new ArrayList<>();

        Page<Account> fakeAccountPage = new PageImpl<>(fakeAccounts, pageable, 0);

        when(accountRepositoryMock.findAll(spec, pageable)).thenReturn(fakeAccountPage);

        Page<AccountReaderDTO> result = this.sut.getPagedFiltered(spec, pageable);

        verify(accountRepositoryMock).findAll(spec, pageable);

        assertEquals(fakeAccountPage.map(accountMapperMock::accountToAccountReadDTO), result);
    }

    @Test
    @DisplayName("Should return a list of account")
    void should_return_a_list_of_account(Account account)  {
        List<Account> accountList = List.of(account);

        when(this.accountRepositoryMock.findAll()).thenReturn(accountList);

        this.sut.findAll();

        verify(this.accountRepositoryMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Should delete account")
    void should_delete_account(Account account) throws NotFoundException {
        final var accountCaptor = ArgumentCaptor.forClass(Account.class);

        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(account));

        this.sut.softDelete(anyLong());

        verify(this.accountRepositoryMock).save(accountCaptor.capture());

        final var accountCap = accountCaptor.getValue();
        assertEquals(account.getDeletedAt(), accountCap.getDeletedAt());
    }
}
