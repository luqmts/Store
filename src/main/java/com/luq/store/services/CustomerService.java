package com.luq.store.services;

import java.time.LocalDateTime;
import java.util.List;

import com.luq.store.dto.request.customer.CustomerRegisterDTO;
import com.luq.store.dto.request.customer.CustomerUpdateDTO;
import com.luq.store.dto.response.customer.CustomerResponseDTO;
import com.luq.store.exceptions.NotFoundException;
import com.luq.store.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.luq.store.domain.Customer;
import com.luq.store.repositories.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository cRepository;
    @Autowired
    private CustomerMapper cMapper;
    
    public List<CustomerResponseDTO> getAll() {
        return cMapper.toDTOList(cRepository.findAll());
    }

    public List<CustomerResponseDTO> getAllSorted(String sortBy, String direction, String name) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        if (name != null)
            return cMapper.toDTOList(cRepository.findByNameIgnoreCase(sort, name));

        return cMapper.toDTOList(cRepository.findAll(sort));
    }

    public CustomerResponseDTO getById(int id) {
        Customer customer = cRepository.findById(id).orElse(null);
        assert customer != null;
        return cMapper.toDTO(customer);
    }

    public CustomerResponseDTO register(CustomerRegisterDTO data) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Customer customer = cMapper.toEntity(data);

        customer.setModifiedBy(authentication.getName());
        customer.setModified(LocalDateTime.now());
        customer.setCreatedBy(authentication.getName());
        customer.setCreated(LocalDateTime.now());

        customer = cRepository.save(customer);
        return cMapper.toDTO(customer);
    }

    public CustomerResponseDTO update(int id, CustomerUpdateDTO data) {
        Customer customer = cRepository.findById(id).orElse(null);
        if (customer == null) throw new NotFoundException("Customer not found");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        customer.setName(data.name());

        customer.setModifiedBy(authentication.getName());
        customer.setModified(LocalDateTime.now());

        return cMapper.toDTO(cRepository.save(customer));
    }

    public void delete(int id) {
        cRepository.deleteById(id);
    }
}
