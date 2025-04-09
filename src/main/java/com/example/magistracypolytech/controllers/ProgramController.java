package com.example.magistracypolytech.controllers;

import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.models.EducationProgram;
import com.example.magistracypolytech.services.EducationProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Education Programs", description = "Управление образовательными программами")
public class ProgramController {

    private final EducationProgramService programService;

    public ProgramController(EducationProgramService programService) {
        this.programService = programService;
    }

    @GetMapping("/programs")
    @Operation(
            summary = "Получить список программ",
            description = "Возвращает все образовательные программы",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Успешное получение списка программ"

                    )
            }
    )
    public ResponseEntity<List<EducationProgram>> getPrograms(Model model) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(programService.getAllPrograms());
    }
}

