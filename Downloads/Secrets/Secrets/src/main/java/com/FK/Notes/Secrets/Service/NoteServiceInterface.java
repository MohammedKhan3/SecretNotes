package com.FK.Notes.Secrets.Service;
import com.FK.Notes.Secrets.Models.Notes;

import java.util.List;

public interface NoteServiceInterface {
    Notes createNoteForUser(String username, String content);
    Notes updateNoteForUser(String username,Long noteId, String content);
    void deleteNoteForUser(Long noteId, String username);
    List<Notes> getNotesForUser(String username);




}
