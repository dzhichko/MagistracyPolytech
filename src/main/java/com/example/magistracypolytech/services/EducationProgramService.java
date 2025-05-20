package com.example.magistracypolytech.services;


import com.example.magistracypolytech.dto.EducationProgramDTO;
import com.example.magistracypolytech.repositories.EducationProgramRepository;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class EducationProgramService {

    private final EducationProgramRepository programRepository;

    @Autowired
    public EducationProgramService(EducationProgramRepository programRepository){
        this.programRepository = programRepository;
    }

    public List<EducationProgramDTO> getAllPrograms(){
        return programRepository.findAll().
                stream().map(program -> EducationProgramDTO.builder()
                        .name(program.getName())
                        .code(program.getCode()).build()).collect(Collectors.toList());
    }

    private void savePdfFromUrl(String url, String filename) throws IOException {
        Connection.Response response = Jsoup.connect(url)
                .ignoreContentType(true)
                .execute();

        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(filename))) {
            out.write(response.bodyAsBytes());
        }
    }



}

