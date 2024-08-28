package com.example.GftApplication.resolver.payment;

import com.example.GftApplication.dtos.Payment.PaymentCreateDTO;
import com.example.GftApplication.dtos.Payment.PaymentReadDTO;
import com.example.GftApplication.entities.Account;
import com.example.GftApplication.enums.AccountStatus;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class PaymentReaderDTOResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == PaymentReaderDTOResolver.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

        Account accountPayer = Account.builder()
                .id(1L)
                .status(AccountStatus.ACTIVE)
                .agency("3456")
                .balance(15.00)
                .build();

        Account accountRecipient = Account.builder()
                .id(2L)
                .status(AccountStatus.ACTIVE)
                .agency("3457")
                .balance(15.00)
                .build();

        return new PaymentReadDTO(
                1L,
                accountPayer,
                accountRecipient,
                15.00
        );
    }
}
