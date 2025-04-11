package com.example.magistracypolytech.services;

import com.example.magistracypolytech.models.EmailDTO;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.models.UserFavoriteProgram;
import com.example.magistracypolytech.repositories.UserFavoriteProgramRepository;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserFavoriteProgramService {

    private final UserFavoriteProgramRepository userFavoriteProgramRepository;

    @Autowired
    public UserFavoriteProgramService(UserFavoriteProgramRepository userFavoriteProgramRepository){
        this.userFavoriteProgramRepository = userFavoriteProgramRepository;
    }

    public List<EmailDTO> getUsersEmailByProgramId(long programId){
        Optional<List<EmailDTO>> userFavoriteProgramOpt = userFavoriteProgramRepository.findAllByProgramId(programId);

        return userFavoriteProgramOpt.orElse(null);
    }
}
