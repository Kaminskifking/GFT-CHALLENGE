package com.example.GftApplication.controllers;

import com.example.GftApplication.dtos.CustomerCreateDTO;
import com.example.GftApplication.dtos.CustomerReadDTO;
import com.example.GftApplication.dtos.CustomerUpdateDTO;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.exceptions.UniqueConstraintViolationException;
import com.example.GftApplication.services.CustomerService;
import com.example.GftApplication.specifications.SpecificationTemplate;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody final CustomerCreateDTO customerController) throws UniqueConstraintViolationException {
        customerService.create(customerController);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerReadDTO> findById(@PathVariable(value = "id") final Long id) throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findById(id));
    }


    @GetMapping("/paged")
    public ResponseEntity<Page<CustomerReadDTO>> getPagedFiltered(final SpecificationTemplate.CustomerSpec spec,
                                                                       @PageableDefault(
                                                                               page = 0,
                                                                               size = 10,
                                                                               sort = "id",
                                                                               direction = Sort.Direction.DESC) final Pageable pageable) {
        Specification<Customer> combinedSpec = Specification.where(spec);

        return ResponseEntity.status(HttpStatus.OK).body(customerService.getPagedFiltered(combinedSpec, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateCustomer(@PathVariable("id") final Long id,
                                         @RequestBody @Valid final CustomerUpdateDTO customerUpdateDTO) throws EntityNotFoundException {
        customerService.updateCustomer(id, customerUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Customer Updated Successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>  softDelete(@PathVariable("id") final Long id) throws EntityNotFoundException {
        customerService.softDelete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
