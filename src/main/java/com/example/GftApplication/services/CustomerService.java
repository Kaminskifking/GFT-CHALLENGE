package com.example.GftApplication.services;

import com.example.GftApplication.dtos.Customer.CustomerCreateDTO;
import com.example.GftApplication.dtos.CustomerReadDTO;
import com.example.GftApplication.dtos.CustomerUpdateDTO;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.exceptions.customs.UniqueConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CustomerService {

    void create(CustomerCreateDTO customerCreateDto) throws UniqueConstraintViolationException, IllegalArgumentException;

    List<CustomerReadDTO> findAll();

    CustomerReadDTO findById(Long id) throws NotFoundException;

    Page<CustomerReadDTO> getPagedFiltered(final Specification<Customer> spec, final Pageable pageable);

    void updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO) throws NotFoundException;

    void softDelete(final Long id) throws NotFoundException;

}
