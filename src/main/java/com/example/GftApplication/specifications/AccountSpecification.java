package com.example.GftApplication.specifications;

import com.example.GftApplication.entities.Account;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;


@Component
public class AccountSpecification {


    @And({
            @Spec(path = "balance", spec = Equal.class),
            @Spec(path = "agency", spec = LikeIgnoreCase.class),
            @Spec(path = "status", spec = Equal.class),
    })
    public interface AccountSpec extends Specification<Account> {}

}
