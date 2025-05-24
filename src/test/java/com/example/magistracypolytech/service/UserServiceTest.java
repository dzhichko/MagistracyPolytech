package com.example.magistracypolytech.service;

import com.example.magistracypolytech.AbstractContainerBaseTest;
import com.example.magistracypolytech.models.Role;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.repositories.UserRepository;
import com.example.magistracypolytech.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
public class UserServiceTest extends AbstractContainerBaseTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        userService.save(user);

        User actUser = userService.findByUsername("admin");
        assertTrue(passwordEncoder.matches("qwerty", actUser.getPassword()));
        assertEquals(user.getUsername(), actUser.getUsername());
        assertEquals(actUser.getRole(), Role.USER);
        assertEquals("qwerty@gmail.com", actUser.getEmail());
    }

}
