package com.example.magistracypolytech.services;

import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean match(String username, String password) {
        User user = this.findByUsername(username);
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    }
    public void save(User user){
        userRepository.save(user);
    }

    public boolean isPresentByUsername(String username){
        return userRepository.findByUsername(username).isPresent();
    }

}
