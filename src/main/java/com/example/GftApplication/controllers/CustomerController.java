package com.example.GftApplication.controllers;

import com.example.GftApplication.dtos.Customer.CustomerCreateDTO;
import com.example.GftApplication.dtos.Customer.CustomerReadDTO;
import com.example.GftApplication.dtos.Customer.CustomerUpdateDTO;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.exceptions.customs.UniqueConstraintViolationException;
import com.example.GftApplication.services.CustomerService;
import com.example.GftApplication.specifications.SpecificationTemplate;
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

import java.util.List;


@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerController {

    private CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<Object> create(@Valid @RequestBody final CustomerCreateDTO customerCreateDTO) throws UniqueConstraintViolationException, IllegalArgumentException {
        customerService.create(customerCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerReadDTO> findById(@PathVariable(value = "id") final Long id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findById(id));
    }


    @GetMapping("/all")
    public ResponseEntity<List<CustomerReadDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findAll());
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
                                         @RequestBody @Valid final CustomerUpdateDTO customerUpdateDTO) throws NotFoundException {
        customerService.updateCustomer(id, customerUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Customer Updated Successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>  softDelete(@PathVariable("id") final Long id) throws NotFoundException {
        customerService.softDelete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
