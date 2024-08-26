package com.example.GftApplication.unit.service;

import com.example.GftApplication.annotation.DocumentValidator;
import com.example.GftApplication.mappers.CustomerMapper;
import com.example.GftApplication.repositories.CustomerRepository;
import com.example.GftApplication.resolver.customer.CustomerResolver;
import com.example.GftApplication.services.CustomerService;
import com.example.GftApplication.services.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
@ExtendWith(CustomerResolver.class)
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
}
