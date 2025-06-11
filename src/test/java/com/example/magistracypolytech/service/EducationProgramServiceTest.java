package com.example.magistracypolytech.service;

import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.mappers.EducationProgramMapper;
import com.example.magistracypolytech.models.EducationProgram;
import com.example.magistracypolytech.repositories.EducationProgramRepository;
import com.example.magistracypolytech.services.EducationProgramService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EducationProgramServiceTest {

    @Mock
    private EducationProgramRepository programRepository;

    @Spy
    private EducationProgramMapper programMapper = Mappers.getMapper(EducationProgramMapper.class);

    @InjectMocks
    private EducationProgramService programService;

    @Test
    void getAllPrograms_shouldReturnListOfDTOs() {
        // Arrange
        EducationProgram program1 = createProgram(1L, "P-001", "Program One");
        EducationProgram program2 = createProgram(2L, "P-002", "Program Two");

        when(programRepository.findAll()).thenReturn(Arrays.asList(program1, program2));

        // Act
        List<EducationProgramDTO> result = programService.getAllPrograms();

        // Assert
        assertEquals(2, result.size());
        assertEquals("P-001", result.get(0).getCode());
        assertEquals("Program One", result.get(0).getName());
        assertEquals("P-002", result.get(1).getCode());
        assertEquals("Program Two", result.get(1).getName());
    }

    @Test
    void findByCode_shouldReturnProgramWhenExists() {
        // Arrange
        EducationProgram program = createProgram(1L, "CODE-123", "Test Program");
        when(programRepository.findByCode("CODE-123")).thenReturn(Optional.of(program));

        // Act
        EducationProgram result = programService.findByCode("CODE-123");

        // Assert
        assertNotNull(result);
        assertEquals("CODE-123", result.getCode());
    }

    @Test
    void findByCode_shouldThrowExceptionWhenNotFound() {
        // Arrange
        when(programRepository.findByCode("INVALID")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            programService.findByCode("INVALID");
        });

        assertEquals("Education program: INVALID not found", exception.getMessage());
    }

    @Test
    void findById_shouldReturnProgramWhenExists() {
        // Arrange
        EducationProgram program = createProgram(1L, "ID-001", "Program by ID");
        when(programRepository.findById(1L)).thenReturn(Optional.of(program));

        // Act
        EducationProgram result = programService.findById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void findById_shouldThrowExceptionWhenNotFound() {
        // Arrange
        when(programRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            programService.findById(999L);
        });

        assertEquals("Education program not found", exception.getMessage());
    }

    @Test
    void getProgramFile_shouldReturnFileDataWhenExists() {
        // Arrange
        byte[] fileData = "PDF content".getBytes();
        EducationProgram program = createProgram(1L, "FILE-001", "File Program");
        program.setFileData(fileData);

        when(programRepository.findByCode("FILE-001")).thenReturn(Optional.of(program));

        // Act
        byte[] result = programService.getProgramFile("FILE-001");

        // Assert
        assertArrayEquals(fileData, result);
    }

    @Test
    void getProgramFile_shouldThrowExceptionWhenNotFound() {
        // Arrange
        when(programRepository.findByCode("MISSING")).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            programService.getProgramFile("MISSING");
        });

        assertEquals("Education program: MISSING not found", exception.getMessage());
    }

    private EducationProgram createProgram(Long id, String code, String name) {
        EducationProgram program = new EducationProgram();
        program.setId(id);
        program.setCode(code);
        program.setName(name);
        program.setInstitution("Test Institution");
        program.setBudgetPlace("10");
        program.setContractPlace("20");
        program.setInstituteShortName("TI");
        return program;
    }
}
