package com.example.GftApplication.mappers;

import com.example.GftApplication.dtos.Customer.CustomerReadDTO;
import com.example.GftApplication.dtos.Customer.CustomerUpdateDTO;
import com.example.GftApplication.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {

   CustomerReadDTO customerToCustomerReadDTO(Customer customer);

   void updateCustomerFromDto(CustomerUpdateDTO dto, @MappingTarget Customer customer);
}
