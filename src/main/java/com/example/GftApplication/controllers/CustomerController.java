package com.example.GftApplication.controllers;

import com.example.GftApplication.dtos.CustomerCreateDTO;
import com.example.GftApplication.dtos.CustomerReadDTO;
import com.example.GftApplication.exceptions.UniqueConstraintViolationException;
import com.example.GftApplication.services.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid final CustomerCreateDTO customerController) throws UniqueConstraintViolationException {
        customerService.create(customerController);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerReadDTO> findById(@PathVariable(value = "id") final Long id) throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findById(id));
    }

}
