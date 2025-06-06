package com.example.magistracypolytech.controller;

import com.example.magistracypolytech.AbstractContainerBaseTest;
import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.models.Role;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.repositories.UserRepository;
import com.example.magistracypolytech.security.CustomUserDetails;
import com.example.magistracypolytech.services.EducationProgramService;
import com.example.magistracypolytech.services.UserFavouriteProgramService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProgramControllerTest extends AbstractContainerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EducationProgramService programService;

    @Autowired
    private UserFavouriteProgramService userFavouriteProgramService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    void getPrograms_shouldReturnProgramList() throws Exception {
        EducationProgramDTO program = new EducationProgramDTO();
        when(programService.getAllPrograms()).thenReturn(List.of(program));

        mockMvc.perform(get("/programs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(programService, times(1)).getAllPrograms();
    }

    @Test
    void getProgramFile_whenExists_shouldReturnPdf() throws Exception {
        byte[] pdfContent = "PDF content".getBytes();
        when(programService.getProgramFile("01.03.02")).thenReturn(pdfContent);

        mockMvc.perform(get("/programs/download/01.03.02"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "inline; filename=\"program_01.03.02.pdf\""))
                .andExpect(content().bytes(pdfContent));
    }

    @Test
    void getProgramFile_whenNotExists_shouldReturn404() throws Exception {
        when(programService.getProgramFile("invalid")).thenReturn(null);

        mockMvc.perform(get("/programs/download/invalid"))
                .andExpect(status().isNotFound());
    }

    @Test
    void setFavouriteProgram_shouldCallService() throws Exception {
        mockMvc.perform(post("/programs/favourite/01.03.02"))
                .andExpect(status().isOk());

        verify(userFavouriteProgramService).setFavouriteProgramToUser("01.03.02", 1L);
    }

    @Test
    void deleteFavouriteProgram_shouldCallService() throws Exception {
        mockMvc.perform(delete("/programs/favourite/01.03.02"))
                .andExpect(status().isOk());

        verify(userFavouriteProgramService).deleteFavouriteProgramByUserIdAndCode("01.03.02", 1L);
    }

    @Test
    void getFavouritePrograms_shouldReturnList() throws Exception {
        EducationProgramDTO program = new EducationProgramDTO();
        when(userFavouriteProgramService.getFavouriteProgramsByUser(1L)).thenReturn(List.of(program));

        mockMvc.perform(get("/programs/favourite/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        public EducationProgramService educationProgramService() {
            return mock(EducationProgramService.class);
        }

        @Bean
        @Primary
        public UserFavouriteProgramService userFavouriteProgramService() {
            return mock(UserFavouriteProgramService.class);
        }
    }
}