package com.example.magistracypolytech.controller;

import com.example.magistracypolytech.AbstractContainerBaseTest;
import com.example.magistracypolytech.dto.AuthRequest;
import com.example.magistracypolytech.models.Role;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest extends AbstractContainerBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @BeforeEach()
    void setupUser(TestInfo testInfo){
        if ("testRegisterSuccess".equals(testInfo.getTestMethod().orElseThrow().getName())) {
            return;
        }
        User user = new User();
        user.setUsername("testuser");
        user.setPassword(passwordEncoder.encode("password"));
        user.setEmail("test@gmail.com");
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    @Test
    @DisplayName("Test of success login")
    void testLoginSuccess() throws Exception {
        AuthRequest request = new AuthRequest("testuser", "password","test@gmail.com");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("Test of unsuccessful login")
    void testLoginUnSuccessful() throws Exception {
        AuthRequest request = new AuthRequest("testuser1", "password", null);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Invalid username/email or password"));
    }

    @Test
    @DisplayName("Test of success registration")
    void testRegisterSuccess() throws Exception {
        AuthRequest request = new AuthRequest("testuser", "password","stas@gmail.com");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        User savedUser = userRepository.findByUsername(request.getUsername()).orElseThrow();
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);
        assertThat(passwordEncoder.matches(request.getPassword(), savedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("Test of unsuccessful registration (user already exist)")
    void testRegisterUnsuccessful() throws Exception {
        AuthRequest request = new AuthRequest("testuser", "password","stas@gmail.com");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("User already exist"));
    }
    @Test
    @DisplayName("Test of unsuccessful registration (Bad request)")
    void testRegisterUnsuccessfulBadCredentials() throws Exception {
        AuthRequest request = new AuthRequest(null, "password","stas@gmail.com");

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Username, login and email are required"));

    }
}