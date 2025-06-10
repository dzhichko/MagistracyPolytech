package com.example.magistracypolytech.services;


import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.mappers.EducationProgramMapper;
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

    private final EducationProgramMapper programMapper;

    public List<EducationProgramDTO> getAllPrograms(){
        return programRepository.findAll().
                stream().map(programMapper::toDTO).collect(Collectors.toList());
    }

    public EducationProgram findByCode(String code){
        return programRepository.findByCode(code).
                orElseThrow(() -> new EntityNotFoundException("Education program: " + code + " not found"));
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

