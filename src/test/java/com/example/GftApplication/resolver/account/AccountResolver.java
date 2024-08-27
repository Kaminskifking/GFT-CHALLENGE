package com.example.GftApplication.resolver.account;

import com.example.GftApplication.entities.Account;
import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.enums.AccountStatus;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class AccountResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Account.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Customer customer = Customer.builder()
                .id(1L)
                .name("Ash Ketchum")
                .address("Cidade de Pallet")
                .password("StrongPassword951!")
                .document("05148764806")
                .build();

        return Account.builder()
                .id(1L)
                .status(AccountStatus.ACTIVE)
                .agency("3456")
                .balance(10.00)
                .customer(customer)
                .build();
    }
}
