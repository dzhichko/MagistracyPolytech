package com.example.magistracypolytech.services;


import com.example.magistracypolytech.models.EducationProgram;
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


@Service
public class EducationProgramService {

    private final EducationProgramRepository programRepository;

    @Autowired
    public EducationProgramService(EducationProgramRepository programRepository){
        this.programRepository = programRepository;
    }

    public List<EducationProgram> getAllPrograms(){
        return programRepository.findAll();
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

