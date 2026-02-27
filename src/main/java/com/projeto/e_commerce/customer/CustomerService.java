package com.projeto.e_commerce.customer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }


    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto requestDto){
        if(customerRepository.findCustomerByEmail(requestDto.email()).isPresent()){
            throw new IllegalArgumentException("Este Email já esta em uso !");
        }

    Customer customer = Customer.builder()
    .name(requestDto.name())
    .email(requestDto.email()).build();

    Customer savedCustomer = customerRepository.save(customer);

    return new CustomerResponseDto(savedCustomer);
    
    }
}
