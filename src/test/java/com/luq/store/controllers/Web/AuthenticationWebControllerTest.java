package com.luq.store.controllers.Web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luq.store.config.TestSecurityConfig;
import com.luq.store.domain.User.AuthenticationDTO;
import com.luq.store.domain.User.User;
import com.luq.store.domain.User.UserRole;
import com.luq.store.repositories.UserRepository;
import com.luq.store.services.AuthorizationService;
import com.luq.store.services.TokenService;
import com.luq.store.valueobjects.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationWebController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public class AuthenticationWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository uRepository;

    @MockBean
    private AuthenticationManager authManager;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private AuthorizationService uService;

    private User fakeUser1, fakeUser2;

    @BeforeEach
    public void setUp() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        String user = "Jimmy McGill";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        fakeUser1 = new User(
            UUID.randomUUID().toString(), "Test User 01", new Mail("user1@mail.com"),
            encoder.encode("strongPassword"), UserRole.USER, encoder.encode("strongPassword"),
            user, now, user, now
        );

        fakeUser2 = new User(
            UUID.randomUUID().toString(), "Test User 02", new Mail("user2@mail.com"),
            encoder.encode("strongPasswordAdmin"), UserRole.ADMIN, encoder.encode("strongPasswordAdmin"),
            user, now, user, now
        );
    }

    @Test
    @DisplayName("Test if a not logged user can access login page")
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("Test if a not logged user must not access protected urls")
    public void testRedirectToLoginPage() throws Exception {
        mockMvc.perform(get("/order/list"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Test if a user can login with provided username and password")
    public void testLoginUser() throws Exception {
        AuthenticationDTO authDto = new AuthenticationDTO(new Mail("user1@mail.com"), "strongPassword");
        Authentication auth = new UsernamePasswordAuthenticationToken(fakeUser1, null, fakeUser1.getAuthorities());

        when(uRepository.findByLogin(authDto.login())).thenReturn(fakeUser1);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenService.generateToken((User) auth.getPrincipal())).thenReturn("fake-token");

        mockMvc.perform(post("/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(authDto)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/order/list"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if a default user must not access User list")
    public void testAccessToUserListAsDefaultUser() throws Exception{
        mockMvc.perform(get("/user/list"))
        .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can access User page with no filters")
    public void testAccessToUserListAsAdminUserWithNoFilter() throws Exception{
        when(uService.getAllSorted("name", "asc", null, null, null))
            .thenReturn(List.of(fakeUser1, fakeUser2));

        mockMvc.perform(
            get("/user/list")
                .param("sortBy", "name")
                .param("direction", "asc")
        ).andExpect(status().isOk())
        .andExpect(model().attribute("page", "user"))
        .andExpect(model().attribute("users", List.of(fakeUser1, fakeUser2)))
        .andExpect(model().attribute("roles", UserRole.values()))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("sortBy", "name"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can access User page with one filter")
    public void testAccessToUserListAsAdminUserWithOneFilter() throws Exception{
        when(uService.getAllSorted("name", "asc", "Test User 01", null, null))
            .thenReturn(List.of(fakeUser1, fakeUser2));

        mockMvc.perform(
            get("/user/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("name", "Test User 01")
        ).andExpect(status().isOk())
        .andExpect(model().attribute("page", "user"))
        .andExpect(model().attribute("users", List.of(fakeUser1, fakeUser2)))
        .andExpect(model().attribute("roles", UserRole.values()))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("name", "Test User 01"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can access User page with all filters")
    public void testAccessToUserListAsAdminUserWithAllFilters() throws Exception{
        when(uService.getAllSorted("name", "asc", "Test User 01", "user1@mail.com", "USER"))
            .thenReturn(List.of(fakeUser1, fakeUser2));

        mockMvc.perform(
            get("/user/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("name", "Test User 01")
                .param("login", "user1@mail.com")
                .param("selectedRole", "USER")
        ).andExpect(status().isOk())
        .andExpect(model().attribute("page", "user"))
        .andExpect(model().attribute("users", List.of(fakeUser1, fakeUser2)))
        .andExpect(model().attribute("roles", UserRole.values()))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("name", "Test User 01"))
        .andExpect(model().attribute("login", "user1@mail.com"))
        .andExpect(model().attribute("selectedRole", "USER"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can access User page with no results")
    public void testAccessToUserListAsAdminUserWithNoResult() throws Exception{
        when(uService.getAllSorted("name", "asc", "No user", null, null))
            .thenReturn(List.of());

        mockMvc.perform(
            get("/user/list")
                .param("sortBy", "name")
                .param("direction", "asc")
                .param("name", "No user")
        ).andExpect(status().isOk())
        .andExpect(model().attribute("page", "user"))
        .andExpect(model().attribute("users", List.of()))
        .andExpect(model().attribute("roles", UserRole.values()))
        .andExpect(model().attribute("direction", "asc"))
        .andExpect(model().attribute("sortBy", "name"))
        .andExpect(model().attribute("name", "No user"));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if a default user must not access User form")
    public void testAccessToUserFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/user/form"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can access User register form")
    public void testAccessToUserRegisterFormAsAdminUser() throws Exception{
        mockMvc.perform(get("/user/form"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("page", "user"))
        .andExpect(model().attribute("updatePassword", false))
        .andExpect(model().attribute("roles", UserRole.values()));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if a default user must not access User edit form")
    public void testAccessToUserEditFormAsDefaultUser() throws Exception{
        mockMvc.perform(get("/user/form/" + fakeUser1.getId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can access User edit form")
    public void testAccessToUserEditFormAsAdminUser() throws Exception{
        when(uService.getById(fakeUser1.getId())).thenReturn(fakeUser1);

        mockMvc.perform(get("/user/form/" + fakeUser1.getId()))
        .andExpect(status().isOk())
        .andExpect(model().attribute("page", "user"))
        .andExpect(model().attribute("user", fakeUser1))
        .andExpect(model().attribute("updatePassword", false))
        .andExpect(model().attribute("roles", UserRole.values()));
    }

    @Test
    @WithMockUser
    @DisplayName("Test if a default user must not access User edit form to update a password")
    public void testAccessToUserEditFormUpdatePasswordAsDefaultUser() throws Exception{
        mockMvc.perform(get("/user/form/" + fakeUser1.getId() + "/update-password"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can access User edit form to update a password")
    public void testAccessToUserEditFormUpdatePasswordAsAdminUser() throws Exception{
        when(uService.getById(fakeUser1.getId())).thenReturn(fakeUser1);

        mockMvc.perform(get("/user/form/" + fakeUser1.getId() + "/update-password"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("page", "user"))
        .andExpect(model().attribute("user", fakeUser1))
        .andExpect(model().attribute("updatePassword", true))
        .andExpect(model().attribute("roles", UserRole.values()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can register a new User")
    public void testRegisterNewUserAsAdminUser() throws Exception{
        mockMvc.perform(post("/user/form")
            .param("updatePassword", "false")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "UserName")
            .param("login", "user@mail.com")
            .param("password", "strongPassword")
            .param("confirmPassword", "strongPassword")
            .param("role", "USER")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user must not register a new User if passwords don't match")
    public void testRegisterNewUserAsAdminUserNoMatchPassword() throws Exception{
        mockMvc.perform(post("/user/form")
            .param("updatePassword", "false")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "UserName")
            .param("login", "user@mail.com")
            .param("password", "strongPassword123")
            .param("confirmPassword", "strongPassword")
            .param("role", "USER")
        ).andExpect(status().isOk())
        .andExpect(model().attribute("passwordError", "Passwords don't match"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user must not register a new User if mail is invalid")
    public void testRegisterNewUserAsAdminUserInvalidMail() throws Exception{
        mockMvc.perform(post("/user/form")
            .param("updatePassword", "false")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "UserName")
            .param("login", "user")
            .param("password", "strongPassword")
            .param("confirmPassword", "strongPassword")
            .param("role", "USER")
        ).andExpect(status().isOk())
        .andExpect(model().attribute("mailError", "Invalid mail"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can edit a User")
    public void testEditUserAsAdminUser() throws Exception{
        when(uService.getById(fakeUser1.getId())).thenReturn(fakeUser1);

        mockMvc.perform(post("/user/form")
            .param("id", fakeUser1.getId())
            .param("updatePassword", "false")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "UserName")
            .param("login", "user@mail.com")
            .param("role", "USER")
            .param("password", "")
            .param("confirmPassword", "")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user must not edit a new User if mail is invalid")
    public void testEditUserAsAdminUserInvalidMail() throws Exception{
        when(uService.getById(fakeUser1.getId())).thenReturn(fakeUser1);

        mockMvc.perform(post("/user/form")
            .param("id", fakeUser1.getId())
            .param("updatePassword", "false")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "UserName")
            .param("login", "user")
            .param("role", "USER")
            .param("password", "")
            .param("confirmPassword", "")
        ).andExpect(status().isOk())
        .andExpect(model().attribute("mailError", "Invalid mail"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user can update password")
    public void testUpdatePasswordAsAdminUser() throws Exception{
        when(uService.getById(fakeUser1.getId())).thenReturn(fakeUser1);

        mockMvc.perform(post("/user/form")
            .param("id", fakeUser1.getId())
            .param("updatePassword", "true")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", fakeUser1.getName())
            .param("login", fakeUser1.getLogin().getValue())
            .param("role", fakeUser1.getRole().toString())
            .param("password", "strongPassword")
            .param("confirmPassword", "strongPassword")
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/user/list"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Test if a admin user must not update a User's password if passwords don't match")
    public void testUpdatePasswordAsAdminUserNoMatchPassword() throws Exception{
        when(uService.getById(fakeUser1.getId())).thenReturn(fakeUser1);

        mockMvc.perform(post("/user/form")
            .param("id", fakeUser1.getId())
            .param("updatePassword", "true")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .param("name", "")
            .param("login", "")
            .param("role", "")
            .param("password", "strongPassword123")
            .param("confirmPassword", "strongPassword")
        ).andExpect(status().isOk())
        .andExpect(model().attribute("passwordError", "Passwords don't match"));
    }

    @Test
    @WithMockUser
    @DisplayName("Default user must not delete Customers")
    public void testDeleteCustomerAsUser() throws Exception{
        mockMvc.perform(get("/user/delete/" + fakeUser1.getId()))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "Admin", roles = {"ADMIN"})
    @DisplayName("Admin user can delete Customers")
    public void testDeleteCustomerAsAdmin() throws Exception{
        mockMvc.perform(get("/user/delete/" + fakeUser1.getId()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/user/list"));

        verify(uService, times(1)).delete(eq(fakeUser1.getId()));
    }
}