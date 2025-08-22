package com.FK.Notes.Secrets.Service;

import com.FK.Notes.Secrets.Models.Notes;
import com.FK.Notes.Secrets.Repo.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.FK.Notes.Secrets.Models.Notes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.NotificationEmitter;
import java.util.List;
@Service
public class NoteService implements NoteServiceInterface{

    @Autowired
    private NoteRepository noteRepository;
    @Override
    public Notes createNoteForUser(String username, String content) {
        Notes notes = new Notes();
        notes.setContent(content);
        notes.setOwnerUsername(username);
        Notes savedNote = noteRepository.save(notes);
        return savedNote;
    }

    @Override
    public Notes updateNoteForUser(String username, Long noteId, String content) {
        Notes notesFromDb = noteRepository.findById(noteId).orElseThrow(()->new RuntimeException("Note with id " + noteId + " does not exist."));
        notesFromDb.setContent(content);
        Notes updatedNotes = noteRepository.save(notesFromDb);
        return notesFromDb;
    }

    @Override
    public void deleteNoteForUser(Long noteId, String username) {
        noteRepository.deleteById(noteId);
    }

    @Override
    public List<Notes> getNotesForUser(String username) {
        List<Notes> personalNotes = noteRepository.findByOwnerUserName(username);
        return personalNotes;
    }
}
