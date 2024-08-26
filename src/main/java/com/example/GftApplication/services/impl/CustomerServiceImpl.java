package com.example.GftApplication.services.impl;

import com.example.GftApplication.annotation.DocumentValidator;
import com.example.GftApplication.dtos.Customer.CustomerCreateDTO;
import com.example.GftApplication.dtos.CustomerReadDTO;
import com.example.GftApplication.dtos.CustomerUpdateDTO;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.exceptions.customs.UniqueConstraintViolationException;
import com.example.GftApplication.mappers.CustomerMapper;
import com.example.GftApplication.repositories.CustomerRepository;
import com.example.GftApplication.services.CustomerService;
import com.example.GftApplication.entities.Customer;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;

    private final PasswordEncoder passwordEncoder;

    private final CustomerRepository customerRepository;

    private final DocumentValidator documentValidator;

    @Override
    public void create(CustomerCreateDTO customerCreateDto) throws UniqueConstraintViolationException, IllegalArgumentException {

        if (customerRepository.existsByDocument(String.valueOf(customerCreateDto.document()))) {
            throw new UniqueConstraintViolationException("A customer with the same document already exists.");
        }

        documentValidator.isValidDocument(customerCreateDto.document());

        Customer customer = new Customer();
        customer.setName(customerCreateDto.name());
        customer.setDocument(customerCreateDto.document());

        String encryptedPassword = passwordEncoder.encode(customerCreateDto.password());
        customer.setPassword(encryptedPassword);

        customer.setAddress(customerCreateDto.address());
        customerRepository.save(customer);
    }

    @Override
    public List<CustomerReadDTO> findAll() {
        return   customerRepository.findAll()
                .stream()
                .map(customerMapper::customerToCustomerReadDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerReadDTO findById(Long id) throws NotFoundException {
        final var customer = customerRepository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found"));

        return customerMapper.customerToCustomerReadDTO(customer);
    }

    @Override
    public Page<CustomerReadDTO> getPagedFiltered(final Specification<Customer> spec, final Pageable pageable) {
        Page<Customer> customerPageable = customerRepository.findAll(spec, pageable);
        return customerPageable.map(customerMapper::customerToCustomerReadDTO);
    }

    @Override
    public void updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO) throws NotFoundException {
        final var customerForUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        customerMapper.updateCustomerFromDto(customerUpdateDTO, customerForUpdate);

        if (customerUpdateDTO.password() != null) {
            customerForUpdate.setPassword(passwordEncoder.encode(customerUpdateDTO.password()));
        }

        customerRepository.save(customerForUpdate);
    }

    @Override
    public void softDelete(Long id) throws NotFoundException {
        final var customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found"));

        customer.setDeletedAt(LocalDateTime.now());
        customerRepository.save(customer);
    }

}
