package com.example.GftApplication.resolver.account;

import com.example.GftApplication.dtos.Account.AccountUpdateDTO;
import com.example.GftApplication.enums.AccountStatus;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class AccountUpdateDTOResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == AccountUpdateDTO.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new AccountUpdateDTO(
                15.00,
                AccountStatus.INACTIVE
        );
    }
}
