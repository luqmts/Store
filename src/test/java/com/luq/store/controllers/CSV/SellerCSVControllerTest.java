package com.luq.store.controllers.CSV;

import com.luq.store.domain.*;
import com.luq.store.repositories.*;
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
public class SellerCSVControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SellerRepository sRepository;

    Seller fakeSeller1, fakeSeller2;
    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeSeller1 = new Seller(
            "Walter White", new Mail("WalterWhite@Cooking.com"),
            new Phone("11901010101"), Department.FOOD
        );
        fakeSeller2 = new Seller(
            "Jesse Pinkman", new Mail("Jesse Pinkman@Cooking.com"),
            new Phone("11904040404"), Department.FOOD
        );

        fakeSeller1.setCreated(now);
        fakeSeller1.setCreatedBy(user);
        fakeSeller1.setModified(now);
        fakeSeller1.setModifiedBy(user);
        fakeSeller2.setCreated(now);
        fakeSeller2.setCreatedBy(user);
        fakeSeller2.setModified(now);
        fakeSeller2.setModifiedBy(user);
        fakeSeller1 = sRepository.save(fakeSeller1);
        fakeSeller2 = sRepository.save(fakeSeller2);
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with only one Seller")
    public void testExportOneSellerToCsvMethod() throws Exception {
        String csvHeader = "id,name,mail,phone,department,created,created_by,modified,modified_by\n";
        String row = csvHeader.concat(String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
            fakeSeller1.getId(),
            escapeCsv(fakeSeller1.getName()),
            escapeCsv(fakeSeller1.getMail().toString()),
            escapeCsv(fakeSeller1.getPhone().toString()),
            escapeCsv(fakeSeller1.getDepartment().toString()),
            escapeCsv(fakeSeller1.getCreated().toString()),
            escapeCsv(fakeSeller1.getCreatedBy()),
            escapeCsv(fakeSeller1.getModified().toString()),
            escapeCsv(fakeSeller1.getModifiedBy())
        ));

        mockMvc.perform(
            get("/seller/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "Walter White")
        ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sellers.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with multiples Sellers")
    public void testExportMultipleSellersToCsvMethod() throws Exception {
        String csvHeader = "id,name,mail,phone,department,created,created_by,modified,modified_by\n";
        String row = csvHeader + String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
            fakeSeller1.getId(),
            escapeCsv(fakeSeller1.getName()),
            escapeCsv(fakeSeller1.getMail().toString()),
            escapeCsv(fakeSeller1.getPhone().toString()),
            escapeCsv(fakeSeller1.getDepartment().toString()),
            escapeCsv(fakeSeller1.getCreated().toString()),
            escapeCsv(fakeSeller1.getCreatedBy()),
            escapeCsv(fakeSeller1.getModified().toString()),
            escapeCsv(fakeSeller1.getModifiedBy())
        ) + String.format(Locale.ROOT,
            "%d,%s,%s,%s,%s,%s,%s,%s,%s\n",
            fakeSeller2.getId(),
            escapeCsv(fakeSeller2.getName()),
            escapeCsv(fakeSeller2.getMail().toString()),
            escapeCsv(fakeSeller2.getPhone().toString()),
            escapeCsv(fakeSeller2.getDepartment().toString()),
            escapeCsv(fakeSeller2.getCreated().toString()),
            escapeCsv(fakeSeller2.getCreatedBy()),
            escapeCsv(fakeSeller2.getModified().toString()),
            escapeCsv(fakeSeller2.getModifiedBy())
        );

        mockMvc.perform(
            get("/seller/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sellers.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if csv is being exported correctly with no Sellers")
    public void testExportNoSellersToCsvMethod() throws Exception {
        String row = "id,name,mail,phone,department,created,created_by,modified,modified_by\n";

        mockMvc.perform(
            get("/seller/csv")
                .param("sortBy", "id")
                .param("direction", "asc")
                .param("name", "Saul Goodman")
        ).andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"sellers.csv\""))
        .andExpect(content().contentType("text/csv;charset=UTF-8"))
        .andExpect(content().string(row));
    }
}
