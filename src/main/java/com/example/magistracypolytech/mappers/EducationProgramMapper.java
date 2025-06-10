package com.example.magistracypolytech.mappers;

import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.models.EducationProgram;
import org.springframework.stereotype.Component;

@Component
public class EducationProgramMapper {

    public EducationProgramDTO toDTO(EducationProgram educationProgram) {
        return EducationProgramDTO.builder()
                .id(educationProgram.getId())
                .code(educationProgram.getCode())
                .name(educationProgram.getName())
                .contractPlace(educationProgram.getContractPlace())
                .budgetPlace(educationProgram.getBudgetPlace())
                .institution(educationProgram.getInstitution())
                .instituteShortName(educationProgram.getInstituteShortName())
                .build();
    }
}
