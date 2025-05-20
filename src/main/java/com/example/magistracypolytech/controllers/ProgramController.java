package com.example.magistracypolytech.controllers;

import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.security.CustomUserDetails;
import com.example.magistracypolytech.services.EducationProgramService;
import com.example.magistracypolytech.services.UserFavouriteProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/programs")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Education Programs", description = "Управление образовательными программами")
@RequiredArgsConstructor
public class ProgramController {

    private final EducationProgramService programService;
    private final UserFavouriteProgramService userFavouriteProgramService;

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
    @GetMapping()
    @Cacheable(value = "programs")
    public List<EducationProgramDTO> getPrograms(){
        return programService.getAllPrograms();
    }

    @PostMapping("/favourite/{idProgram}")
    @PreAuthorize("isAuthenticated()")
    public void setFavouriteProgram(@PathVariable long idProgram, @AuthenticationPrincipal CustomUserDetails userDetails){
        long userId = userDetails.getId();
        userFavouriteProgramService.setFavouriteProgramToUser(idProgram, userId);
    }

}

