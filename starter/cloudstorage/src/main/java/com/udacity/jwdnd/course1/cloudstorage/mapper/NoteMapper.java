package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES (#{note.notetitle}, #{note.notedescription}, #{userid})")
    public int insert(Note note, int userid);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    public int updateNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteid}")
    public int deleteNote(int noteid);

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    public List<Note> findByUserId(int userid);
}
