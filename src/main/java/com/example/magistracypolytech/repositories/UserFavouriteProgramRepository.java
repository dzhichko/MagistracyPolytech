package com.example.magistracypolytech.repositories;

import com.example.magistracypolytech.dto.EmailDTO;
import com.example.magistracypolytech.models.User;
import com.example.magistracypolytech.models.UserFavouriteProgram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserFavouriteProgramRepository extends JpaRepository<UserFavouriteProgram, Long> {

    @Query("SELECT u.user.email as email, u.user.username as username FROM UserFavouriteProgram u WHERE u.program.id = :programId")
    Optional<List<EmailDTO>> findAllByProgramId(@Param("programId") Long programId);

    void deleteByProgram_CodeAndUser_Id(String code, Long userId);

    List<UserFavouriteProgram> getUserFavouriteProgramsByUser_id(Long userId);

    Optional<UserFavouriteProgram> getByProgram_CodeAndUser_id(String code, Long userId);

    List<UserFavouriteProgram> user(User user);
}
