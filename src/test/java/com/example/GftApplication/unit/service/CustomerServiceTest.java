package com.example.GftApplication.unit.service;

import com.example.GftApplication.utils.DocumentValidator;
import com.example.GftApplication.dtos.Customer.CustomerCreateDTO;
import com.example.GftApplication.dtos.Customer.CustomerReadDTO;
import com.example.GftApplication.dtos.Customer.CustomerUpdateDTO;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.exceptions.customs.UniqueConstraintViolationException;
import com.example.GftApplication.mappers.CustomerMapper;
import com.example.GftApplication.repositories.CustomerRepository;
import com.example.GftApplication.resolver.customer.CustomerCreateDTOResolver;
import com.example.GftApplication.resolver.customer.CustomerReadDTOResolver;
import com.example.GftApplication.resolver.customer.CustomerResolver;
import com.example.GftApplication.resolver.customer.CustomerUpdateDTOResolver;
import com.example.GftApplication.services.CustomerService;
import com.example.GftApplication.services.impl.CustomerServiceImpl;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(CustomerResolver.class)
@ExtendWith(CustomerCreateDTOResolver.class)
@ExtendWith(CustomerReadDTOResolver.class)
@ExtendWith(CustomerUpdateDTOResolver.class)
public class CustomerServiceTest {

    private CustomerService sut;

    @Mock
    private  CustomerMapper customerMapperMock;

    @Mock
    private  PasswordEncoder passwordEncoderMock;

    @Mock
    private  CustomerRepository customerRepositoryMock;

    @Mock
    private DocumentValidator documentValidatorMock;



    @BeforeEach
    void setup() {
        this.sut = new CustomerServiceImpl(customerMapperMock, passwordEncoderMock, customerRepositoryMock, documentValidatorMock);
    }

    @Test
    @DisplayName("Should save new customer")
    void should_save_new_customer(Customer customer, CustomerCreateDTO customerCreateDTO) throws UniqueConstraintViolationException {
        final var customerCaptor = ArgumentCaptor.forClass(Customer.class);

        when(this.customerRepositoryMock.existsByDocument(customer.getDocument())).thenReturn(false);
        when(this.documentValidatorMock.isValidDocument(customer.getDocument())).thenReturn(true);


        this.sut.create(customerCreateDTO);

        verify(customerRepositoryMock).save(customerCaptor.capture());

        final var customerCaptorValue = customerCaptor.getValue();

        assertEquals(customerCreateDTO.address(), customerCaptorValue.getAddress());
        assertEquals(customerCreateDTO.name(), customerCaptorValue.getName());
        assertEquals(customerCreateDTO.document(), customerCaptorValue.getDocument());
    }

    @Test
    @DisplayName("Should return a list of customers")
    void should_return_a_list_of_customers(Customer customer)  {
        List<Customer> customerList = List.of(customer);

        when(this.customerRepositoryMock.findAll()).thenReturn(customerList);

        this.sut.findAll();

        verify(this.customerRepositoryMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Should find customer with success")
    void should_find_customer_by_id(Customer customer) throws NotFoundException {
        final var id = 1L;

        when(this.customerRepositoryMock.findById(anyLong())).thenReturn(Optional.of(customer));

        this.sut.findById(id);

        verify(this.customerRepositoryMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should paginated filtered customers")
    void should_paginated_filtered_customers() {
        Specification<Customer> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();
        Pageable pageable = PageRequest.of(0, 10);

        List<Customer> fakeCustomers = new ArrayList<>();

        Page<Customer> fakeCustomersPage = new PageImpl<>(fakeCustomers, pageable, 0);

        when(customerRepositoryMock.findAll(spec, pageable)).thenReturn(fakeCustomersPage);

        Page<CustomerReadDTO> result = this.sut.getPagedFiltered(spec, pageable);

        verify(customerRepositoryMock).findAll(spec, pageable);

        assertEquals(fakeCustomersPage.map(customerMapperMock::customerToCustomerReadDTO), result);
    }

    @Test
    @DisplayName("Should update customer with correct values")
    void should_update_customer_with_correct_values(CustomerUpdateDTO customerUpdateDTO, Customer customer) throws NotFoundException {
        final var customerCaptor = ArgumentCaptor.forClass(Customer.class);
        final Long id = 1L;

        when(this.customerRepositoryMock.findById(id)).thenReturn(Optional.of(customer));
        when(this.passwordEncoderMock.encode(customerUpdateDTO.password())).thenReturn(customerUpdateDTO.password());
        doAnswer(invocation -> {
            Customer dtoCustomer = invocation.getArgument(1);
            dtoCustomer.setName(customerUpdateDTO.name());
            dtoCustomer.setPassword(customerUpdateDTO.password()); // This will be updated before encoding
            return null;
        }).when(this.customerMapperMock).updateCustomerFromDto(customerUpdateDTO, customer);

        this.sut.updateCustomer(id, customerUpdateDTO);

        verify(this.customerRepositoryMock, times(1)).findById(id);
        verify(this.customerMapperMock).updateCustomerFromDto(customerUpdateDTO, customer);
        verify(this.customerRepositoryMock).save(customerCaptor.capture());

        Customer customerCap = customerCaptor.getValue();
        assertEquals(customerUpdateDTO.name(), customerCap.getName());
        assertEquals(customerUpdateDTO.password(), customerCap.getPassword());
    }


    @Test
    @DisplayName("Should delete customer")
    void should_delete_customer(Customer customer) throws NotFoundException {
        final var customerCaptor = ArgumentCaptor.forClass(Customer.class);
        final Long id = 1L;

        when(this.customerRepositoryMock.findById(id)).thenReturn(Optional.of(customer));

        this.sut.softDelete(id);

        verify(this.customerRepositoryMock).save(customerCaptor.capture());

        final var customerCap = customerCaptor.getValue();
        assertEquals(customer.getDeletedAt(), customerCap.getDeletedAt());
    }
}
