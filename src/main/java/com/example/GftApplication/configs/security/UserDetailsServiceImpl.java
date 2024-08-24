package com.example.GftApplication.configs.security;

import com.example.GftApplication.entities.Customer;
import com.example.GftApplication.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String document) throws UsernameNotFoundException {
        Customer customerModel = (Customer) customerRepository.findByDocument(document)
                .orElseThrow(() -> new UsernameNotFoundException("Document Not Found: " + document));

        return UserDetailsImpl.build(customerModel);
    }
}