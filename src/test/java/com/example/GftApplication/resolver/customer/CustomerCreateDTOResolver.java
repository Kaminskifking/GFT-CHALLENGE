package com.example.GftApplication.resolver.customer;

import com.example.GftApplication.dtos.Customer.CustomerCreateDTO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class CustomerCreateDTOResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == CustomerCreateDTO.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new CustomerCreateDTO(
                "Ash Ketchum",
                "05148764806",
                "Cidade de Pallet",
                "StrongPassword951!"
                );
    }
}
