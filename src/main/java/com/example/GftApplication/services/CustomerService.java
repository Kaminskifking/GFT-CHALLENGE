package com.example.GftApplication.services;

import com.example.GftApplication.dtos.CustomerCreateDTO;
import com.example.GftApplication.dtos.CustomerReadDTO;
import com.example.GftApplication.dtos.CustomerUpdateDTO;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.exceptions.UniqueConstraintViolationException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface CustomerService {

    void create(CustomerCreateDTO customerCreateDto) throws UniqueConstraintViolationException;

    CustomerReadDTO findById(Long id) throws EntityNotFoundException;

    Page<CustomerReadDTO> getPagedFiltered(final Specification<Customer> spec, final Pageable pageable);

    void updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO);

    void softDelete(final Long id) throws EntityNotFoundException;

}
