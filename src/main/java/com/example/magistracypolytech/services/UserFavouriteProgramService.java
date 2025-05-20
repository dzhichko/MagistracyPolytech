package com.example.magistracypolytech.services;

import com.example.magistracypolytech.dto.EmailDTO;
import com.example.magistracypolytech.models.EducationProgram;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.models.UserFavouriteProgram;
import com.example.magistracypolytech.repositories.UserFavouriteProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFavouriteProgramService {

    private final UserFavouriteProgramRepository repository;
    private final UserService userService;
    private final EducationProgramService programService;

    public void setFavouriteProgramToUser(long programId, long userId){
        UserFavouriteProgram userFavoriteProgram = new UserFavouriteProgram();
        User user = userService.findById(userId);
        EducationProgram educationProgram = programService.findById(programId);

        userFavoriteProgram.setUser(user);
        userFavoriteProgram.setProgram(educationProgram);

        repository.save(userFavoriteProgram);

    }

    public List<EmailDTO> getUsersEmailByProgramId(long programId){
        Optional<List<EmailDTO>> userFavoriteProgramOpt = repository.findAllByProgramId(programId);

        return userFavoriteProgramOpt.orElse(null);
    }
}
