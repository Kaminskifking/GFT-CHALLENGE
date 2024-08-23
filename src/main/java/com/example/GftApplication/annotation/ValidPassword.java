package com.example.GftApplication.annotation;

import com.example.GftApplication.annotation.impl.PasswordValidator;
import jakarta.validation.Constraint;

import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ TYPE, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface ValidPassword {
    String message() default "Password must be at least 8 characters long, include an uppercase letter, a lowercase letter, a digit, and a special character.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
