package com.example.magistracypolytech.services;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EducationProgramService {
    private static final String URL = "https://www.spbstu.ru/abit/master/to-choose-the-direction-of-training/education-program/";

    public Map<String, String> getProgramList() throws IOException {
        Document doc = Jsoup.connect(URL).get();
        Map<String, String> programMap = new HashMap<>();

        Elements titles = doc.select(".panel-title g.accordion-toggle");

        for (Element title : titles) {
            String fullText = title.text();
            String[] parts = fullText.split(" ", 2);
            if (parts.length == 2) {
                String code = parts[0];
                String name = parts[1];
                System.out.println(code + " | " + name);
                programMap.put(code, name);
            }
        }
        System.out.println(programMap);
        return programMap;
    }
}
