package com.example.GftApplication.specifications;

import com.example.GftApplication.entities.Account;
import com.example.GftApplication.entities.Customer;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;


public class SpecificationTemplate {

    @And({
            @Spec(path = "name", spec = LikeIgnoreCase.class),
            @Spec(path = "document", spec = LikeIgnoreCase.class),
            @Spec(path = "address", spec = LikeIgnoreCase.class),
    })
    public interface CustomerSpec extends Specification<Customer> {}

}
