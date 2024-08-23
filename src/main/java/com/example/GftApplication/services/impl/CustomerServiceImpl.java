package com.example.GftApplication.services.impl;

import com.example.GftApplication.dtos.CustomerCreateDTO;
import com.example.GftApplication.dtos.CustomerReadDTO;
import com.example.GftApplication.exceptions.UniqueConstraintViolationException;
import com.example.GftApplication.mappers.CustomerMapper;
import com.example.GftApplication.repositories.CustomerRepository;
import com.example.GftApplication.services.CustomerService;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.annotation.CustomerValidator;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;

    private final PasswordEncoder passwordEncoder;

    private final CustomerRepository customerRepository;

    private final CustomerValidator customerValidator = new CustomerValidator();

    @Override
    public void create(CustomerCreateDTO customerCreateDto) throws UniqueConstraintViolationException {

        if (customerRepository.existsByDocument(String.valueOf(customerCreateDto.document()))) {
            throw new UniqueConstraintViolationException("A customer with the same document already exists.");
        }

        customerValidator.validateCustomerCreateDto(customerCreateDto);

        Customer customer = new Customer();
        customer.setName(customerCreateDto.name());
        customer.setDocument(customerCreateDto.document().toString());

        String encryptedPassword = passwordEncoder.encode(customerCreateDto.password());
        customer.setPassword(encryptedPassword);

        customer.setAddress(customerCreateDto.address());
        customerRepository.save(customer);
    }

    @Override
    public CustomerReadDTO findById(Long id) throws EntityNotFoundException {
        final var customer = customerRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        return customerMapper.customerToCustomerReadDTO(customer);
    }
}
