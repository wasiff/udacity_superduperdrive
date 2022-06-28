package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    private NoteMapper noteMapper;

    public List<Note> getAllNotes(int userid) throws Exception {
        List<Note> notes = noteMapper.findByUserId(userid);
        if (notes == null) {
            throw new Exception();
        }
        return notes;
    }

    public void addNote(Note note, int userid) {
        noteMapper.insert(note, userid);
    }

    public void updateNote(Note note) {
        noteMapper.updateNote(note);
    }

    public void deleteNote(int noteid) {
        noteMapper.deleteNote(noteid);
    }

}
