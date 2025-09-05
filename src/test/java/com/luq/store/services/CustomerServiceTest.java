package com.luq.store.services;

import com.luq.store.domain.Customer;
import com.luq.store.dto.request.customer.CustomerRegisterDTO;
import com.luq.store.dto.request.customer.CustomerUpdateDTO;
import com.luq.store.dto.response.customer.CustomerResponseDTO;
import com.luq.store.mapper.CustomerMapper;
import com.luq.store.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {
    @Mock
    CustomerRepository cRepository;
    @InjectMocks
    CustomerService cService;
    @Mock
    private CustomerMapper cMapper;

    private CustomerResponseDTO fakeCustomer1Response, fakeCustomer2Response, result;
    private CustomerRegisterDTO fakeCustomerRegister;
    private CustomerUpdateDTO fakeCustomerUpdate;
    private Customer fakeCustomer1, fakeCustomer2;

    Authentication authentication;
    SecurityContext securityContext;

    String user;
    LocalDateTime now;

    @BeforeEach
    public void setUp(){
        user = "Jimmy McGill";
        now = LocalDateTime.now();

        fakeCustomer1Response = new CustomerResponseDTO(1, "Test Customer 01", user, now, user, now);
        fakeCustomer2Response = new CustomerResponseDTO(1, "Test Customer 02", user, now, user, now);
        fakeCustomerRegister = new CustomerRegisterDTO("Test Customer 01");
        fakeCustomerUpdate = new CustomerUpdateDTO("Test Customer 02");
        fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        fakeCustomer2 = new Customer(1, "Test Customer 02", user, now, user, now);

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    @DisplayName("Test if Customer is being registered correctly")
    public void testRegisterCustomer(){
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(cRepository.save(fakeCustomer1)).thenReturn(fakeCustomer1);
        when(cMapper.toEntity(fakeCustomerRegister)).thenReturn(fakeCustomer1);
        when(cMapper.toDTO(fakeCustomer1)).thenReturn(fakeCustomer1Response);

        result = cService.register(fakeCustomerRegister);

        assertAll(
            () -> verify(cRepository, atMostOnce()).save(fakeCustomer1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(CustomerResponseDTO.class, result),
            () -> assertEquals(fakeCustomer1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Customer is being updated correctly")
    public void testUpdateCustomer(){
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(cRepository.findById(fakeCustomer1Response.id())).thenReturn(Optional.ofNullable(fakeCustomer1));
        when(cRepository.save(fakeCustomer2)).thenReturn(fakeCustomer2);
        when(cMapper.toDTO(fakeCustomer2)).thenReturn(fakeCustomer2Response);

        result = cService.update(fakeCustomer1Response.id(), fakeCustomerUpdate);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(CustomerResponseDTO.class, result),
            () -> assertEquals(fakeCustomer2Response, result)
        );
    }

    @Test
    @DisplayName("Test if Customer is being deleted correctly")
    public void testDeleteCustomer(){
        cService.delete(fakeCustomer1Response.id());

        verify(cRepository, atMostOnce()).deleteById(fakeCustomer1Response.id());
    }

    @Test
    @DisplayName("Test if all Customers registered are being returned on method getALl()")
    public void testGetAllCustomers() {
        when(cRepository.findAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));
        when(cMapper.toDTOList(List.of(fakeCustomer1, fakeCustomer2))).thenReturn(List.of(fakeCustomer1Response, fakeCustomer2Response));
        List<CustomerResponseDTO> result = cService.getAll();

        assertEquals(2, result.size());
        verify(cRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Customer is being returned by id on method getById()")
    public void testGetCustomerById(){
        when(cRepository.findById(1)).thenReturn(Optional.ofNullable(fakeCustomer1));
        when(cMapper.toDTO(fakeCustomer1)).thenReturn(fakeCustomer1Response);

        result = cService.getById(1);

        assertAll(
            () -> verify(cRepository, atMostOnce()).findById(1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(CustomerResponseDTO.class, result),
            () -> assertEquals(fakeCustomer1Response, result)
        );
    }
}