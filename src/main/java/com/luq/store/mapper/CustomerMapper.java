package com.luq.store.mapper;

import com.luq.store.domain.Customer;
import com.luq.store.dto.request.customer.CustomerRegisterDTO;
import com.luq.store.dto.response.customer.CustomerResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerMapper {
    public Customer toEntity(CustomerRegisterDTO data) {
        Customer customer = new Customer();
        customer.setName(data.name());

        return customer;
    }

    public Customer toEntity(CustomerResponseDTO data) {
        return new Customer(
            data.id(),
            data.name(),
            data.createdBy(),
            data.created(),
            data.modifiedBy(),
            data.modified()
        );
    }

    public CustomerResponseDTO toDTO(Customer customer) {
        return new CustomerResponseDTO(
            customer.getId(),
            customer.getName(),
            customer.getCreatedBy(),
            customer.getCreated(),
            customer.getModifiedBy(),
            customer.getModified()
        );
    }

    public List<CustomerResponseDTO> toDTOList(List<Customer> cList) {
        return cList
            .stream()
            .map(this::toDTO)
            .toList();
    }
}
