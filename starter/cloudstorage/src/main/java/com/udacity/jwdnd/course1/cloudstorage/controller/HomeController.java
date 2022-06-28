package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    UserService userService;

    @Autowired
    NoteService noteService;

    @Autowired
    CredentialService credentialService;

    @Autowired
    FileService fileService;


    @GetMapping()
    public String homeView(Authentication authentication, Model model) throws Exception {
        String username = (String) authentication.getPrincipal();
        User user = userService.getUser(username);
        model.addAttribute("notes", noteService.getAllNotes(user.getUserid()));
        model.addAttribute("credentials", credentialService.getAllCredentials(user.getUserid()));
        model.addAttribute("credentialService", credentialService);
        model.addAttribute("files", fileService.getAllFiles(user.getUserid()));


        return "home";
    }

}
