package com.api.rest.service;

import com.api.rest.model.dto.AddressDTO;
import com.api.rest.model.entities.Address;

public interface AddressService {
    Address getAddressById(Long addressId);
    Address updateAddress(Address address);
    Address mapToAddress(AddressDTO addressDTO);

    Address createAddress(AddressDTO addressDTO);
}
