package com.example.magistracypolytech.services;

import com.example.magistracypolytech.models.EducationProgram;
import com.example.magistracypolytech.dto.EmailDTO;
import com.example.magistracypolytech.repositories.EducationProgramRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EducationProgramScheduler {
    private static final String AJAX_URL = "https://www.spbstu.ru/abit/ajax_groups.php";
    private static final String DOWNLOAD_URL = "https://www.spbstu.ru/";

    private final EmailServiceImpl emailService;
    private final UserFavouriteProgramService userFavouriteProgramService;
    private final EducationProgramRepository programRepository;

    @PostConstruct
    public void init() {
        setEducationPrograms();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void scheduledDownload() {
        setEducationPrograms();
    }


    @CacheEvict(value = "programs", allEntries = true)
    public void setEducationPrograms(){
        try {
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

            Elements programBlocks = doc.select(".prof-item");

            for (Element programBlock : programBlocks) {
                Element specElement = programBlock.selectFirst(".prof-item__spec");
                Element linkElement = programBlock.selectFirst(".exams__item a[href]");

                if (specElement != null && linkElement != null) {
                    String fullText = specElement.text();
                    String pdfUrl = linkElement.attr("abs:href");

                    String[] parts = fullText.split("\\s+", 2);
                    if (parts.length == 2) {
                        byte[] pdfData = downloadPdf(pdfUrl);
                        Optional<EducationProgram> existingProgramOpt = programRepository.findByCode(parts[0]);
                        EducationProgram existingProgram;
                        if (existingProgramOpt.isPresent()) {
                            existingProgram = existingProgramOpt.orElseGet(EducationProgram::new);

                            if(!Arrays.equals(existingProgram.getFileData(), pdfData)){
                                List<EmailDTO> emailsToNotified = userFavouriteProgramService.getUsersEmailByProgramId(existingProgram.getId());
                                emailsToNotified.forEach(m -> emailService.sendSimpleEmail(m.getEmail(),
                                        "Dear "+ m.getUsername() + " " + existingProgram.getName(),
                                        "Dear "+ m.getUsername() + " " + existingProgram.getName() + " has been changed!"));
                            }

                            existingProgram.setName(parts[1]);
                            existingProgram.setFileData(pdfData);
                        } else {
                            existingProgram = new EducationProgram(parts[0], parts[1], pdfData);
                        } programRepository.save(existingProgram);
                    }
                }
            }
        }
        catch (IOException e){
            System.out.println("Error with download education program info");
        }
    }

    private byte[] downloadPdf(String pdfUrl) throws IOException {
        if (!pdfUrl.startsWith("http")) {
            pdfUrl = DOWNLOAD_URL + pdfUrl;
        }

        try (InputStream in = new URL(pdfUrl).openStream()) {
            return in.readAllBytes();
        }
    }


}