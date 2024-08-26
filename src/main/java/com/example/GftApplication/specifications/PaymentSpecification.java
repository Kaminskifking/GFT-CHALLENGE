package com.example.GftApplication.specifications;

import com.example.GftApplication.entities.Account;
import com.example.GftApplication.entities.Payment;
import jakarta.persistence.criteria.Join;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PaymentSpecification {
    @And({
            @Spec(path = "transferValue", spec = Equal.class),
    })
    public interface PaymentSpec extends Specification<Payment> {}

    public static Specification<Payment> filterPaymentByAgency(String agency) {
        return (root, query, builder) -> {
            Join<Payment, Account> accountPayerJoin = root.join("accountPayer");
            Join<Payment, Account> accountRecipientJoin = root.join("accountRecipient");
            return builder.or(
                    builder.like(builder.upper(accountPayerJoin.get("agency")), "%" + agency.toUpperCase() + "%"),
                    builder.like(builder.upper(accountRecipientJoin.get("agency")), "%" + agency.toUpperCase() + "%")
            );
        };
    }
}
