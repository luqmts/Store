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
public class SupplyCSVControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplyRepository supplyRepository;
    @Autowired
    private SupplierRepository supplierRepository;
    @Autowired
    private ProductRepository pRepository;


    Supplier fakeSupplier1, fakeSupplier2;
    Product fakeProduct1, fakeProduct2;
    Supply fakeSupply1, fakeSupply2;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

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

        fakeSupply1 = new Supply(20, fakeProduct1);
        fakeSupply2 = new Supply(50, fakeProduct2);
        fakeSupply1.setCreated(now);
        fakeSupply1.setCreatedBy(user);
        fakeSupply1.setModified(now);
        fakeSupply1.setModifiedBy(user);
        fakeSupply2.setCreated(now);
        fakeSupply2.setCreatedBy(user);
        fakeSupply2.setModified(now);
        fakeSupply2.setModifiedBy(user);
        fakeSupply1 = supplyRepository.save(fakeSupply1);
        fakeSupply2 = supplyRepository.save(fakeSupply2);
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with only one Supply")
    public void testExportOneSupplyToCsvMethod() throws Exception {
        String csvHeader = "id,product,quantity,created,created_by,modified,modified_by\n";
        String row = csvHeader.concat(String.format(Locale.ROOT,
            "%d,%s,%d,%s,%s,%s,%s\n",
            fakeSupply1.getId(),
            escapeCsv(fakeSupply1.getProduct().getName()),
            fakeSupply1.getQuantity(),
            escapeCsv(fakeSupply1.getCreated().toString()),
            escapeCsv(fakeSupply1.getCreatedBy()),
            escapeCsv(fakeSupply1.getModified().toString()),
            escapeCsv(fakeSupply1.getModifiedBy())
        ));

        mockMvc.perform(
            get("/supply/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("product.id", "1")
        ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"supply.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with multiples Supply")
    public void testExportMultipleSupplyToCsvMethod() throws Exception {
        String csvHeader = "id,product,quantity,created,created_by,modified,modified_by\n";
        String row = csvHeader + String.format(Locale.ROOT,
            "%d,%s,%d,%s,%s,%s,%s\n",
            fakeSupply1.getId(),
            escapeCsv(fakeSupply1.getProduct().getName()),
            fakeSupply1.getQuantity(),
            escapeCsv(fakeSupply1.getCreated().toString()),
            escapeCsv(fakeSupply1.getCreatedBy()),
            escapeCsv(fakeSupply1.getModified().toString()),
            escapeCsv(fakeSupply1.getModifiedBy())
        ) + String.format(Locale.ROOT,
            "%d,%s,%d,%s,%s,%s,%s\n",
            fakeSupply2.getId(),
            escapeCsv(fakeSupply2.getProduct().getName()),
            fakeSupply2.getQuantity(),
            escapeCsv(fakeSupply2.getCreated().toString()),
            escapeCsv(fakeSupply2.getCreatedBy()),
            escapeCsv(fakeSupply2.getModified().toString()),
            escapeCsv(fakeSupply2.getModifiedBy())
        );

        mockMvc.perform(
            get("/supply/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"supply.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with no Supply")
    public void testExportNoSupplyToCsvMethod() throws Exception {
        String row = "id,product,quantity,created,created_by,modified,modified_by\n";

        mockMvc.perform(
            get("/supply/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("product.id", "1000")
        ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"supply.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }
}
