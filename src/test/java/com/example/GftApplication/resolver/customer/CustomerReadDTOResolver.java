package com.example.GftApplication.resolver.customer;

import com.example.GftApplication.dtos.Customer.CustomerCreateDTO;
import com.example.GftApplication.dtos.Customer.CustomerReadDTO;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class CustomerReadDTOResolver implements ParameterResolver {
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == CustomerReadDTO.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new CustomerReadDTO(
                1L,
                "Ash Ketchum",
                "05148764806",
                "Cidade de Pallet"
        );
    }
}
