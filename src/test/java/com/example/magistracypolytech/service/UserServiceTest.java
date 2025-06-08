package com.example.magistracypolytech.service;

import com.example.magistracypolytech.AbstractContainerBaseTest;
import com.example.magistracypolytech.models.Role;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.repositories.UserRepository;
import com.example.magistracypolytech.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends AbstractContainerBaseTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceTest() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @AfterEach
    public void deleteAll(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Success registration")
    public void testUser(){
        User user = new User();
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("qwerty"));
        user.setEmail("qwerty@gmail.com");
        user.setRole(Role.USER);


        Mockito.when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User actUser = userService.findByUsername("admin");
        assertTrue(passwordEncoder.matches("qwerty", actUser.getPassword()));
        assertEquals(user.getUsername(), actUser.getUsername());
        assertEquals(Role.USER, actUser.getRole());
        assertEquals("qwerty@gmail.com", actUser.getEmail());
    }

}
