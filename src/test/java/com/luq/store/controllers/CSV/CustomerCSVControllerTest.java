package com.luq.store.controllers.CSV;

import com.luq.store.domain.Customer;
import com.luq.store.repositories.CustomerRepository;
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
public class CustomerCSVControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository cRepository;

    private Customer fakeCustomer1, fakeCustomer2;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeCustomer1 = new Customer("Test Customer 01");
        fakeCustomer2 = new Customer("Test Customer 02");
        fakeCustomer1.setCreated(now);
        fakeCustomer1.setCreatedBy(user);
        fakeCustomer1.setModified(now);
        fakeCustomer1.setModifiedBy(user);
        fakeCustomer2.setCreated(now);
        fakeCustomer2.setCreatedBy(user);
        fakeCustomer2.setModified(now);
        fakeCustomer2.setModifiedBy(user);

        fakeCustomer1 = cRepository.save(fakeCustomer1);
        fakeCustomer2 = cRepository.save(fakeCustomer2);
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with only one Customer")
    public void testExportOneCustomerToCsvMethod() throws Exception {
        String csvHeader = "id,name,created,created_by,modified,modified_by\n";
        String row = csvHeader.concat(String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%s\n",
            this.fakeCustomer1.getId(),
            escapeCsv(this.fakeCustomer1.getName()),
            escapeCsv(this.fakeCustomer1.getCreated().toString()),
            escapeCsv(this.fakeCustomer1.getCreatedBy()),
            escapeCsv(this.fakeCustomer1.getModified().toString()),
            escapeCsv(this.fakeCustomer1.getModifiedBy())
        ));

        mockMvc.perform(
            MockMvcRequestBuilders.get("/customer/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "Test Customer 01")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"customers.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with multiples Customers")
    public void testExportMultipleCustomersToCsvMethod() throws Exception {
        String csvHeader = "id,name,created,created_by,modified,modified_by\n";
        String row = csvHeader + String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%s\n",
            this.fakeCustomer1.getId(),
            escapeCsv(this.fakeCustomer1.getName()),
            escapeCsv(this.fakeCustomer1.getCreated().toString()),
            escapeCsv(this.fakeCustomer1.getCreatedBy()),
            escapeCsv(this.fakeCustomer1.getModified().toString()),
            escapeCsv(this.fakeCustomer1.getModifiedBy())
        ) + String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%s\n",
            this.fakeCustomer2.getId(),
            escapeCsv(this.fakeCustomer2.getName()),
            escapeCsv(this.fakeCustomer2.getCreated().toString()),
            escapeCsv(this.fakeCustomer2.getCreatedBy()),
            escapeCsv(this.fakeCustomer2.getModified().toString()),
            escapeCsv(this.fakeCustomer2.getModifiedBy())
        );

        mockMvc.perform(
            MockMvcRequestBuilders.get("/customer/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"customers.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with no Customers")
    public void testExportNoCustomersToCsvMethod() throws Exception {
        String row = "id,name,created,created_by,modified,modified_by\n";

        mockMvc.perform(
            MockMvcRequestBuilders.get("/customer/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "noitemreturn")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"customers.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }
}
