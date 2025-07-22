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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.unbescape.csv.CsvEscape.escapeCsv;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class SupplierCSVControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierRepository sRepository;

    Supplier fakeSupplier1, fakeSupplier2;
    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeSupplier1 = new Supplier(
            "Microsoft Brasil LTDA.", new Cnpj("43.447.044/0004-10"),
            new Mail("microsoft@mail.com"), new Phone("11000001111")
        );
        fakeSupplier2 = new Supplier(
            "Sony Brasil LTDA.", new Cnpj("04.542.534/0001-09"),
            new Mail("sony@mail.com"), new Phone("11222225555")
        );

        fakeSupplier1.setCreated(now);
        fakeSupplier1.setCreatedBy(user);
        fakeSupplier1.setModified(now);
        fakeSupplier1.setModifiedBy(user);
        fakeSupplier2.setCreated(now);
        fakeSupplier2.setCreatedBy(user);
        fakeSupplier2.setModified(now);
        fakeSupplier2.setModifiedBy(user);
        fakeSupplier1 = sRepository.save(fakeSupplier1);
        fakeSupplier2 = sRepository.save(fakeSupplier2);
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with only one Supplier")
    public void testExportOneSupplierToCsvMethod() throws Exception {
        String csvHeader = "id,name,cnpj,mail,phone,created,created_by,modified,modified_by\n";
        String row = csvHeader.concat(String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
            fakeSupplier1.getId(),
            escapeCsv(fakeSupplier1.getName()),
            escapeCsv(fakeSupplier1.getCnpj().toString()),
            escapeCsv(fakeSupplier1.getMail().toString()),
            escapeCsv(fakeSupplier1.getPhone().toString()),
            escapeCsv(fakeSupplier1.getCreated().toString()),
            escapeCsv(fakeSupplier1.getCreatedBy()),
            escapeCsv(fakeSupplier1.getModified().toString()),
            escapeCsv(fakeSupplier1.getModifiedBy())
        ));

        mockMvc.perform(
            MockMvcRequestBuilders.get("/supplier/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "Microsoft Brasil LTDA.")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"suppliers.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with multiples Suppliers")
    public void testExportMultipleSuppliersToCsvMethod() throws Exception {
        String csvHeader = "id,name,cnpj,mail,phone,created,created_by,modified,modified_by\n";
        String row = csvHeader + String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
            fakeSupplier1.getId(),
            escapeCsv(fakeSupplier1.getName()),
            escapeCsv(fakeSupplier1.getCnpj().toString()),
            escapeCsv(fakeSupplier1.getMail().toString()),
            escapeCsv(fakeSupplier1.getPhone().toString()),
            escapeCsv(fakeSupplier1.getCreated().toString()),
            escapeCsv(fakeSupplier1.getCreatedBy()),
            escapeCsv(fakeSupplier1.getModified().toString()),
            escapeCsv(fakeSupplier1.getModifiedBy())
        ) + String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
            fakeSupplier2.getId(),
            escapeCsv(fakeSupplier2.getName()),
            escapeCsv(fakeSupplier2.getCnpj().toString()),
            escapeCsv(fakeSupplier2.getMail().toString()),
            escapeCsv(fakeSupplier2.getPhone().toString()),
            escapeCsv(fakeSupplier2.getCreated().toString()),
            escapeCsv(fakeSupplier2.getCreatedBy()),
            escapeCsv(fakeSupplier2.getModified().toString()),
            escapeCsv(fakeSupplier2.getModifiedBy())
        );

        mockMvc.perform(
            MockMvcRequestBuilders.get("/supplier/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"suppliers.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with no Suppliers")
    public void testExportNoSuppliersToCsvMethod() throws Exception {
        String row = "id,name,cnpj,mail,phone,created,created_by,modified,modified_by\n";

        mockMvc.perform(
            MockMvcRequestBuilders.get("/supplier/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "noresultitem")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"suppliers.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }
}
