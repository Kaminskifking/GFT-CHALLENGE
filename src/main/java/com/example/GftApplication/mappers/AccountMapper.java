package com.example.GftApplication.mappers;

import com.example.GftApplication.dtos.Account.AccountReaderDTO;
import com.example.GftApplication.dtos.Account.AccountUpdateDTO;
import com.example.GftApplication.entities.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AccountMapper {
    AccountReaderDTO accountToAccountReadDTO(Account account);

    void updateAccountFromDto(AccountUpdateDTO dto, @MappingTarget Account account);

}
