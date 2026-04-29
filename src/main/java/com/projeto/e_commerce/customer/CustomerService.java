package com.projeto.e_commerce.customer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.e_commerce.exception.DuplicateResourceException;
import com.projeto.e_commerce.exception.ResourceNotFoundException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponseDto createCustomer(CustomerRequestDto requestDto) {
        if (customerRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new DuplicateResourceException("Já existe um cliente com o e-mail: " + requestDto.email());
        }

        Customer customer = Customer.builder()
            .name(requestDto.name())
            .email(requestDto.email())
            .build();

        Customer savedCustomer = customerRepository.save(customer);

        return new CustomerResponseDto(savedCustomer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findAll() {
        return customerRepository.findAll().stream()
            .map(CustomerResponseDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerResponseDto findById(UUID id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        return new CustomerResponseDto(customer);
    }

    @Transactional
    public CustomerResponseDto update(UUID id, CustomerRequestDto requestDto) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        customerRepository.findByEmail(requestDto.email())
            .filter(existing -> !existing.getId().equals(id))
            .ifPresent(existing -> {
                throw new DuplicateResourceException("Já existe um cliente com o e-mail: " + requestDto.email());
            });

        customer.setName(requestDto.name());
        customer.setEmail(requestDto.email());

        Customer updatedCustomer = customerRepository.save(customer);

        return new CustomerResponseDto(updatedCustomer);
    }

    @Transactional
    public void delete(UUID id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));

        customerRepository.delete(customer);
    }
}
