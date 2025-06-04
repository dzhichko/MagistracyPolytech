package com.example.magistracypolytech.models;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="education_programs")
public class EducationProgram {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private long id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "BYTEA")
    private byte[] fileData;

    @Column(nullable = false)
    private String institution;

    @Column
    private String budgetPlace;

    @Column
    private String contractPlace;

    public EducationProgram(String code, String name, byte[] fileData, String institution, String budgetPlace, String contractPlace) {
        this.code = code;
        this.name = name;
        this.fileData = fileData;
        this.institution = institution;
        this.budgetPlace = budgetPlace;
        this.contractPlace = contractPlace;
    }

}