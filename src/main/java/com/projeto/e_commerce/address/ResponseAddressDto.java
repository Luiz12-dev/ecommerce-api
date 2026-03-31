package com.projeto.e_commerce.address;

import java.util.UUID;

public record ResponseAddressDto(
    UUID id,
    String street,
    String number,
    String complement,
    String neighborhood,
    String city,
    String state,
    String zipCode,
    UUID customerId
) {

    public ResponseAddressDto(Address address) {
        this(
            address.getId(),
            address.getStreet(),
            address.getNumber(),
            address.getComplement(),
            address.getNeighborhood(),
            address.getCity(),
            address.getState(),
            address.getZipCode(),
            address.getCustomer().getId()
        );
    }
}
