package com.example.magistracypolytech.controllers;

import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.security.CustomUserDetails;
import com.example.magistracypolytech.services.EducationProgramService;
import com.example.magistracypolytech.services.UserFavouriteProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/programs")
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

    @GetMapping(value = "/download/{code}", produces = MediaType.APPLICATION_PDF_VALUE)
    public void getProgramFile(@PathVariable String code, HttpServletResponse response) throws IOException {
        byte[] pdfBytes = programService.getProgramFile(code);

        if (pdfBytes == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", "inline; filename=\"program_" + code + ".pdf\"");
        response.setContentLength(pdfBytes.length);

        ServletOutputStream os = response.getOutputStream();
        os.write(pdfBytes);
        os.flush();
    }

    @SecurityRequirement(name="bearerAuth")
    @PostMapping("/favourite/{code}")
    @PreAuthorize("isAuthenticated()")
    public void setFavouriteProgram(@PathVariable String code, @AuthenticationPrincipal CustomUserDetails userDetails){
        long userId = userDetails.getId();
        userFavouriteProgramService.setFavouriteProgramToUser(code, userId);
    }

}

