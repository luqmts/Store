package com.luq.store.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.luq.store.domain.*;
import com.luq.store.domain.Customer;
import com.luq.store.dto.request.order.OrderRegisterDTO;
import com.luq.store.dto.request.order.OrderUpdateDTO;
import com.luq.store.dto.response.customer.CustomerResponseDTO;
import com.luq.store.dto.response.order.OrderResponseDTO;
import com.luq.store.dto.response.product.ProductResponseDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
import com.luq.store.dto.response.supply.SupplyResponseDTO;
import com.luq.store.mapper.*;
import com.luq.store.repositories.OrderRepository;
import com.luq.store.repositories.ProductRepository;
import com.luq.store.repositories.SupplyRepository;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
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

import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository oRepository;
    @InjectMocks
    private OrderService oService;
    @Mock
    private OrderMapper oMapper;

    @Mock
    private SupplyRepository sRepository;
    @Mock
    private SupplyService supplyService;
    @Mock
    private SupplyMapper supplyMapper;

    @Mock
    private ProductRepository pRepository;
    @Mock
    private ProductService pService;
    @Mock
    private ProductMapper pMapper;

    @Mock
    private SellerService sellerService;
    @Mock
    private SellerMapper sellerMapper;

    @Mock
    private CustomerService cService;
    @Mock
    private CustomerMapper cMapper;

    private OrderResponseDTO fakeOrder1Response, fakeOrder2Response, result;
    private OrderRegisterDTO fakeOrderRegister;
    private OrderUpdateDTO fakeOrderUpdate;
    private Order fakeOrder1, fakeOrder2;
    private CustomerResponseDTO fakeCustomerResponse;
    private Supply fakeSupply;
    private SupplyResponseDTO fakeSupplyResponse;
    private SellerResponseDTO fakeSellerResponse;
    private ProductResponseDTO fakeProductResponse;
    private Product fakeProduct1;

    Authentication authentication;
    SecurityContext securityContext;

    String user;
    LocalDateTime now;



    @BeforeEach
    public void setUp(){
        user = "Jimmy McGill";
        now = LocalDateTime.now();

        fakeCustomerResponse = new CustomerResponseDTO(2, "Test Customer 02", user, now, user, now);
        Customer fakeCustomer1 = new Customer(1, "Test Customer 01", user, now, user, now);
        Customer fakeCustomer2 = new Customer(2, "Test Customer 02", user, now, user, now);

        Supplier fakeSupplier1 = new Supplier(
            1, "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111"),
            user, now, user, now
        );
        Supplier fakeSupplier2 = new Supplier(
            2, "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
            new Mail("sony@mail.com"), new Phone("11222225555"),
            user, now, user, now
        );

        fakeProductResponse = new ProductResponseDTO(
            2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
            BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );
        fakeProduct1 = new Product(
                1, "Xbox One Controller", "XOneCont", "Controller for Xbox One Console",
                BigDecimal.valueOf(200.00), fakeSupplier1, user, now, user, now
        );
        Product fakeProduct2 = new Product(
                2, "Playstation 5 Controller", "PS5Cont", "Controller for Playstation 5 Console",
                BigDecimal.valueOf(250.00), fakeSupplier2, user, now, user, now
        );

        fakeSellerResponse = new SellerResponseDTO(
            2, "Jesse Pinkman", "Jesse Pinkman@Cooking.com", "11904040404", Department.FOOD,
            user, now, user, now
        );
        Seller fakeSeller1 = new Seller(
                1, "Walter White",
                new Mail("WalterWhite@Cooking.com"), new Phone("11901010101"), Department.FOOD,
                user, now, user, now
        );
        Seller fakeSeller2 = new Seller(
                2, "Jesse Pinkman",
                new Mail("Jesse Pinkman@Cooking.com"), new Phone("11904040404"), Department.FOOD,
                user, now, user, now
        );

        fakeSupplyResponse = new SupplyResponseDTO(1, 50, fakeProduct1, user, now, user, now);
        fakeSupply = new Supply(1, 50, fakeProduct1, user, now, user, now);

        fakeOrder1Response = new OrderResponseDTO(
            1, "", BigDecimal.valueOf(400.00), BigDecimal.valueOf(200.00), OrderStatus.PENDING_PAYMENT,2, LocalDate.now(),
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
        fakeOrder2Response = new OrderResponseDTO(
            1,"", BigDecimal.valueOf(600.00), BigDecimal.valueOf(150.00), OrderStatus.PAID, 4, LocalDate.now().plusDays(5),
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
        fakeOrderRegister = new OrderRegisterDTO(
            2, LocalDate.now(), fakeProduct1.getId(), fakeSeller1.getId(), fakeCustomer1.getId()
        );
        fakeOrderUpdate = new OrderUpdateDTO(
            OrderStatus.PENDING_PAYMENT,4, LocalDate.now().plusDays(5), fakeProduct2.getId(), fakeSeller2.getId(), fakeCustomer2.getId()
        );
        fakeOrder1 = new Order(
            1, BigDecimal.valueOf(400.00), BigDecimal.valueOf(200.00), OrderStatus.PENDING_PAYMENT,
            2, LocalDate.now(), "",
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );
        fakeOrder2 = new Order(
            1, BigDecimal.valueOf(600.00), BigDecimal.valueOf(150.00), OrderStatus.PAID,
            4, LocalDate.now().plusDays(5), "",
            fakeProduct1, fakeSeller1, fakeCustomer1, user, now, user, now
        );

        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }
    
    @Test
    @DisplayName("Test if Order is being registered correctly")
    public void testRegisterOrder() throws JsonProcessingException, URISyntaxException {
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(oRepository.save(fakeOrder1)).thenReturn(fakeOrder1);
        when(oMapper.toEntity(fakeOrderRegister)).thenReturn(fakeOrder1);
        when(oMapper.toDTO(fakeOrder1)).thenReturn(fakeOrder1Response);
        when(supplyService.getByProductId(fakeOrderRegister.product_id())).thenReturn(fakeSupplyResponse);
        when(supplyMapper.toEntity(fakeSupplyResponse)).thenReturn(fakeSupply);
        when(sRepository.save(fakeSupply)).thenReturn(null);
        when(pRepository.findById(fakeOrderRegister.product_id())).thenReturn(Optional.ofNullable(fakeProduct1));

        result = oService.register(fakeOrderRegister);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(OrderResponseDTO.class, result),
            () -> assertEquals(fakeOrder1Response, result)
        );
    }

    @Test
    @DisplayName("Test if Order is being updated correctly")
    public void testUpdateOrder() {
        when(authentication.getName()).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(oRepository.findById(fakeOrder1Response.id())).thenReturn(Optional.ofNullable(fakeOrder1));
        when(oRepository.save(fakeOrder2)).thenReturn(fakeOrder2);
        when(oMapper.toDTO(fakeOrder2)).thenReturn(fakeOrder2Response);
        when(pRepository.findById(fakeOrderUpdate.product_id())).thenReturn(Optional.ofNullable(fakeProduct1));
        when(sRepository.getByProductId(fakeOrder2.getProduct().getId())).thenReturn(fakeSupply);
        when(pService.getById(fakeOrderUpdate.product_id())).thenReturn(fakeProductResponse);
        when(sellerService.getById(fakeOrderUpdate.seller_id())).thenReturn(fakeSellerResponse);
        when(cService.getById(fakeOrderUpdate.customer_id())).thenReturn(fakeCustomerResponse);

        result = oService.update(1, fakeOrderUpdate);

        assertAll(
            () -> assertNotNull(result),
            () -> assertInstanceOf(OrderResponseDTO.class, result),
            () -> assertEquals(fakeOrder2Response, result)
        );
    }

    @Test
    @DisplayName("Test if Order is being deleted correctly")
    public void testDeleteOrder() {
        oService.delete(fakeOrder1Response.id());

        verify(oRepository, atMostOnce()).deleteById(fakeOrder1Response.id());
    }

    @Test
    @DisplayName("Test if all Orders registered are being returned on method getAll()")
    public void testGetAllOrders() {
        when(oRepository.findAll()).thenReturn(List.of(fakeOrder1, fakeOrder2));
        when(oMapper.toDTOList(List.of(fakeOrder1, fakeOrder2))).thenReturn(List.of(fakeOrder1Response, fakeOrder2Response));

        assertEquals(2, oService.getAll().size());
        verify(oRepository, atMostOnce()).findAll();
    }

    @Test
    @DisplayName("Test if Order is being returned by id on method getById()")
    public void testGetOrderById() {
        when(oRepository.findById(1)).thenReturn(Optional.ofNullable(fakeOrder1));
        when(oMapper.toDTO(fakeOrder1)).thenReturn(fakeOrder1Response);

        result = oService.getById(1);

        assertAll(
            () -> verify(oRepository, atMostOnce()).findById(1),
            () -> assertNotNull(result),
            () -> assertInstanceOf(OrderResponseDTO.class, result),
            () -> assertEquals(fakeOrder1Response, result)
        );
    }
}