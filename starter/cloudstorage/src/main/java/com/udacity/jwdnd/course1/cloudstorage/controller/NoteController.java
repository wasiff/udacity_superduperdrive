package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/notes")
public class NoteController {
    @Autowired
    NoteService noteService;
    @Autowired
    UserService userService;

    @PostMapping()
    public String createUpdateNote(Note note) {
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(username);

        if (note.getNoteid() != null && note.getNoteid() > 0) {
            noteService.updateNote(note);
        } else {
            noteService.addNote(note,user.getUserid());
        }
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteNote(@RequestParam("id") int noteid) {
        if (noteid > 0) {
            noteService.deleteNote(noteid);
            return "redirect:/result?success";
        }
        return "redirect:/result?error";
    }
}
