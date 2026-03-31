package com.projeto.e_commerce.address;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto.e_commerce.customer.Customer;
import com.projeto.e_commerce.customer.CustomerRepository;
import com.projeto.e_commerce.exception.ResourceNotFoundException;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    public AddressService(AddressRepository addressRepository, CustomerRepository customerRepository) {
        this.addressRepository = addressRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    public ResponseAddressDto create(RequestAddressDto req) {
        Customer customer = customerRepository.findById(req.customerId())
            .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + req.customerId()));

        Address address = Address.builder()
            .street(req.street())
            .number(req.number())
            .complement(req.complement())
            .neighborhood(req.neighborhood())
            .city(req.city())
            .state(req.state().toUpperCase())
            .zipCode(req.zipCode())
            .customer(customer)
            .build();

        Address savedAddress = addressRepository.save(address);

        return new ResponseAddressDto(savedAddress);
    }

    @Transactional(readOnly = true)
    public List<ResponseAddressDto> findByCustomer(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Cliente não encontrado com ID: " + customerId);
        }

        return addressRepository.findAllByCustomerId(customerId).stream()
            .map(ResponseAddressDto::new)
            .toList();
    }

    @Transactional(readOnly = true)
    public ResponseAddressDto findById(UUID id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com ID: " + id));

        return new ResponseAddressDto(address);
    }

    @Transactional
    public ResponseAddressDto update(UUID id, RequestAddressDto req) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com ID: " + id));

        address.setStreet(req.street());
        address.setNumber(req.number());
        address.setComplement(req.complement());
        address.setNeighborhood(req.neighborhood());
        address.setCity(req.city());
        address.setState(req.state().toUpperCase());
        address.setZipCode(req.zipCode());

        Address updatedAddress = addressRepository.save(address);

        return new ResponseAddressDto(updatedAddress);
    }

    @Transactional
    public void delete(UUID id) {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com ID: " + id));

        addressRepository.delete(address);
    }
}
