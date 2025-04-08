package com.example.magistracypolytech.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EducationProgramDTO {
    @Schema(
            description = "Код направления подготовки",
            example = "01.04.02"
    )
    private String code;
    @Schema(
            description = "Название образовательной программы",
            example = "Прикладная математика и информатика"
    )
    private String name;


    @Override
    public String toString() {
        return code + " - " + name;
    }
}