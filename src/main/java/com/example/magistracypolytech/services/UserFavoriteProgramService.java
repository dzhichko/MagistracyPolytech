package com.example.magistracypolytech.services;

import com.example.magistracypolytech.dto.EmailDTO;
import com.example.magistracypolytech.repositories.UserFavoriteProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
