package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    @Autowired
    UserService userService;

    @PostMapping()
    public String saveFile(MultipartFile fileUpload) throws IOException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getUser(username);

        if (fileUpload.isEmpty()) {
            return "redirect:/result?error";
        }
        fileService.addFile(fileUpload, user.getUserid());
        return "redirect:/result?success";
    }

    @GetMapping("/delete")
    public String deleteFile(@RequestParam("id") int fileid) {
        if (fileid > 0) {
            fileService.deleteFile(fileid);
            return "redirect:/result?success";
        }
        return "redirect:/result?error";
    }
}
