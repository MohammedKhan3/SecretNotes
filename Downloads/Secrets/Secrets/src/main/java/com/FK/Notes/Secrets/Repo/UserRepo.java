package com.FK.Notes.Secrets.Repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.FK.Notes.Secrets.Models.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User>findByUserName(String username);
}
