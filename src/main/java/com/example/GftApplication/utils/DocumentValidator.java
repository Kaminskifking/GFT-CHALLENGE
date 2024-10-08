package com.example.GftApplication.utils;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import org.springframework.stereotype.Component;

@Component
public class DocumentValidator {

    private final CPFValidator cpfValidator = new CPFValidator();
    private final CNPJValidator cnpjValidator = new CNPJValidator();

    public boolean isValidDocument(String document) {
        document = document.replaceAll("\\D", "");

        if (document.length() >= 11 && document.length() < 14) {
            try {
                cpfValidator.assertValid(document);
                return true;
            } catch (InvalidStateException e) {
                throw new IllegalArgumentException("Invalid CPF");
            }
        } else if (document.length() == 14) {
            try {
                cnpjValidator.assertValid(document);
                return true;
            } catch (InvalidStateException e) {
                throw new IllegalArgumentException("Invalid CNPJ");
            }
        }
        return false;
    }
}
