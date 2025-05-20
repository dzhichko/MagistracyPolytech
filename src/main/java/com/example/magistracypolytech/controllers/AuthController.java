package com.example.magistracypolytech.controllers;

import com.example.magistracypolytech.dto.AuthRequest;
import com.example.magistracypolytech.dto.AuthResponse;
import com.example.magistracypolytech.exceptions.UserAlreadyExistException;
import com.example.magistracypolytech.models.Role;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.security.JwtTokenUtil;
import com.example.magistracypolytech.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Authentication", description = "API для аутентификации и регистрации пользователей")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          PasswordEncoder passwordEncoder,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Возвращает JWT токен в cookie и теле ответа",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для входа",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequest.class),
                            examples = @ExampleObject(
                                    name = "Пример запроса",
                                    value = "{\"username\": \"user\", \"password\": \"password\"}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешная аутентификация",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            name = "Пример ответа",
                                            value = "{\"jwt\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неверные учетные данные"
                    )
            }
    )
    public AuthResponse login(@RequestBody AuthRequest authRequest) throws BadRequestException {

        if (authRequest.getUsername() == null || authRequest.getPassword() == null || authRequest.getEmail() == null) {
            throw new BadRequestException("Username, login and email are required");
        }

        if (!userService.match(authRequest.getUsername(), authRequest.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthResponse(jwtTokenUtil.generateToken(authentication.getName()));
    }

    @PostMapping("/register")
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Создает нового пользователя с ролью USER",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для регистрации",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь успешно зарегистрирован",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{\"message\": \"User registered successfully\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Пользователь уже существует"
                    )
            }
    )
    public AuthResponse register(@RequestBody AuthRequest authRequest) throws BadRequestException {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        if (authRequest.getUsername() == null || authRequest.getPassword() == null || authRequest.getEmail() == null) {
            throw new BadRequestException("Username, login and email are required");
        }

        if (userService.isPresentByUsername(username)) {
            throw new UserAlreadyExistException();
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);

        userService.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new AuthResponse(jwtTokenUtil.generateToken(authentication.getName()));
    }
}