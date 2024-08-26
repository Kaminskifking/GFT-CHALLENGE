package com.example.GftApplication.resolver.customer;

import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.entities.Payment;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class CustomerResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Customer.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return Customer.builder()
                .id(1L)
                .name("Ash Ketchum")
                .address("Cidade de Pallet")
                .password("StrongPassword951!")
                .document("05148764806")
                .build();
    }
}
