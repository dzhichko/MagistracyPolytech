package com.example.magistracypolytech;

import com.example.magistracypolytech.services.EducationProgramService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class MagistracyPolytechApplication {
    public static void main(String[] args) throws IOException {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(e -> System.setProperty(e.getKey(), e.getValue()));

        EducationProgramService service = new EducationProgramService();
        System.out.println(service.getProgramList());

        SpringApplication.run(MagistracyPolytechApplication.class, args);
    }

}
