package com.example.GftApplication.repositories;

import com.example.GftApplication.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    boolean existsByDocument(String document);

    Optional<Object> findByDocument(String document);
}