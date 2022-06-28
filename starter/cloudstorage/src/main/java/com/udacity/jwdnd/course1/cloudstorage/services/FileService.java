package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.ParsedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileService {
    @Autowired
    private FileMapper filesMapper;

    public List<ParsedFile> getAllFiles(int userid) throws Exception {
        List<File> files = filesMapper.findByUserId(userid);
        if (files == null) {
            throw new Exception();
        }
        return files.stream().map(this::generateParsedFile).collect(Collectors.toList());
    }

    private ParsedFile generateParsedFile(File file) {
        String base64 = Base64.getEncoder().encodeToString(file.getFiledata());
        String dataURL = "data:" + file.getContenttype() + ";base64," + base64;
        return ParsedFile.builder().fileid(file.getFileId()).filename(file.getFilename()).dataURL(dataURL).build();
    }

    public void addFile(MultipartFile fileUpload, int userid) throws IOException {
        File file = new File();
        try {
            file.setContenttype(fileUpload.getContentType());
            file.setFiledata(fileUpload.getBytes());
            file.setFilename(fileUpload.getOriginalFilename());
            file.setFilesize(Long.toString(fileUpload.getSize()));
        } catch (IOException e) {
            throw e;
        }
        filesMapper.insertFile(file, userid);
    }

    public void deleteFile(int fileid) {
        filesMapper.deleteFile(fileid);
    }
}
