package com.example.magistracypolytech.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EducationProgramDTO implements Serializable {

    private long id;

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