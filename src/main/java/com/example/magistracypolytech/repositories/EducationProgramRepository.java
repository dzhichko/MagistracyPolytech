package com.example.magistracypolytech.repositories;

import com.example.magistracypolytech.models.EducationProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EducationProgramRepository extends JpaRepository<EducationProgram, Long> {
    Optional<EducationProgram> findByCode(String code);
}
