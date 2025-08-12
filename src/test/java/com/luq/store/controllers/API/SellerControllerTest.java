package com.luq.store.controllers.API;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.domain.Department;
import com.luq.store.dto.request.seller.SellerRegisterDTO;
import com.luq.store.dto.request.seller.SellerUpdateDTO;
import com.luq.store.dto.response.seller.SellerResponseDTO;
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

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class SellerControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SellerService sService;

    private SellerResponseDTO fakeSeller1Response, fakeSeller2Response;
    private SellerRegisterDTO fakeSellerRegister;
    private SellerUpdateDTO fakeSellerUpdate;

    @BeforeEach
    public void setUp() {
        String user = "Jimmy McGill";
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        fakeSeller1Response = new SellerResponseDTO(
            1, "Walter White",
            new Mail("WalterWhite@Cooking.com"), new Phone("11901010101"), Department.FOOD,
            user, now, user, now
        );
        fakeSeller2Response = new SellerResponseDTO(
            1, "Jesse Pinkman",
            new Mail("Jesse Pinkman@Cooking.com"), new Phone("11904040404"), Department.TECHNOLOGY,
            user, now, user, now
        );

        fakeSellerRegister = new SellerRegisterDTO(
            "Jesse Pinkman", new Mail("Jesse Pinkman@Cooking.com"), new Phone("11904040404"), Department.FOOD
        );

        fakeSellerUpdate = new SellerUpdateDTO(
            "Walter White", new Mail("WalterWhite@Cooking.com"), new Phone("11901010101"), Department.TECHNOLOGY
        );
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct seller's parameters are being returned on get all method")
    public void testGetAllMethod() throws Exception {
        when(sService.getAll()).thenReturn(List.of(fakeSeller1Response));
        String sellerJson = objectMapper.writeValueAsString(fakeSeller1Response);

        mockMvc.perform(get("/api/seller"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(content().json("[" + sellerJson + "]"));

        verify(sService, times(1)).getAll();
    }

    @Test
    @WithMockUser
    @DisplayName("Testing if correct seller's parameters are being returned on get by id")
    public void testGetByIdMethod() throws Exception {
        when(sService.getById(1)).thenReturn(fakeSeller1Response);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller1Response);

        mockMvc.perform(get("/api/seller/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().json(sellerJson));

        verify(sService, times(1)).getById(1);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if registering a new Seller with a admin's role user is going successfully")
    public void testRegisterMethodWithAdmin() throws Exception {
        when(sService.register(fakeSellerRegister)).thenReturn(fakeSeller1Response);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller1Response);

        mockMvc.perform(
            post("/api/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(sellerJson));


        verify(sService, times(1)).register(fakeSellerRegister);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if registering a new Seller with a user's role user is going unauthorized")
    public void testRegisterMethodWithUser() throws Exception {
        when(sService.register(fakeSellerRegister)).thenReturn(fakeSeller2Response);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller2Response);

        mockMvc.perform(
            post("/api/seller")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerJson)
        ).andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if updating a Seller with a admin's role user is going successfully")
    public void testUpdateMethodWithAdmin() throws Exception {
        when(sService.update(1, fakeSellerUpdate)).thenReturn(fakeSeller2Response);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller2Response);

        mockMvc.perform(
            put("/api/seller/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerJson)
            ).andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().json(sellerJson));

        verify(sService, times(1)).update(1, fakeSellerUpdate);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if updating a Seller with a user's role user is going unauthorized")
    public void testUpdateMethodWithUser() throws Exception {
        when(sService.update(1, fakeSellerUpdate)).thenReturn(fakeSeller2Response);
        String sellerJson = objectMapper.writeValueAsString(fakeSeller2Response);

        mockMvc.perform(
            put("/api/seller/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(sellerJson)
        ).andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Testing if deleting a Seller with a admin's role user is going successfully")
    public void testDeleteMethodWithAdmin() throws Exception {
        mockMvc.perform(delete("/api/seller/1"))
            .andExpect(status().isOk());

        verify(sService, times(1)).delete(1);
    }

    @Test
    @WithMockUser()
    @DisplayName("Testing if deleting a Seller with a user's role user is going unauthorized")
    public void testDeleteMethodWithUser() throws Exception {
        mockMvc.perform(put("/api/seller/1"))
            .andExpect(status().isForbidden());

        verifyNoInteractions(sService);
    }
}