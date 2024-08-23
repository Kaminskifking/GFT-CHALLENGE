package com.example.GftApplication.services;

import com.example.GftApplication.dtos.CustomerCreateDTO;
import com.example.GftApplication.dtos.CustomerReadDTO;
import com.example.GftApplication.exceptions.UniqueConstraintViolationException;
import jakarta.persistence.EntityNotFoundException;

public interface CustomerService {

    void create(CustomerCreateDTO customerCreateDto) throws UniqueConstraintViolationException;

    CustomerReadDTO findById(Long id) throws EntityNotFoundException;
}
