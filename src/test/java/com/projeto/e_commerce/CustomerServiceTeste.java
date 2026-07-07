package com.projeto.e_commerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.projeto.e_commerce.customer.Customer;
import com.projeto.e_commerce.customer.CustomerRepository;
import com.projeto.e_commerce.customer.CustomerRequestDto;
import com.projeto.e_commerce.customer.CustomerResponseDto;
import com.projeto.e_commerce.customer.CustomerService;
import com.projeto.e_commerce.exception.DuplicateResourceException;
import com.projeto.e_commerce.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTeste {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;


    @Test
    @DisplayName("Deve criar um customer com sucesso")
    void shouldCreateNewCustomerSuccessfully() {
        CustomerRequestDto requestDto = new CustomerRequestDto("Luiz", "luiz@email.com");

        Customer savedCustomer = Customer.builder()
            .id(UUID.randomUUID())
            .name("Luiz")
            .email("luiz@email.com")
            .build();

        when(customerRepository.findByEmail(requestDto.email())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        CustomerResponseDto res = customerService.createCustomer(requestDto);


        assertNotNull(res);
        assertEquals("Luiz", res.name());
        assertEquals("luiz@email.com", res.email());

        verify(customerRepository, times(1)).save(any(Customer.class));

    }


    @Test
    @DisplayName("Deve falhar em criar um customer quando email duplicado")
    void ShouldFailToCreateNewCustommerWhenDuplicatedEmail() {
        CustomerRequestDto requestDto = new CustomerRequestDto("Pedro", "luiz@email.com");

        Customer existingCustomer = Customer.builder()
            .id(UUID.randomUUID())
            .name("Pedro")
            .email("luiz@email.com")
            .build();

        when(customerRepository.findByEmail(requestDto.email())).thenReturn(Optional.of(existingCustomer));

        assertThrows(DuplicateResourceException.class, () -> {
            customerService.createCustomer(requestDto);
        });

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    @DisplayName("Encontra pelo id e retorna um responseDto")
    void findByIdWhenIdExistsReturnsResponseDto(){

        UUID idBuscado = UUID.randomUUID();

        Customer customer = Customer.builder()
            .id(idBuscado)
            .name("Luiz")
            .email("luiz@email.com")
            .build();

        when(customerRepository.findById(idBuscado)).thenReturn(Optional.of(customer));

        CustomerResponseDto res = customerService.findById(idBuscado);

        assertNotNull(res);
        assertEquals("Luiz", res.name());
        assertEquals(idBuscado, res.id());


        verify(customerRepository, times(1)).findById(idBuscado);


    }

    @Test
    void findByIdWhenIdDoesNotExistThrowsResourceNotFoundException(){

        UUID idBuscado = UUID.randomUUID();

        when(customerRepository.findById(idBuscado)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->{
            customerService.findById(idBuscado);
        });

        verify(customerRepository, times(1)).findById(idBuscado);
    }

    @Test
    void findAllReturnListOfResponseDto(){

        Customer customer = Customer.builder()
            .id(UUID.randomUUID())
            .name("luiz")
            .email("luiz@email.com")
            .build();

        List<Customer> all = List.of(customer);

        
        when(customerRepository.findAll()).thenReturn(all);


        List<CustomerResponseDto> res = customerService.findAll();

        assertNotNull(res);
        assertEquals(1, res.size());
        assertEquals("luiz", res.get(0).name());
        assertEquals("luiz@email.com", res.get(0).email());


        verify(customerRepository, times(1)).findAll();

    }


    @Test
    void shouldReturnEmptyListWhenNoCustomerFound(){

        List<Customer> all = List.of();


        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

       List<CustomerResponseDto> res = customerService.findAll();

       assertNotNull(all);
       assertTrue(res.isEmpty());
    }


    @Test
    void shouldUpdateCustomerSuccessfully(){

        UUID id = UUID.randomUUID();

        Customer customer = Customer.builder().id(id).name("luiz").email("luiz@email.com").build();

        CustomerRequestDto reqAtualizar = new CustomerRequestDto("antonio", "antonio@email.com");

        Customer updatedCustomer = Customer.builder().id(id).name("antonio").email("antonio@email.com").build();

        when(customerRepository.findById(id)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        CustomerResponseDto res = customerService.update(id, reqAtualizar);

        assertNotNull(res);

        assertEquals("antonio", res.name()); 
        assertEquals("antonio@email.com", res.email());

        
        verify(customerRepository, times(1)).findById(id);
        verify(customerRepository, times(1)).save(any(Customer.class));

    }




    
}
