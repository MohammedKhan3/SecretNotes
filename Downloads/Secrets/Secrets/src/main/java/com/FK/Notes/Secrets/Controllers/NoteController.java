package com.FK.Notes.Secrets.Controllers;

import com.FK.Notes.Secrets.Models.Notes;
import com.FK.Notes.Secrets.Service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    @Autowired
    private NoteService noteService;
    @PostMapping
    public Notes createNote(@RequestBody String content, @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();
        System.out.println("USER DETAILS: " + username);
        return noteService.createNoteForUser(username,content);
    }
    @GetMapping
    public List<Notes> getNotes(@AuthenticationPrincipal UserDetails userDetails){
        List<Notes> userNotes = noteService.getNotesForUser(userDetails.getUsername());
        return userNotes;
    }
    @PutMapping("/{noteId}")
    public Notes updateNote(@RequestParam Long noteId,@RequestBody String updatedNotes, @AuthenticationPrincipal UserDetails userDetails){

      return noteService.updateNoteForUser(userDetails.getUsername(),noteId,updatedNotes);
    }
    @DeleteMapping("/{noteId}")
    public void deleteNote(@RequestParam Long noteId,@AuthenticationPrincipal UserDetails userDetails){
        noteService.deleteNoteForUser(noteId, userDetails.getUsername());
    }

    /*

     */

}
