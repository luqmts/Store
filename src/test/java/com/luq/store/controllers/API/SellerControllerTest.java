package com.luq.store.controllers.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.domain.Department;
import com.luq.store.domain.Seller;
import com.luq.store.services.SellerService;
import com.luq.store.valueobjects.Mail;
import com.luq.store.valueobjects.Phone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class SellerControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SellerService sService;

    private Seller fakeSeller1, fakeSeller2;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeSeller1 = new Seller(
            1, "Walter White",
            new Mail("WalterWhite@Cooking.com"), new Phone("11901010101"), Department.FOOD,
            user, now, user, now
        );
        fakeSeller2 = new Seller(
            2, "Jesse Pinkman",
            new Mail("Jesse Pinkman@Cooking.com"), new Phone("11904040404"), Department.FOOD,
            user, now, user, now
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct seller's parameters are being returned on get all method")
    public void testGetAllMethod() throws Exception {
        when(sService.getAll()).thenReturn(List.of(fakeSeller1));
        String sellerJson = objectMapper.writeValueAsString(fakeSeller1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/seller"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(content().json("[" + sellerJson + "]"));

        verify(sService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct seller's parameters are being returned on get by id")
    public void testGetByIdMethod() throws Exception {
        when(sService.getById(1)).thenReturn(fakeSeller1);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/seller/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(sellerJson));

        verify(sService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Seller with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(sService.register(fakeSeller1)).thenReturn(fakeSeller1);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller1);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerJson)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(sellerJson));


        verify(sService, times(1)).register(fakeSeller1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Seller with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(sService.register(fakeSeller1)).thenReturn(fakeSeller1);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller1);

        mockMvc.perform(
            MockMvcRequestBuilders.post("/api/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerJson)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Seller with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(sService.update(1, fakeSeller2)).thenReturn(fakeSeller2);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller2);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/seller/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerJson)
            ).andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(sellerJson));

        verify(sService, times(1)).update(1, fakeSeller2);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Seller with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(sService.register(fakeSeller2)).thenReturn(fakeSeller2);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller2);

        mockMvc.perform(
            MockMvcRequestBuilders.put("/api/seller/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerJson)
        ).andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if deleting a Seller with a admin's role user is going successfully")
    public void testDeleteMethodWithAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/seller/1"))
            .andExpect(MockMvcResultMatchers.status().isOk());

        verify(sService, times(1)).delete(1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if deleting a Seller with a user's role user is going unauthorized")
    public void testDeleteMethodWithUser() throws Exception {
        mockMvc.perform( MockMvcRequestBuilders.put("/api/seller/1"))
            .andExpect(MockMvcResultMatchers.status().isForbidden());

        verifyNoInteractions(sService);
    }
}