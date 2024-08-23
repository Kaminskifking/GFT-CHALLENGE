package com.example.GftApplication.annotation;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;

public class DocumentValidator {

    private final CPFValidator cpfValidator = new CPFValidator();
    private final CNPJValidator cnpjValidator = new CNPJValidator();

    public boolean isValidDocument(String document) {
        document = document.replaceAll("\\D", ""); // Remove non-numeric characters

        if (document.length() == 11) {
            try {
                cpfValidator.assertValid(document);
                return true;
            } catch (InvalidStateException e) {
                return false;
            }
        } else if (document.length() == 14) {
            try {
                cnpjValidator.assertValid(document);
                return true;
            } catch (InvalidStateException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
