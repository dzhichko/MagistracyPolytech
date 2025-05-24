package com.example.magistracypolytech.services;


import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.models.EducationProgram;
import com.example.magistracypolytech.repositories.EducationProgramRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EducationProgramService {

    private final EducationProgramRepository programRepository;

    public List<EducationProgramDTO> getAllPrograms(){
        return programRepository.findAll().
                stream().map(program -> EducationProgramDTO.builder()
                        .id(program.getId())
                        .name(program.getName())
                        .code(program.getCode()).build()).collect(Collectors.toList());
    }

    public EducationProgram findById(long id){
        return programRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Education program not found"));
    }

    public byte[] getProgramFile(String code){
        EducationProgram program = programRepository.findByCode(code).
                orElseThrow(() -> new EntityNotFoundException("Education program: " + code + " not found"));

        return program.getFileData();
    }

}

