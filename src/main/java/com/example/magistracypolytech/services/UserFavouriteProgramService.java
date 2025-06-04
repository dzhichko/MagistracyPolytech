package com.example.magistracypolytech.services;

import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.dto.EmailDTO;
import com.example.magistracypolytech.mappers.EducationProgramMapper;
import com.example.magistracypolytech.models.EducationProgram;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.models.UserFavouriteProgram;
import com.example.magistracypolytech.repositories.UserFavouriteProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFavouriteProgramService {

    private final UserFavouriteProgramRepository repository;
    private final UserService userService;
    private final EducationProgramService programService;
    private final EducationProgramMapper programMapper;

    public void setFavouriteProgramToUser(String code, long userId){
        repository.getByProgram_CodeAndUser_id(code, userId).ifPresent(userFavouriteProgram -> {throw new BadCredentialsException("User favourite program already exists");});


        UserFavouriteProgram userFavoriteProgram = new UserFavouriteProgram();
        User user = userService.findById(userId);
        EducationProgram educationProgram = programService.findByCode(code);

        userFavoriteProgram.setUser(user);
        userFavoriteProgram.setProgram(educationProgram);

        repository.save(userFavoriteProgram);
    }

    @Transactional
    public void deleteFavouriteProgramByUserIdAndCode(String code, long userId){
        try {
            repository.deleteByProgram_CodeAndUser_Id(code, userId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public List<EducationProgramDTO> getFavouriteProgramsByUser(long userId){
        return repository.getUserFavouriteProgramsByUser_id(userId).stream().
                map(UserFavouriteProgram::getProgram).
                map(programMapper::toDTO).toList();
    }

    public List<EmailDTO> getUsersEmailByProgramId(long programId){
        Optional<List<EmailDTO>> userFavoriteProgramOpt = repository.findAllByProgramId(programId);
        return userFavoriteProgramOpt.orElse(null);
    }
}
