package com.projeto.e_commerce.address;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<ResponseAddressDto> create(@RequestBody @Valid RequestAddressDto req) {
        ResponseAddressDto address = addressService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(address);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<ResponseAddressDto>> findByCustomer(@PathVariable UUID customerId) {
        List<ResponseAddressDto> addresses = addressService.findByCustomer(customerId);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAddressDto> findById(@PathVariable UUID id) {
        ResponseAddressDto address = addressService.findById(id);
        return ResponseEntity.ok(address);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseAddressDto> update(@PathVariable UUID id, @RequestBody @Valid RequestAddressDto req) {
        ResponseAddressDto updated = addressService.update(id, req);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
