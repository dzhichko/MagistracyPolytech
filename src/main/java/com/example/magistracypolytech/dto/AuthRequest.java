package com.example.magistracypolytech.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    @Schema(
            description = "Имя пользователя",
            example = "admin"
    )
    private String username;

    @Schema(
            description = "Пароль пользователя",
            example = "qwerty"
    )
    private String password;

    @Schema(
            description = "Почта пользвователя",
            example = "qwerty@domain.com"
    )
    private String email;
}
