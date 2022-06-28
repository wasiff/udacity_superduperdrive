package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    public List<File> findByUserId(int userid);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, filedata, userid) VALUES (#{file.filename}, #{file.contenttype}, #{file.filesize}, #{file.filedata}, #{userid})")
    public int insertFile(File file, int userid);

    @Delete("DELETE FROM FILES WHERE fileid = #{fileid}")
    public int deleteFile(int fileid);

}
