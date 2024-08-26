package com.example.GftApplication.repositories;

import com.example.GftApplication.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
    boolean existsByAgency(String agency);

    @Query("SELECT a FROM Account a WHERE a.id = :accountId AND a.customer.document = :customerDocument")
    Optional<Account> findByAccountIdAndCustomerDocument(
            @Param("accountId") Long accountId,
            @Param("customerDocument") String customerDocument
    );
}
