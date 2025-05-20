package com.example.magistracypolytech.repositories;

import com.example.magistracypolytech.dto.EmailDTO;
import com.example.magistracypolytech.models.UserFavoriteProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserFavoriteProgramRepository extends JpaRepository<UserFavoriteProgram, Long> {

    @Query("SELECT u.user.email as email, u.user.username as username FROM UserFavoriteProgram u WHERE u.program.id = :programId")
    Optional<List<EmailDTO>> findAllByProgramId(@Param("programId") Long programId);

}
