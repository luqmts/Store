package com.luq.store.controllers.CSV;

import com.luq.store.domain.*;
import com.luq.store.repositories.*;
import com.luq.store.valueobjects.Cnpj;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.unbescape.csv.CsvEscape.escapeCsv;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OrderCSVControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository oRepository;
    @Autowired
    private CustomerRepository cRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductRepository pRepository;
    @Autowired
    private SellerRepository sellerRepository;

    Customer fakeCustomer1, fakeCustomer2;
    Supplier fakeSupplier1, fakeSupplier2;
    Product fakeProduct1, fakeProduct2;
    Seller fakeSeller1, fakeSeller2;
    Order fakeOrder1, fakeOrder2;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        fakeCustomer1 = cRepository.save(new Customer("Test Customer 01"));
        fakeCustomer2 = cRepository.save(new Customer("Test Customer 02"));

        fakeSupplier1 = supplierRepository.save(new Supplier(
            "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111")
        ));
        fakeSupplier2 = supplierRepository.save(new Supplier(
            "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
            new Mail("sony@mail.com"), new Phone("11222225555")
        ));

        fakeProduct1 = pRepository.save(new Product(
            "XOneCont", "Xbox One Controller", "Controller for Xbox One Console",
            fakeSupplier1, 200.00F
        ));
        fakeProduct2 = pRepository.save(new Product(
            "PS5Cont", "Playstation 5 Controller", "Controller for Playstation 5 Console",
            fakeSupplier2, 250.00F
        ));

        fakeSeller1 = sellerRepository.save(new Seller(
            "Walter White", new Mail("WalterWhite@Cooking.com"),
            new Phone("11901010101"), Department.FOOD
        ));
        fakeSeller2 = sellerRepository.save(new Seller(
            "Jesse Pinkman", new Mail("Jesse Pinkman@Cooking.com"),
            new Phone("11904040404"), Department.FOOD
        ));

        fakeOrder1 = new Order(400.00F, 5, LocalDate.now(), fakeProduct1, fakeSeller1, fakeCustomer1);
        fakeOrder2 = new Order(800.00F, 8, LocalDate.now(), fakeProduct2, fakeSeller2, fakeCustomer2);
        fakeOrder1.setCreated(now);
        fakeOrder1.setCreatedBy(user);
        fakeOrder1.setModified(now);
        fakeOrder1.setModifiedBy(user);
        fakeOrder2.setCreated(now);
        fakeOrder2.setCreatedBy(user);
        fakeOrder2.setModified(now);
        fakeOrder2.setModifiedBy(user);
        fakeOrder1 = oRepository.save(fakeOrder1);
        fakeOrder2 = oRepository.save(fakeOrder2);
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with only one Order")
    public void testExportOneOrderToCsvMethod() throws Exception {
        String csvHeader = "id,product,seller,customer,orderDate,quantity,totalPrice,created,created_by,modified,modified_by\n";
        String row = csvHeader.concat(String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%d,%.2f,%s,%s,%s,%s\n",
            this.fakeOrder1.getId(),
            escapeCsv(this.fakeOrder1.getProduct().getName()),
            escapeCsv(this.fakeOrder1.getSeller().getName()),
            escapeCsv(this.fakeOrder1.getCustomer().getName()),
            escapeCsv(this.fakeOrder1.getOrderDate().toString()),
            this.fakeOrder1.getQuantity(),
            this.fakeOrder1.getTotalPrice(),
            escapeCsv(this.fakeOrder1.getCreated().toString()),
            escapeCsv(this.fakeOrder1.getCreatedBy()),
            escapeCsv(this.fakeOrder1.getModified().toString()),
            escapeCsv(this.fakeOrder1.getModifiedBy())
        ));

        mockMvc.perform(
            get("/order/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("product.id", "1")
        ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"orders.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with multiples Orders")
    public void testExportMultipleOrdersToCsvMethod() throws Exception {
        String csvHeader = "id,product,seller,customer,orderDate,quantity,totalPrice,created,created_by,modified,modified_by\n";
        String row = csvHeader + String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%d,%.2f,%s,%s,%s,%s\n",
            this.fakeOrder1.getId(),
            escapeCsv(this.fakeOrder1.getProduct().getName()),
            escapeCsv(this.fakeOrder1.getSeller().getName()),
            escapeCsv(this.fakeOrder1.getCustomer().getName()),
            escapeCsv(this.fakeOrder1.getOrderDate().toString()),
            this.fakeOrder1.getQuantity(),
            this.fakeOrder1.getTotalPrice(),
            escapeCsv(this.fakeOrder1.getCreated().toString()),
            escapeCsv(this.fakeOrder1.getCreatedBy()),
            escapeCsv(this.fakeOrder1.getModified().toString()),
            escapeCsv(this.fakeOrder1.getModifiedBy())
        ) + String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%d,%.2f,%s,%s,%s,%s\n",
            this.fakeOrder2.getId(),
            escapeCsv(this.fakeOrder2.getProduct().getName()),
            escapeCsv(this.fakeOrder2.getSeller().getName()),
            escapeCsv(this.fakeOrder2.getCustomer().getName()),
            escapeCsv(this.fakeOrder2.getOrderDate().toString()),
            this.fakeOrder2.getQuantity(),
            this.fakeOrder2.getTotalPrice(),
            escapeCsv(this.fakeOrder2.getCreated().toString()),
            escapeCsv(this.fakeOrder2.getCreatedBy()),
            escapeCsv(this.fakeOrder2.getModified().toString()),
            escapeCsv(this.fakeOrder2.getModifiedBy())
        );

        mockMvc.perform(
            get("/order/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"orders.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with no Orders")
    public void testExportNoOrdersToCsvMethod() throws Exception {
        String row = "id,product,seller,customer,orderDate,quantity,totalPrice,created,created_by,modified,modified_by\n";

        mockMvc.perform(
            get("/order/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("product.id", "1000")
        ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"orders.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }
}
