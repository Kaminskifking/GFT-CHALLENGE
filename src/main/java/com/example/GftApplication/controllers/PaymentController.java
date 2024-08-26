package com.example.GftApplication.controllers;

import com.example.GftApplication.configs.security.UserDetailsImpl;
import com.example.GftApplication.dtos.Payment.PaymentDTO;
import com.example.GftApplication.dtos.Payment.PaymentReadDTO;
import com.example.GftApplication.entities.Payment;
import com.example.GftApplication.exceptions.customs.BadRequestExceptions;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.services.PaymentService;
import com.example.GftApplication.specifications.PaymentSpecification;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{id}")
    public ResponseEntity<Object> create(@PathVariable("id") final Long id, @Valid @RequestBody final PaymentDTO paymentDTO, Authentication authentication) throws BadRequestExceptions, NotFoundException {
        var customerDocument = ((UserDetailsImpl) authentication.getPrincipal()).getDocument();
        paymentService.create(id, paymentDTO, customerDocument);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentReadDTO> findById(@PathVariable(value = "id") final Long id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.findById(id));
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<PaymentReadDTO>> getPagedFiltered(final PaymentSpecification.PaymentSpec spec,
                                                                 @RequestParam(value = "agency", required = false) String agency,
                                                                   @PageableDefault(
                                                                           page = 0,
                                                                           size = 10,
                                                                           sort = "id",
                                                                           direction = Sort.Direction.DESC) final Pageable pageable) {
        Specification<Payment> combinedSpec = Specification.where(spec);

        if (agency != null) {
            combinedSpec = combinedSpec.and(PaymentSpecification.filterPaymentByAgency(agency));
        }

        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPagedFiltered(combinedSpec, pageable));
    }

    @GetMapping("/all")
    public ResponseEntity<List<PaymentReadDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>  softDelete(@PathVariable("id") final Long id) throws NotFoundException {
        paymentService.softDelete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
