package com.example.GftApplication.mappers;

import com.example.GftApplication.dtos.CustomerReadDTO;
import com.example.GftApplication.entities.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

   CustomerReadDTO customerToCustomerReadDTO(Customer customer);

}
