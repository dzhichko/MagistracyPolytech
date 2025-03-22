package com.example.magistracypolytech.controllers;

import com.example.magistracypolytech.services.EducationProgramService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController
public class ProgramController {

    private final EducationProgramService programParser;

    public ProgramController(EducationProgramService programParser) {
        this.programParser = programParser;
    }

    @GetMapping("/programs")
    @ResponseBody
    public Map<String,String> getPrograms(Model model) throws IOException {
        return programParser.getProgramList();
    }
}

