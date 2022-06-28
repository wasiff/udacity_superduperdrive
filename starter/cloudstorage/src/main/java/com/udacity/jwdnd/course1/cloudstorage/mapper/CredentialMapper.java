package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {
    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    public List<Credential> findByUserId(int userid);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userid) VALUES (#{credential.url}, #{credential.username}, #{credential.key}, #{credential.password}, #{userid})")
    public int insert(Credential credential, int userid);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialid}")
    public int delete(int credentialid);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, key = #{key}, password = #{password} WHERE credentialid = #{credentialid}")
    public int update(Credential credential);

}
