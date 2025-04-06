package com.example.magistracypolytech.services;
import com.example.magistracypolytech.dto.EducationProgram;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EducationProgramService {
    private static final String AJAX_URL = "https://www.spbstu.ru/abit/ajax_groups.php";

    public List<EducationProgram> getProgramList() throws IOException {
        List<EducationProgram> programList = new ArrayList<>();

        Connection.Response response = Jsoup.connect(AJAX_URL)
                .method(Connection.Method.POST)
                .data("ABITURIENT_LEVEL_ID", "2")
                .data("MEGA_ID", "")
                .data("CAMPAGIN_ID", "1")
                .data("form_1", "1")
                .data("finance_1", "1")
                .userAgent("Mozilla/5.0")
                .referrer("https://www.spbstu.ru/abit/master/to-choose-the-direction-of-training/education-program/")
                .execute();

        Document doc = response.parse();

        Elements programs = doc.select(".prof-item__header div");

        for (Element program : programs) {
            String text = program.text();
            String[] parts = text.split(" ", 2);
            if (parts.length == 2) {
                programList.add(new EducationProgram(parts[0], parts[1]));
            }
        }

        return programList;
    }
}