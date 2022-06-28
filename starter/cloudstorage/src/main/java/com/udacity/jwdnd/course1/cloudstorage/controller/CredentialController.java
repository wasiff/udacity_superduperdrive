package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    @Autowired
    UserService userService;

    @Autowired
    CredentialService credentialService;

    @PostMapping()
    public String createUpdateCredential(Credential credential) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(username);

        if (credential.getCredentialid() != null && credential.getCredentialid() > 0) {
            credentialService.updateCredential(credential);
        } else {
            credentialService.addCredential(credential,user.getUserid());
        }
        return "redirect:/result?success";
    }

    @GetMapping ("/delete")
    public String delete(@RequestParam("id") int credentialid) {
        if (credentialid > 0) {
            credentialService.deleteCredential(credentialid);
            return "redirect:/result?success";
        }
        return "redirect:/result?error";
    }

}
