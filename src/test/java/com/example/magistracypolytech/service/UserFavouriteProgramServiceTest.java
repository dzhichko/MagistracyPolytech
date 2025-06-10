package com.example.magistracypolytech.service;

import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.dto.EmailDTO;
import com.example.magistracypolytech.mappers.EducationProgramMapper;
import com.example.magistracypolytech.models.EducationProgram;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.models.UserFavouriteProgram;
import com.example.magistracypolytech.repositories.UserFavouriteProgramRepository;
import com.example.magistracypolytech.services.EducationProgramService;
import com.example.magistracypolytech.services.UserFavouriteProgramService;
import com.example.magistracypolytech.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFavouriteProgramServiceTest {

    @Mock
    private UserFavouriteProgramRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private EducationProgramService programService;

    @Mock
    private EducationProgramMapper programMapper;

    @InjectMocks
    private UserFavouriteProgramService favouriteProgramService;

    @Test
    void setFavouriteProgramToUser_shouldSaveNewFavorite() {
        // Arrange
        User user = new User();
        user.setId(1L);

        EducationProgram program = new EducationProgram();
        program.setCode("01.03.02");

        when(repository.getByProgram_CodeAndUser_id(anyString(), anyLong()))
                .thenReturn(Optional.empty());
        when(userService.findById(anyLong())).thenReturn(user);
        when(programService.findByCode(anyString())).thenReturn(program);

        // Act
        favouriteProgramService.setFavouriteProgramToUser("01.03.02", 1L);

        // Assert
        verify(repository).save(any(UserFavouriteProgram.class));
    }

    @Test
    void setFavouriteProgramToUser_shouldThrowWhenAlreadyExists() {
        // Arrange
        UserFavouriteProgram existing = new UserFavouriteProgram();
        when(repository.getByProgram_CodeAndUser_id(anyString(), anyLong()))
                .thenReturn(Optional.of(existing));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> {
            favouriteProgramService.setFavouriteProgramToUser("01.03.02", 1L);
        });

        verify(repository, never()).save(any());
    }

    @Test
    void deleteFavouriteProgramByUserIdAndCode_shouldCallRepository() {
        // Arrange - нет необходимости настраивать моки, так как метод void

        // Act
        favouriteProgramService.deleteFavouriteProgramByUserIdAndCode("01.03.02", 1L);

        // Assert
        verify(repository).deleteByProgram_CodeAndUser_Id("01.03.02", 1L);
    }

    @Test
    void getFavouriteProgramsByUser_shouldReturnMappedDTOs() {
        // Arrange
        UserFavouriteProgram favorite = new UserFavouriteProgram();
        EducationProgram program = new EducationProgram();
        favorite.setProgram(program);

        EducationProgramDTO dto = new EducationProgramDTO();
        dto.setCode("01.03.02");

        when(repository.getUserFavouriteProgramsByUser_id(anyLong()))
                .thenReturn(Collections.singletonList(favorite));
        when(programMapper.toDTO(any(EducationProgram.class))).thenReturn(dto);

        // Act
        List<EducationProgramDTO> result = favouriteProgramService.getFavouriteProgramsByUser(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("01.03.02", result.get(0).getCode());
        verify(programMapper).toDTO(program);
    }

    @Test
    void getUsersEmailByProgramId_shouldReturnEmailsWhenPresent() {
        // Arrange
        EmailDTO emailDto = new EmailDTO("test@example.com", "test");
        when(repository.findAllByProgramId(anyLong()))
                .thenReturn(Optional.of(Collections.singletonList(emailDto)));

        // Act
        List<EmailDTO> result = favouriteProgramService.getUsersEmailByProgramId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
    }

    @Test
    void getUsersEmailByProgramId_shouldReturnNullWhenNotPresent() {
        // Arrange
        when(repository.findAllByProgramId(anyLong())).thenReturn(Optional.empty());

        // Act
        List<EmailDTO> result = favouriteProgramService.getUsersEmailByProgramId(1L);

        // Assert
        assertNull(result);
    }
}
