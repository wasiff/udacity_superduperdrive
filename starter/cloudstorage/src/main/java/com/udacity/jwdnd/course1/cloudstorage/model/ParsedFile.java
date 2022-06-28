package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.Builder;

@Builder
public class ParsedFile {
    private int fileid;
    private String filename;
    private String dataURL;

    public ParsedFile() {}

    public ParsedFile(int fileid, String filename, String dataURL) {
        this.fileid = fileid;
        this.filename = filename;
        this.dataURL = dataURL;
    }

    public int getFileid() {
        return fileid;
    }

    public void setFileid(int fileid) {
        this.fileid = fileid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getDataURL() {
        return dataURL;
    }

    public void setDataURL(String dataURL) {
        this.dataURL = dataURL;
    }
}