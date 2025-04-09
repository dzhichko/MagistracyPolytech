package com.example.magistracypolytech.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
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

    public EducationProgram(String code, String name, byte[] fileData){
        this.code = code;
        this.name = name;
        this.fileData = fileData;
    }

}