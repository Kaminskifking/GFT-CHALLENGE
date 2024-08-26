package com.example.GftApplication.controllers;

import com.example.GftApplication.configs.security.UserDetailsImpl;
import com.example.GftApplication.dtos.Account.AccountReaderDTO;
import com.example.GftApplication.dtos.Account.AccountUpdateDTO;
import com.example.GftApplication.entities.Account;
import com.example.GftApplication.exceptions.customs.NotFoundException;
import com.example.GftApplication.services.AccountService;
import com.example.GftApplication.specifications.AccountSpecification;
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
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {

    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Object> create(Authentication authentication) throws NotFoundException {
        var customerId = ((UserDetailsImpl) authentication.getPrincipal()).getId();
        accountService.create(customerId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountReaderDTO> findById(@PathVariable(value = "id") final Long id) throws NotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountReaderDTO>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(accountService.findAll());
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<AccountReaderDTO>> getPagedFiltered(final AccountSpecification.AccountSpec spec,
                                                                   @PageableDefault(
                                                                           page = 0,
                                                                           size = 10,
                                                                           sort = "id",
                                                                           direction = Sort.Direction.DESC) final Pageable pageable) {
        Specification<Account> combinedSpec = Specification.where(spec);

        return ResponseEntity.status(HttpStatus.OK).body(accountService.getPagedFiltered(combinedSpec, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAccount(@PathVariable("id") final Long id,
                                                 @RequestBody @Valid final AccountUpdateDTO accountUpdateDTO) throws NotFoundException {
        accountService.updateAccount(id, accountUpdateDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Account Updated Successful");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object>  softDelete(@PathVariable("id") final Long id) throws NotFoundException {
        accountService.softDelete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
