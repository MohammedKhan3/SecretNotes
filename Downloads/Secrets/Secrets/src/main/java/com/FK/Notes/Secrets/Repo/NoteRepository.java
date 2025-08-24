package com.FK.Notes.Secrets.Repo;

import com.FK.Notes.Secrets.Models.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Notes,Long> {
    List<Notes> findByOwnerUsername(String ownerUsername);
}
