package com.luq.store.services;

import com.luq.store.domain.Customer;
import com.luq.store.dto.request.customer.CustomerRegisterDTO;
import com.luq.store.dto.request.customer.CustomerUpdateDTO;
import com.luq.store.dto.response.customer.CustomerResponseDTO;
import com.luq.store.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    private CustomerResponseDTO fakeCustomer1Response, result;
    private CustomerRegisterDTO fakeCustomer1Register, fakeCustomer2Register;
    private CustomerUpdateDTO fakeCustomer1Update;
    private Customer fakeCustomer1, fakeCustomer2;

    @BeforeEach
    public void setUp(){
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now();

        fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        fakeCustomer2 = new Customer(2, "Test Customer 02", user, now, user, now);

        fakeCustomer1Response = new CustomerResponseDTO(1, "Test Customer 01", user, now, user, now);

        fakeCustomer1Register = new CustomerRegisterDTO("Test Customer 01");
        fakeCustomer2Register = new CustomerRegisterDTO("Test Customer 02");
        fakeCustomer1Update = new CustomerUpdateDTO("Test Customer 02");
    }
    
    @Test
    @DisplayName("Test if Customer is being registered correctly")
    public void testRegisterCustomer(){
        when(cRepository.save(fakeCustomer1)).thenReturn(fakeCustomer1);
        result = cService.register(fakeCustomer1Register);

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
        when(cRepository.save(fakeCustomer1)).thenReturn(fakeCustomer1);
        when(cRepository.findById(fakeCustomer1.getId())).thenReturn(Optional.ofNullable(fakeCustomer1));
        when(cRepository.save(fakeCustomer2)).thenReturn(fakeCustomer2);

        cService.register(fakeCustomer1Register);
        result = cService.update(fakeCustomer1.getId(), fakeCustomer1Update);

        assertAll(
            () -> verify(cRepository, times(2)).save(fakeCustomer1),
            () -> verify(cRepository, times(2)).save(fakeCustomer2),
            () -> verify(cRepository, atMostOnce()).findById(fakeCustomer1.getId()),
            () -> assertNotNull(result),
            () -> assertInstanceOf(CustomerResponseDTO.class, result),
            () -> assertEquals(fakeCustomer1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Customer is being deleted correctly")
    public void testDeleteCustomer(){
        when(cRepository.save(fakeCustomer1)).thenReturn(fakeCustomer1);
        cService.register(fakeCustomer1Register);

        cService.delete(fakeCustomer1.getId());

        verify(cRepository, atMostOnce()).deleteById(fakeCustomer1.getId());
    }

    @Test
    @DisplayName("Test if all Customers registered are being returned on method getALl()")
    public void testGetAllCustomers() {
        when(cRepository.save(fakeCustomer1)).thenReturn(fakeCustomer1);
        when(cRepository.save(fakeCustomer2)).thenReturn(fakeCustomer2);
        when(cRepository.findAll()).thenReturn(List.of(fakeCustomer1, fakeCustomer2));

        cService.register(fakeCustomer1Register);
        cService.register(fakeCustomer2Register);
        assertEquals(2, cService.getAll().size());
        verify(cRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Customer is being returned by id on method getById()")
    public void testGetCustomerById(){
        when(cRepository.save(fakeCustomer1)).thenReturn(fakeCustomer1);
        when(cRepository.findById(1)).thenReturn(Optional.ofNullable(fakeCustomer1));

        cService.register(fakeCustomer1Register);
        result = cService.getById(1);
        assertAll(
            () -> verify(cRepository, atMostOnce()).findById(1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(CustomerResponseDTO.class, result),
            () -> assertEquals(fakeCustomer1Response, result)
        );
    }
}